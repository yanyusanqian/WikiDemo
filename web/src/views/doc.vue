<template>
    <a-layout>
        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
            <h3 v-if="level1.length === 0">对不起，找不到相关文档！</h3>
            <a-row>
                <a-col :span="6">
                    <a-tree
                            v-if="level1.length > 0"
                            :tree-data="level1"
                            :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                            :defaultExpandAll="true"
                            :defaultSelectedKeys="defaultSelectedKeys"
                    >
                    </a-tree>
                </a-col>
                <a-col :span="18">

                </a-col>
            </a-row>
        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, createVNode} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

export default defineComponent({
    name: 'Doc',
    setup() {
        const route = useRoute();
        const docs = ref();
        const html = ref();
        const defaultSelectedKeys = ref();
        defaultSelectedKeys.value = [];
        // 当前选中的文档
        const doc = ref();
        doc.value = {};

        /**
         * 一级文档树，children属性就是二级文档
         * [{
         *   id: "",
         *   name: "",
         *   children: [{
         *     id: "",
         *     name: "",
         *   }]
         * }]
         */
        const level1 = ref(); // 一级文档树，children属性就是二级文档
        level1.value = [];

        /**
         * 内容查询
         **/
        const handleQueryContent = (id: number) => {
            console.log("iddddddddddd:"+id);
            axios.get("/doc/find-content/" + id).then((response) => {
                const data = response.data;
                if (data.success) {
                    html.value = data.content;
                } else {
                    message.error(data.message);
                }
            });
        };

        /**
         * 数据查询
         **/
        const handleQuery = () => {
            // 根据ebookId查文档
            axios.get("/doc/all/" + route.query.ebookId).then((response) => {
                const data = response.data;
                if (data.success) {
                    docs.value = data.content;

                    level1.value = [];
                    level1.value = Tool.array2Tree(docs.value, 0);

                    if (Tool.isNotEmpty(level1)) {
                        console.log(level1);
                        defaultSelectedKeys.value = [level1.value[0].id];
                        handleQueryContent(level1.value[0].id);
                        // 初始显示文档信息
                        doc.value = level1.value[0];
                    }
                } else {
                    message.error(data.message);
                }
            });
        };



        onMounted(() => {
            handleQuery();
        });

        return {
            level1,
            html,
            defaultSelectedKeys,
            doc,
        }
    }
});
</script>

