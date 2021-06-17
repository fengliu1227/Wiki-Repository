package com.andrew.wiki.service;

import com.andrew.wiki.domain.*;
import com.andrew.wiki.exception.BusinessException;
import com.andrew.wiki.exception.BusinessExceptionCode;
import com.andrew.wiki.mapper.*;
import com.andrew.wiki.request.DocQueryRequest;
import com.andrew.wiki.request.DocSaveRequest;
import com.andrew.wiki.request.DocVoteRequest;
import com.andrew.wiki.response.DocQueryResponse;
import com.andrew.wiki.response.PageResponse;
import com.andrew.wiki.util.CopyUtil;
import com.andrew.wiki.util.SnowFlake;
import com.andrew.wiki.websocket.WebSocketServer;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private DocCustMapper docCustMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private User2VoteMapper user2VoteMapper;

    @Autowired
    private WsService wsService;

    @Autowired
    private EBookCustMapper eBookCustMapper;

    @Autowired
    private SnowFlake snowFlake;

    public List<DocQueryResponse> getAll(){
        DocExample docExample = new DocExample();
//        DocExample.Criteria criteria = docExample.createCriteria();
        docExample.setOrderByClause("sort asc");
        List<Doc> list = docMapper.selectByExample(docExample);


        List<DocQueryResponse> resList = CopyUtil.copyList(list, DocQueryResponse.class);
        return resList;
    }

    public PageResponse<DocQueryResponse> getList(DocQueryRequest req){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
//        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> list = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(list);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<DocQueryResponse> resList = CopyUtil.copyList(list, DocQueryResponse.class);
        PageResponse<DocQueryResponse> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(resList);
        return pageResponse;
    }

    public List<DocQueryResponse> getByEbookId(Long id){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        docExample.setOrderByClause("sort asc");
        criteria.andEbookIdEqualTo(id);
        List<Doc> list = docMapper.selectByExample(docExample);


        List<DocQueryResponse> resList = CopyUtil.copyList(list, DocQueryResponse.class);
        return resList;
    }

    @Transactional
    public void update(DocSaveRequest req){
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        docMapper.updateByPrimaryKey(doc);
        int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
        if(count < 1){
            contentMapper.insert(content);
        }
    }

    @Transactional
    public void save(DocSaveRequest req){
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        doc.setId(snowFlake.nextId());
        content.setId(doc.getId());
        doc.setViewCount(0);
        doc.setVoteCount(0);
        docMapper.insert(doc);
        contentMapper.insert(content);
        eBookCustMapper.increaseDocCount(doc.getEbookId());
    }

    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }


    @Transactional
    public void delete(List<String> ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        Long eBookId = docMapper.selectByPrimaryKey(Long.parseLong(ids.get(0))).getEbookId();
        docMapper.deleteByExample(docExample);
        ContentExample contentExample = new ContentExample();
        ContentExample.Criteria contentCriteria = contentExample.createCriteria();;
        contentCriteria.andIdIn(ids);
        contentMapper.deleteByExample(contentExample);
        Integer nums = ids.size();
        eBookCustMapper.decreaseDocCount(nums, eBookId);
    }


    @Transactional
    public DocQueryResponse vote(DocVoteRequest req){
        User userDb = userMapper.selectByPrimaryKey(req.getUserId());
        if(ObjectUtils.isEmpty(userDb)){
            throw new BusinessException(BusinessExceptionCode.UNEXISTED_USER);
        }
        User2VoteExample user2VoteExample = new User2VoteExample();
        User2VoteExample.Criteria criteria = user2VoteExample.createCriteria();
        criteria.andDocIdEqualTo(req.getDocId());
        criteria.andUserIdEqualTo(req.getUserId());
        List<User2Vote> user2Votes = user2VoteMapper.selectByExample(user2VoteExample);
        if(!CollectionUtils.isEmpty(user2Votes) || user2Votes.size() > 1){
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
        User2Vote user2Vote = new User2Vote();
        user2Vote.setDocId(req.getDocId());
        user2Vote.setUserId(req.getUserId());
        user2Vote.setId(snowFlake.nextId());
        user2VoteMapper.insert(user2Vote);
        docCustMapper.increaseVoteCount(req.getDocId());
        Doc docDb = docMapper.selectByPrimaryKey(req.getDocId());
        DocQueryResponse docQueryResponse = CopyUtil.copy(docDb, DocQueryResponse.class);

        eBookCustMapper.increaseVoteCount(docDb.getEbookId());
        String logId = MDC.get("LOG_ID");
        wsService.sendInfo(docDb.getName(), userDb.getName(), logId);
        return docQueryResponse;
    }


    public Boolean isVoted(Long userId, Long docId){
        User userDb = userMapper.selectByPrimaryKey(userId);
        if(ObjectUtils.isEmpty(userDb)){
            throw new BusinessException(BusinessExceptionCode.UNEXISTED_USER);
        }
        User2VoteExample user2VoteExample = new User2VoteExample();
        User2VoteExample.Criteria criteria = user2VoteExample.createCriteria();
        criteria.andDocIdEqualTo(docId);
        criteria.andUserIdEqualTo(userId);
        List<User2Vote> user2Votes = user2VoteMapper.selectByExample(user2VoteExample);
        if(!CollectionUtils.isEmpty(user2Votes) || user2Votes.size() > 1){
            return true;
        }
        return false;
    }

    public void updateEbookInfo(){
        docCustMapper.updateEbookInfo();
    }

}
