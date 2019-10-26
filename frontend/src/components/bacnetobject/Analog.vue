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
                  Aktueller Wert:  {{prop.value}}
                </div>
                </span>
                <span>
              <span class="select">
                <select v-model="writeValue">
                    <option>{{this.inactiveValue}}</option>
                    <option>{{this.activeValue}}</option>
                </select>
              </span>
                <button class="button is-primary" v-on:click="setWriteValue()">
                     Senden
                 </button>
                </span>
        </span>
        </div>
        <div>
                <span>
                <span v-for="prop in node" :key="prop.propertyIdentifier" >
                <div class="box" v-if="prop.propertyIdentifier==='Polarity'">
                   Polarität: {{prop.value}}
                </div>
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
                inactiveValue:"",
                activeValue:"",
                objectNameValue: "",
                presentValueValue: "",
                objectTypeValue:"",
                descriptionValue:"",
                outOfServiceValue:"",
                polarityValue:"",
                writeValue:"",
                myObject: null,
            };
        },
        mounted(){
            this.myObject = this.getBACnetObject;
            this.presentValue()


        },
        computed:{
            ...mapGetters([
                'getBACnetObject'
            ]),




        },

        methods:{
            ...mapMutations([
                'SetBACnetProperty'
            ]),



            presentValue: function () {
                this.inactiveValue = this.searchPropertyIdentifierValue("Inactive text");
                this.activeValue = this.searchPropertyIdentifierValue("Active text");
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
                console.log(this.getBACnetObject)

                if(this.writeValue=== this.activeValue){
                    this.writeValue=1
                }else{
                    this.writeValue=0
                }
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
    .select {
        display: inline-block;
        max-width: 100%;
        position: relative;
        vertical-align: top;
    }



</style>