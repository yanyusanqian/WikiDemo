<template>
    <a-layout-header class="header">
        <div class="logo"><img src="../assets/logo.png">WIKI</div>
        <a-menu
                theme="dark"
                mode="horizontal"
                :style="{ lineHeight: '64px' }"
        >

            <a-menu-item key="/">
                <router-link to="/">首页</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/user" :style="user.id? {} : {display: 'none'}">
                <router-link to="/admin/user">用户管理</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/ebook" :style="user.id? {} : {display: 'none'}">
                <router-link to="/admin/ebook">电子书管理</router-link>
            </a-menu-item>
            <a-menu-item key="/admin/category" :style="user.id? {} : {display: 'none'}">
                <router-link to="/admin/category">分类管理</router-link>
            </a-menu-item>
            <a-menu-item key="/about">
                <router-link to="/about">关于我们</router-link>
            </a-menu-item>

            <div class="login-modal">
                <a class="login-menu" :style="user.id? {} : {display: 'none'}">
                    <span>您好：{{user.name}}</span>
                </a>

                <a class="login-menu" :style="user.id? {display: 'none'} : {}" @click="showLoginModal">
                    <span>登录</span>
                </a>
                <a-popconfirm
                    title="确认退出登录?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="logout()"
                >
                    <a class="login-menu" v-show="user.id">
                        <span>退出登录</span>
                    </a>
                </a-popconfirm>
            </div>
        </a-menu>



        <a-modal
            title="登录"
            v-model:visible="loginModalVisible"
            :confirm-loading="loginModalLoading"
            @ok="login"
        >
            <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
                <a-form-item label="登录名">
                    <a-input v-model:value="loginUser.loginName" />
                </a-form-item>
                <a-form-item label="密码">
                    <a-input v-model:value="loginUser.password" type="password" />
                </a-form-item>
            </a-form>
        </a-modal>
    </a-layout-header>
</template>
<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from "axios";
import { message } from 'ant-design-vue';
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
    name: 'the-header',
    setup(){

        // ----------------登录-------------
        // 用于登录后保存
        const user = computed(() => store.state.user);
        // 用于登录
        const loginUser = ref({
            loginName: "test11",
            password: "test123"
        });

        const loginModalVisible = ref(false);
        const loginModalLoading = ref(false);
        const showLoginModal = () => {
            loginModalVisible.value = true;
        };

        // 登录
        const login = () => {
            console.log("开始登录");
            loginModalLoading.value = true;
            loginUser.value.password = hexMd5(loginUser.value.password + KEY);
            axios.post('/user/login', loginUser.value).then((response) => {
                loginModalLoading.value = false;
                const data = response.data;
                if (data.success) {
                    loginModalVisible.value = false;
                    store.commit("setUser", data.content);
                    message.success("登录成功！");
                } else {
                    message.error(data.message);
                }
            });
        };

        const logout = () => {
          console.log("退出登录");
          axios.get('/user/logout/'+ user.value.token).then((response) => {
              const data =  response.data;
              if(data.success){
                  message.success("退出登录成功！");
                  store.commit("setUser",{});
              }else{
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
        }
    }
});
</script>

<style>
.logo{
    font-size: 30px;
    font-weight: bold;
    float: left;
    color:white;
    padding-right: 40px;
}
.logo img{
    width: 30px;
    height: 30px;
    margin-right: 18px;
    margin-top: 18px;
    float: left;
}
.login-modal {
    margin-left: auto;
    order: 5;
    align-self: flex-end;
}
.login-menu{
    color: white;
    padding-left: 10px;
}
</style>