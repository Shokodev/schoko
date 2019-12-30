<template>
    <div v-if="getDeviceStructure">
        <h1 class="subtitle is 3">
            BACnet Gerätesicht
        </h1>
        <StructureView
                :node="getDeviceStructure"
        />
    </div>
</template>

<script>
    import StructureView from '../components/StructureView.vue'
    import { mapGetters, mapActions} from "vuex"

    export default {
        name: "Devices",
        methods: mapActions(["deviceStructure"])
        ,
        computed: mapGetters(["getDeviceStructure"])
        ,

        created()  {
            this.deviceStructure();
        },
        components: {
            StructureView
        },
    }
</script>

<style scoped>

</style>