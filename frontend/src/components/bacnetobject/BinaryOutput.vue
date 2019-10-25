<template>
<span>

            <div class="columns">
                    <span class="column is-three-fifths">
                    <span>Aktueller Wert: {{this.presentValueValue}}</span>
                    </span>
                    <span class="column is-right">
                  <span class="select">
                    <select v-model="writeValue">
                        <option>{{this.inactiveValue}}</option>
                        <option>{{this.activeValue}}</option>
                    </select>
                  </span>
                    <a class="button is-primary" v-on:click="setWriteValue()">
                         Senden
                     </a>
                    </span>
            </div>
            <div class="column" >
                    <span>
                        Polarität:
                    </span>
                    <span>
                        {{this.polarityValue}}
                    </span>
            </div>
            <div class="column">
                    <span>
                        Beschreibung:
                    </span>
                    <span>
                        {{this.descriptionValue}}
                    </span>
            </div>
            <div class="column">
                    <span>
                        Ausser Betrieb:
                    </span>
                    <span>
                        {{this.outOfServiceValue}}
                    </span>
            </div>
            <div class="column">
                    <span>
                        Objekt Name:
                    </span>
                    <span>
                        {{this.objectNameValue}}
                    </span>
            </div>
</span>


</template>

<script>
import modal from "../modal";

    export default {
        name: "BinaryOutput",
        data(){
            return{
                bacnetObject: null,
                inactiveValue:"",
                activeValue:"",
                objectNameValue:"",
                presentValueValue:"",
                objectTypeValue:"",
                descriptionValue:"",
                outOfServiceValue:"",
                polarityValue:"",
                writeValue:""

            };
        },
        mounted(){
                this.setBacnetObject(),
                this.presentValue(),
                this.outOfService(),
                    this.description(),
                this.objectName()
        },
        methods:{

            setWriteValue: function () {
                if(this.writeValue=== this.activeValue){
                console.log(1);
                    this.writeValue=1

                }else{
                    console.log(0);
                    this.writeValue=0
                }
                var bacnetProperty = (
                    "value: "+ this.writeValue + ", " +
                        "propertyIdentifier: Present value"
                );

                modal.methods.sendValue(JSON.stringify(bacnetProperty));
            },
            setBacnetObject: function () {
               this.bacnetObject= modal.methods.getProperties()
            },
            objectName: function () {
                this.objectNameValue = this.searchPropertyIdentifierValue("Object name")
            },
            presentValue: function () {
                this.presentValueValue = this.searchPropertyIdentifierValue("Present value");
                this.inactiveValue = this.searchPropertyIdentifierValue("Inactive text");
                this.activeValue = this.searchPropertyIdentifierValue("Active text");
                this.polarityValue = this.searchPropertyIdentifierValue("Polarity");
                if(this.presentValueValue==="0"){
                    return this.presentValueValue = this.inactiveValue
                }else
                    return this.presentValueValue = this.activeValue
            },
            description: function () {
                this.descriptionValue = this.searchPropertyIdentifierValue("Description")

            },
            outOfService: function () {
                this.outOfServiceValue = this.searchPropertyIdentifierValue("Out of service")
            },
            searchPropertyIdentifierValue: function (search) {
                for (let i = 0; i < this.bacnetObject.length; i++) {
                    if (this.bacnetObject[i].propertyIdentifier === (search)) {
                        return this.bacnetObject[i].value;
                    } else {
                        console.log("Not Found")
                    }
                }
            }


        }

    }
</script>

<style scoped>

</style>