<template>
    <a-layout-header class="header">
        <a-menu
                theme="dark"
                mode="horizontal"
                :style="{ lineHeight: '64px'}"
        >
            <a-menu-item key="/">
                <router-link to="/">Home</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/user" :style="user.role === 'ROLE_ADMIN'? {} : {display:'none'}">
                <router-link to="/admin/user">User mgmt</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/ebook" :style="user.role === 'ROLE_ADMIN'? {} : {display:'none'}">
                <router-link to="/admin/ebook">Ebook mgmt</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/category" :style="user.role === 'ROLE_ADMIN'? {} : {display:'none'}">
                <router-link to="/admin/category">Category mgmt</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/statistics" :style="user.role === 'ROLE_ADMIN'? {} : {display:'none'}">
                <router-link to="/admin/statistics">statistics</router-link>
            </a-menu-item>
                <a-popconfirm
                        title="Are you sure to log out?"
                        ok-text="Yes"
                        cancel-text="No"
                        @confirm="logout()"
                >
                    <a class="login-menu" v-show="user.id">
                        <span>Log out</span>
                    </a>
                </a-popconfirm>

            <a class="login-menu" v-show="user.id">
                <span>Hi：{{user.name}}</span>
            </a>

            <a class="login-menu" v-show="!user.id" @click="showLoginModal" >
                <span>Login</span>
            </a>

            <a class="login-menu" v-show="!user.id" @click="showRegisterModal" >
                <span>Register</span>
            </a>

        </a-menu>

        <a-modal
                title="Login"
                v-model:visible="loginModalVisible"
                :confirm-loading="loginModalLoading"
                @ok="login"
        >
            <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
                <a-form-item label="LoginName">
                    <a-input v-model:value="loginUser.loginName" />
                </a-form-item>
                <a-form-item label="Password">
                    <a-input v-model:value="loginUser.password" type="password" />
                </a-form-item>
            </a-form>
        </a-modal>

        <a-modal
                title="Register"
                v-model:visible="registerModalVisible"
                :confirm-loading="registerModalLoading"
                @ok="register"
        >
            <a-form :model="RegisterUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
                <a-form-item label="LoginName">
                    <a-input v-model:value="RegisterUser.loginName" :disabled="!!user.id"/>
                </a-form-item>
                <a-form-item label="Name">
                    <a-input v-model:value="RegisterUser.name" />
                </a-form-item>
                <a-form-item label="Password" v-show="!user.id">
                    <a-input v-model:value="RegisterUser.password" type="password"/>
                </a-form-item>
            </a-form>
        </a-modal>


    </a-layout-header>
</template>

<script lang="ts">
    import { defineComponent, ref, computed } from 'vue';
    import axios from 'axios';
    import { message } from 'ant-design-vue';
    import store from "@/store";
    import { Tool } from '@/util/tool';
    import router from "@/router";


    const hexMd5 = require('js-md5');

    export default defineComponent({
        name: 'the-header',
        setup () {
            // store the user after login
            const user = computed(() => store.state.user);

            // use for login
            const loginUser = ref({
                loginName: "",
                password: ""
            });
            const RegisterUser = ref();
            RegisterUser.value={};
            const loginModalVisible = ref(false);
            const loginModalLoading = ref(false);
            const showLoginModal = () => {
                loginModalVisible.value = true;
            };


            const registerModalVisible = ref(false);
            const registerModalLoading = ref(false);
            const showRegisterModal = () => {
                registerModalVisible.value = true;
            };


            // login
            const login = () => {
                loginModalLoading.value = true;
                loginUser.value.password = hexMd5(loginUser.value.password);
                if(Tool.isNotEmpty(loginUser)){
                    axios.post('/user/login', loginUser.value).then((response) => {
                        loginModalLoading.value = false;
                        const data = response.data;
                        if (data.success) {
                            loginModalVisible.value = false;
                            message.success("Login successfully！");

                            store.commit("setUser", data.content);
                        } else {
                            message.error(data.message);
                        }
                    });
                }
            };

            // login
            const login2 = (givenUser: any) => {
                loginModalLoading.value = true;
                givenUser.value.password = hexMd5(givenUser.value.password);
                if(Tool.isNotEmpty(givenUser)){
                    axios.post('/user/login', givenUser.value).then((response) => {
                        loginModalLoading.value = false;
                        const data = response.data;
                        if (data.success) {
                            loginModalVisible.value = false;
                            message.success("Login successfully！");

                            store.commit("setUser", data.content);
                        } else {
                            message.error(data.message);
                        }
                    });
                }
            };

            // logout
            const logout = () => {
                axios.post('/user/logout/' + user.value.token).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        message.success("Log out successfully！！");
                        store.commit("setUser", {});
                        router.push('/');
                    } else {
                        message.error(data.message);
                    }
                });
            };

            //register

            const register = () => {
                registerModalLoading.value = true;
                const registerPass = RegisterUser.value.password;
                RegisterUser.value.password = hexMd5(RegisterUser.value.password );
                    axios.post("/user", RegisterUser.value).then((response) =>{
                        registerModalLoading.value = false;
                        const data = response.data;
                        if (data.success) {

                            //login
                            RegisterUser.value.password = registerPass;
                            login2(RegisterUser);
                            registerModalVisible.value = false;
                        }else {
                            message.error(data.message);
                        }
                    })

            };

            return {
                loginModalVisible,
                loginModalLoading,
                showLoginModal,
                loginUser,
                login,
                user,
                logout,
                register,
                registerModalVisible,
                registerModalLoading,
                showRegisterModal,
                RegisterUser
            }
        }
    });
</script>

<style>
    .login-menu {
        float: right;
        color: white;
        padding-left: 20px;
    }

    .ant-menu-overflow {
        display: flow-root !important;
    }
    



</style>
