<template>
    <span>
        <div>
                <span>
                <span  v-for="prop in node" :key="prop.propertyIdentifier" >
                <div class="box" v-if="prop.propertyIdentifier==='Description'">
                   Beschreibung: {{prop.value}}
                </div>
                </span>
                </span>
        </div>
        <div>
        <span class="box">
                <span v-for="prop in node" :key="prop.propertyIdentifier" >
                <div v-if="prop.propertyIdentifier==='Present value'">
                  Aktueller Wert:  {{prop.value}}{{getBACnetUnit}}
                </div>
                </span>
                <span >
                <input v-model="writeValue"  class=" input" type="text">
                <button class=" button is-primary" v-on:click="setWriteValue()">
                     Senden
                 </button>
                </span>
        </span>
        </div>
        <div>
                <span>
                <span  v-for="prop in node" :key="prop.propertyIdentifier" >
                <div class="box" v-if="prop.propertyIdentifier==='Out of service'">
                   Ausser Betrieb: {{prop.value}}
                </div>
                </span>
                </span>
        </div>
        <div>
                <span>
                <span v-for="prop in node" :key="prop.propertyIdentifier" >
                <div class="box" v-if="prop.propertyIdentifier==='Object name'">
                   Objekt Name: {{prop.value}}
                </div>
                </span>
                </span>
        </div>
    </span>
</template>

<script>
    import { mapMutations , mapGetters} from 'vuex'
    export default {
        name: "Analog",
        props: {
            node: {}
        },
        data(){
            return{
                objectNameValue: "",
                presentValueValue: "",
                objectTypeValue:"",
                descriptionValue:"",
                outOfServiceValue:"",
                writeValue:"",
                myObject: null,
            };
        },
        mounted(){
            this.myObject = this.getBACnetObject;
            this.presentValueUnit()


        },
        computed:{
            ...mapGetters([
                'getBACnetObject','getBACnetUnit'
            ]),




        },

        methods:{
            ...mapMutations([
                'SetBACnetProperty','setBACnetUnit'
            ]),



            presentValueUnit: function () {
                this.setBACnetUnit(this.searchPropertyIdentifierValue("Units"));
                           },


            searchPropertyIdentifierValue: function (search) {
                for (let i = 0; i < this.node.length; i++) {
                    if (this.node[i]["propertyIdentifier"] === (search)) {
                        return this.node[i].value;
                    } else {
                        console.log("Not Found")
                    }
                }
            },
            setWriteValue: function () {
                let bacnetProperty = {
                    propertyIdentifier: "Present value",
                    value: this.writeValue
                };
                this.SetBACnetProperty(bacnetProperty);
                this.$emit('event', this.getBACnetProperty)
            },
        },
    }
</script>

<style scoped>

    .box {
        background-color: white;
        border-radius: 6px;
        box-shadow: 0 2px 3px rgba(10, 10, 10, 0.1), 0 0 0 1px rgba(10, 10, 10, 0.1);
        color: #4a4a4a;
        display: block;
        padding: 1rem;
        margin: 0.1em;

    }
    .input {
        box-shadow: inset 0 1px 2px rgba(10, 10, 10, 0.1);
        max-width: 20%;
        width: 15%;
    }



</style>