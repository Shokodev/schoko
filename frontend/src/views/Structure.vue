<template>
    <div>
        <h1 class="title">
            BACnet Struktur
        </h1>
        <TreeBrowser
                :node="root"
        />
    </div>
</template>

<script>
    import TreeBrowser from '../components/TreeBrowser.vue'
    import axios from 'axios'
    //TODO make this work
    export default {
        name: 'structure',
        data() {
            return {
                root :{}
            }
        },
        // This Function get the Hierarchy from Backend
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        mounted() {
            axios.get("http://localhost:8098/hierarchy")
                .then(response => {this.root=response.data});
            console.log(this.root)

        },
        components: {
            TreeBrowser

        }
    }

</script>

<style scoped>

</style>