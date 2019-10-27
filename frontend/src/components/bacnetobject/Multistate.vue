<template>
    <span>
        <div>
                <span>
                <span  v-for="prop in node" :key="prop.propertyIdentifier" >
                <div class="box" v-if="prop.propertyIdentifier==='Description'" >
                    <span class="has-text-weight-bold">Beschreibung:</span>
                   {{prop.value}}
                </div>
                </span>
                </span>
        </div>
        <div>
        <span class="box">
            <span class="level">
                <span class="level-left" v-for="prop in node" :key="prop.propertyIdentifier" >
                <span v-if="prop.propertyIdentifier==='Present value'">
                    <span class="has-text-weight-bold">Aktueller Wert:</span>
                  {{getStateText[prop.value -1]}}
                </span>
                </span>
                <span class="level-right">
              <span class="select">
                <select v-model="writeValue">
                    <option v-for="index in this.getStateText" v-bind:key="index.id">{{index}}</option>
                </select>
              </span>
                <button class="button is-primary" v-on:click="setWriteValue()">
                     Senden
                 </button>
                </span>
            </span>
        </span>
        </div>
        <div>
                <span>
                <span  v-for="prop in node" :key="prop.propertyIdentifier" >
                <div class="box" v-if="prop.propertyIdentifier==='Out of service'">
                    <span class="has-text-weight-bold">Ausser Betrieb:</span>
                    {{prop.value}}
                </div>
                </span>
                </span>
        </div>
        <div>
                <span>
                <span v-for="prop in node" :key="prop.propertyIdentifier" >
                <div class="box" v-if="prop.propertyIdentifier==='Object name'">
                    <span class="has-text-weight-bold">Objekt Name:</span>
                    {{prop.value}}
                </div>
                </span>
                </span>
        </div>
    </span>
</template>

<script>
    import { mapMutations , mapGetters} from 'vuex'
    export default {
        name: "Multistate",
        props: {
            node: {}
        },
        data(){
            return{
                options:{},
                objectNameValue: "",
                presentValueValue: "",
                objectTypeValue:"",
                descriptionValue:"",
                outOfServiceValue:"",
                writeValue:"",
                myObject: null,
                numberOfStatesValue:"",
                stateTextValue:[],
            };
        },
        mounted(){
            this.myObject = this.getBACnetObject;
            this.dropdownValue();


        },
        computed:{
            ...mapGetters([
                'getBACnetObject','getStateText'
            ]),




        },

        methods:{
            ...mapMutations([
                'SetBACnetProperty','setStateText'
            ]),



            dropdownValue: function () {
                this.numberOfStatesValue= this.searchPropertyIdentifierValue("Number of states");
                var node= this.searchPropertyIdentifierValue("State text");
                var nodeReplaced=node.replace(/\[|\]|\s/g,"");
                this.setStateText(nodeReplaced.split(','))
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
                let index= (this.getStateText.indexOf(this.writeValue))+1;
                let bacnetProperty = {
                    propertyIdentifier: "Present value",
                    value: index
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
        padding: 1em;
        margin: 0.1em;

    }
    .select {
        display: inline-block;
        max-width: 100%;
        position: relative;
        vertical-align: top;
    }



</style>