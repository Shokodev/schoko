<template>
    <div v-if="getHierarchy">
        <h1 class="subtitle is 3">
            BACnet Struktur
        </h1>
        <TreeBrowser
                :node="getHierarchy"
        />
    </div>
</template>
<script>
    import TreeBrowser from '../components/TreeBrowser.vue'
    import { mapGetters, mapActions} from "vuex"

    //TODO work with vuex instead
    export default {
        name: 'structure',
        methods: mapActions(["completeHierarchy"])
        ,
        computed: mapGetters(["getHierarchy"])
        ,
        created()  {
            this.completeHierarchy();
        },
            components: {
            TreeBrowser
        },
    }

</script>

<style scoped>

</style>