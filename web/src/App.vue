<template>
    <a-layout>
        <the-header></the-header>
        <router-view/>
        <the-footer></the-footer>
    </a-layout>
</template>

<script>
import { defineComponent } from 'vue';
import TheHeader from '@/components/TheHeader.vue';
import TheFooter from "@/components/TheFooter.vue";

export default defineComponent({
    components: {
        TheFooter,
        TheHeader,
    }
});

</script>


