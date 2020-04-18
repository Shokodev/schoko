<template>
    <div v-if="getHierarchy">
        <h1 class="subtitle is 3">
            BACnet Struktur
        </h1>
        <StructureView
                :node="getHierarchy"
        />
    </div>
</template>
<script>
    import StructureView from '../components/StructureView.vue'
    import { mapGetters, mapActions} from "vuex"

    export default {
        name: 'structure',
        methods: mapActions(["completeHierarchy"])
        ,
        computed: mapGetters(["getHierarchy"])
        ,
        // Call the method for creating the complete hierarchy structure view
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        created()  {
            this.completeHierarchy();
        },
            components: {
                StructureView
        },


    }

</script>

<style scoped>

</style>