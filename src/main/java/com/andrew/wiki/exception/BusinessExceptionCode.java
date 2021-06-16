package com.andrew.wiki.exception;

public enum BusinessExceptionCode {

    USER_LOGIN_NAME_EXIST("Login Name Already Existed"),
    LOGIN_USER_ERROR("Login Name or Password incorrect"),
    VOTE_REPEAT("You Already Voted It"),
    UNEXISTED_USER("This User Doesn't Exist"),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
