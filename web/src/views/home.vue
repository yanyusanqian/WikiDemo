<template>
    <a-layout>
        <a-layout-sider width="200" style="background: #fff">
            <a-menu
                    mode="inline"
                    :style="{ height: '100%', borderRight: 0 }"
                    @click="handleClick"
            >
                <a-menu-item key="welcome">
                    <MailOutlined/>
                    <span>欢迎</span>
                </a-menu-item>
                <a-sub-menu v-for="item in level1" :key="item.id">
                    <template v-slot:title>
                        <span>
                            <user-outlined/>
                            {{ item.name }}
                        </span>
                    </template>
                    <a-menu-item v-for="child in  item.children" :key="child.id">
                        <MailOutlined/>
                        <span>
                            {{ child.name }}
                        </span>
                    </a-menu-item>
                </a-sub-menu>
            </a-menu>
        </a-layout-sider>

        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
            <div class="welcome" v-show="isShowWelcome" style="text-align: center">
                <the-welcome></the-welcome>
            </div>
            <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{gutter : 20, column: 3}"
                    :data-source="ebooks">
                <template #renderItem="{ item }">
                    <a-list-item key="item.name">
                        <template #actions>
                            <span>
                                <component v-bind:is="'FileOutlined'" style="margin-right: 8px"/>
                                {{ item.docCount }}
                            </span>
                            <span>
                                <component v-bind:is="'UserOutlined'" style="margin-right: 8px"/>
                                {{ item.viewCount }}
                            </span>
                            <span>
                                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
                                {{ item.voteCount }}
                            </span>
                        </template>

                        <a-list-item-meta :description="item.description">
                            <template #title>
                                <router-link :to="'/doc?ebookId=' + item.id">{{ item.name }}</router-link>
                            </template>
                            <template #avatar>
                                <a-avatar :src="item.cover"/>
                            </template>
                        </a-list-item-meta>
                    </a-list-item>
                </template>
            </a-list>
        </a-layout-content>
        <!--        </a-layout>-->
    </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive, toRef} from 'vue';
import {LaptopOutlined, NotificationOutlined, UserOutlined} from "@ant-design/icons-vue"; // @ is an alias to /src
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import TheWelCome from '@/components/TheWelcome.vue';
import TheWelcome from "@/components/TheWelcome.vue";

export default defineComponent({
    name: 'Home',
    setup() {
        const ebooks = ref();
        const ebooks1 = reactive({books: []});

        /**
         * 查询所有分类
         */
        const level1 = ref();
        let categorys: any;
        const handleQueryCategory = () => {
            axios.get("/category/all").then((response) => {
                const data = response.data;
                if (data.success) {
                    categorys = data.content;
                    console.log("原始数组:", categorys);

                    level1.value = [];
                    level1.value = Tool.array2Tree(categorys, 0);
                    console.log("树型结构:", level1.value);
                } else {
                    message.error(data.message);
                }
            });
        };

        /**
         * 查询电子书
         */
        const handleQueryEbook = () => {
            axios.get("/ebook/list", {
                params: {
                    page: 1,
                    size: 1000,
                    category2Id: category2Id,
                }
            }).then((response) => {
                const data = response.data;
                ebooks.value = data.content.list;
                // ebooks1.books = data.content;
            });
        }

        const isShowWelcome = ref(true);
        let category2Id = 0;
        const handleClick = (value: any) => {
            if (value.key === 'welcome') {
                isShowWelcome.value = true;
            } else {
                category2Id = value.key;
                isShowWelcome.value = false;
                handleQueryEbook();
            }
        }

        onMounted(() => {
            handleQueryCategory();
        });

        return {
            ebooks,
            // ebooks2: toRef(ebooks1, "books"),
            // listData,
            pagination: {
                onChange: (page: any) => {
                    console.log(page);
                },
                pageSize: 3,
            },
            /*actions: [
                {type: 'StarOutlined', text: '156'},
                {type: 'LikeOutlined', text: '156'},
                {type: 'MessageOutlined', text: '2'},
            ],*/

            level1,
            handleClick,

            isShowWelcome,
        }
    },
    components: {
        UserOutlined,
        LaptopOutlined,
        NotificationOutlined,
        TheWelcome
    },
});
</script>

<style scoped>
.ant-avatar {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
}
</style>
