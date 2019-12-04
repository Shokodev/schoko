<template>
    <span>
        <div>
            <span  v-for="prop in node" :key="prop.propertyIdentifier" >
            <span class="box" v-if="prop.propertyIdentifier==='Description'">
                <span class="has-text-weight-bold">Beschreibung:</span>
                {{prop.value}}
            </span>
            </span>
        </div>
        <div >
        <span class="box1">
            <span class="level">
                <span class="level-left">
                    <span v-for="prop in node" :key="prop.propertyIdentifier" >
                    <span v-if="prop.propertyIdentifier==='Present value'">
                        <span  class="has-text-weight-bold">Aktueller Wert:</span>
                       {{getStateText[prop.value]}}
                    </span>
                    </span>
                </span>
                    <span class="level-right">
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
            </span>
        </div>
        <div>
                <span>
                <span v-for="prop in node" :key="prop.propertyIdentifier" >
                <span class="box" v-if="prop.propertyIdentifier==='Polarity'">
                    <span class="has-text-weight-bold">Polarität:</span>
                    {{getPolarityValue}}
                </span>
                </span>
                </span>
        </div>
        <div>
                <span>
                <span  v-for="prop in node" :key="prop.propertyIdentifier" >
                <span class="box" v-if="prop.propertyIdentifier==='Out of service'">
                    <span  class="has-text-weight-bold"> Ausser Betrieb:</span>
                   {{prop.value}}
                </span>
                </span>
                </span>
        </div>
        <div>
                <span>
                <span v-for="prop in node" :key="prop.propertyIdentifier" >
                <div class="box" v-if="prop.propertyIdentifier==='Object name'">
                    <span  class="has-text-weight-bold">Objekt Name: </span>
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
        name: "BinaryOutput",
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
                this.dropdownValue();
                this.isPolarityValue()
        },
        computed:{
            ...mapGetters([
                'getBACnetObject','getStateText','getPolarityValue'
            ]),
        },
        methods:{
        ...mapMutations([
            'SetBACnetProperty','setStateText','setPolarityValue'
        ]),
        // This Function translates the priorityvalue to german
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        isPolarityValue: function(){
            if (this.searchPropertyIdentifierValue("Polarity")==="0"){
                this.setPolarityValue("Normal")
            } else{
                this.setPolarityValue("Invertiert")
            }
        },
        // This Function removes all special charackters and saves it in a array for the dropdown menu
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        dropdownValue: function () {
            this.inactiveValue = this.searchPropertyIdentifierValue("Inactive text");
            this.activeValue = this.searchPropertyIdentifierValue("Active text");
            let node= [this.inactiveValue, this.activeValue];
            this.setStateText(node);
            },
        // This Function searches the name and gives the value back
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        searchPropertyIdentifierValue: function (search) {
            for (let i = 0; i < this.node.length; i++) {
                if (this.node[i]["propertyIdentifier"] === (search)) {
                    return this.node[i].value;
                }
            }
        },
        // This Function sends the new value
        // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
        // @version 1.0
        setWriteValue: function () {
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
    .box1 {
         background-color: white;
        border-radius: 10px;
         box-shadow: 0 2px 3px rgba(10, 10, 10, 0.1), 0 0 0 1px rgba(10, 10, 10, 0.1);
         color: #4a4a4a;
         display: block;
        padding: 1rem;
         margin: 0.1em;

     }
    .box {
        background-color: white;
        border-radius: 10px;
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