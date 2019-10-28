<template>
    <span>
        <div>
                <span>
                <span  v-for="prop in node" :key="prop.propertyIdentifier" >
                <span class="box" v-if="prop.propertyIdentifier==='Description'">
                    <span class="has-text-weight-bold">Beschreibung: </span>
                   {{prop.value}}
                </span>
                </span>
                </span>
        </div>
        <div>
        <span class="box1">
            <span class="level">
                <span class="level-left" v-for="prop in node" :key="prop.propertyIdentifier" >
                    <span class="text" v-if="prop.propertyIdentifier==='Present value'">
                        <span class="has-text-weight-bold">Aktueller Wert:</span>

                        {{prop.value}}{{getBACnetUnit}}
                    </span>
                </span>
                <span class="level-right">
                    <input v-model="writeValue"  class=" input" type="text">
                    <button class=" button is-primary" v-on:click="setWriteValue()">
                     Senden
                    </button>
                </span>
            </span>
        </span>
        </div>
        <div>
                <span>
                <span  v-for="prop in node" :key="prop.propertyIdentifier" >
                <span class="box" v-if="prop.propertyIdentifier==='Low limit'">
                    <span class="has-text-weight-bold">Minimaler Wert:</span>
                    {{prop.value}}{{getBACnetUnit}}
                </span>
                </span>
                </span>
        </div><div>
                <span>
                <span  v-for="prop in node" :key="prop.propertyIdentifier" >
                <span class="box" v-if="prop.propertyIdentifier==='High limit'">
                    <span class="has-text-weight-bold">Maximaler Wert:</span>
                    {{prop.value}}{{getBACnetUnit}}
                </span>
                </span>
                </span>
        </div>
        <div>
                <span>
                <span v-for="prop in node" :key="prop.propertyIdentifier" >
                <span class="box" v-if="prop.propertyIdentifier==='Object name'">
                    <span class="has-text-weight-bold">Objekt Name: </span>
                   {{prop.value}}
                </span>
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

            // This Function save the unit in a value
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            presentValueUnit: function () {
                this.setBACnetUnit(this.searchPropertyIdentifierValue("Units"));
            },
            // This Function search the name and give the value back
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            searchPropertyIdentifierValue: function (search) {
                for (let i = 0; i < this.node.length; i++) {
                    if (this.node[i]["propertyIdentifier"] === (search)) {
                        return this.node[i].value;
                    } else {
                        console.log("wrong")
                    }
                }
            },
            // This Function send the value was overriden
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
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
        border-radius: 10px;
        box-shadow: 0 2px 3px rgba(10, 10, 10, 0.1), 0 0 0 1px rgba(10, 10, 10, 0.1);
        color: #4a4a4a;
        display: block;
        padding: 1rem;
        margin: 0.1em;

    }
    .input {
        box-shadow: inset 0 1px 2px rgba(10, 10, 10, 0.1);
        max-width: 30%;
        width: 30%;
    }
    .text{
        text-align: center;
    }
    .box1 {
        background-color: white;
        border-radius: 10px;
        box-shadow: 0 2px 3px rgba(10, 10, 10, 0.1), 0 0 0 1px rgba(10, 10, 10, 0.1);
        color: #4a4a4a;
        display: block;
        padding-top: 1rem;
        padding-bottom: 1rem;
        padding-right: 1rem;
        margin: 0.1em;

    }

</style>