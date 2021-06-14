<template>
    <a-layout-header class="header">
        <div class="logo" />
        <a-menu
                theme="dark"
                mode="horizontal"
                v-model:selectedKeys="selectedKeys1"
                :style="{ lineHeight: '64px' }"
        >
            <a-menu-item key="home">
                <router-link to="/">Home</router-link>
            </a-menu-item>
            <a-menu-item key="admin">
                <router-link to="/admin/ebook">Admin</router-link>
            </a-menu-item>
            <a-menu-item key="adminCategory">
                <router-link to="/admin/category">Admin2</router-link>
            </a-menu-item>
            <a-menu-item key="about">
                <router-link to="/about">About</router-link>
            </a-menu-item>
        </a-menu>
    </a-layout-header>
</template>

<script lang="ts">
    import { defineComponent } from 'vue';

    export default defineComponent({
        name: 'the-header'
    });
</script>