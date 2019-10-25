<template>
    <span>
        <span class="level box">
                <span class="level-left">
                <span>Aktueller Wert: {{this.presentValueValue}}</span>
                </span>
                <span class="level-right">
              <span class="select">
                <select>
                    <option>{{this.inactiveValue}}</option>
                    <option>{{this.activeValue}}</option>
                </select>
              </span>
                <a class="button is-primary">
                     Senden
                 </a>
                </span>
        </span>
        <div class="box">
                <span>
                    Polarität:
                </span>
                <span>
                    {{this.polarityValue}}
                </span>
        </div>
        <div class="box">
                <span>
                    Beschreibung:
                </span>
                <span>
                    {{this.descriptionValue}}
                </span>
        </div>
        <div class="box">
                <span>
                    Ausser Betrieb:
                </span>
                <span>
                    {{this.outOfServiceValue}}
                </span>
        </div>
        <div class="box">
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

    export default {
        name: "BinaryOutput",
        props: {
            node: null
        },
        data(){

            return{
                inactiveValue:"",
                activeValue:"",
                objectNameValue:"",
                presentValueValue:"",
                objectTypeValue:"",
                descriptionValue:"",
                outOfServiceValue:"",
                polarityValue:"",
            };
        },
        mounted(){

                this.presentValue(),
                this.outOfService(),
                this.description(),
                this.objectName()

        },

        methods:{

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
                for (let i = 0; i < this.node.length; i++) {
                    if (this.node[i]["propertyIdentifier"] === (search)) {
                        return this.node[i].value;
                    } else {
                        console.log("Not Found")
                    }
                }
            },

        },


    }
</script>

<style scoped>

</style>