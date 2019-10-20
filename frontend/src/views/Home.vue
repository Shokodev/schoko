<template>
    <div>
        <div>
            <h1 class="subtitle is 3">
                Home
            </h1>
            <span>
              <strong id="postsButton" class="button is-fullwidth level-left " v-on:click="goBack">{{posts.elementName}} ({{(posts.elementDescription)}})</strong>
            </span>
        </div>
        <div class="container is-fluid">
            <span v-if="posts.length!==0">
                <div id="childButton" class="button is-fullwidth level-left" v-on:click="goIn(child)" :key="child.elementName" v-for="child in posts.children">
                    {{child.elementName}} ({{child.elementDescription}})
                </div>
            </span>
            <modal v-if="isModalVisible" @close="isModalVisible = false" :node="root">

            </modal>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';
    import modal from "../components/modal"
    //TODO make this work
    export default {
        name: 'structure',
        components:{ modal} ,
        data() {
            return {
                posts: {},
                element: "Anlage",
                parent: "",
                firstElement: true,
                properties: [],
                isModalVisible: false,
                root: [],
            };
          },
        mounted() {
            this.loadJSON();
        },
        methods: {
            // This Function get the Hierarchy from Backend only the need one
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            loadJSON: function() {
                axios.get("http://localhost:8098/structure/" + this.element)
                    .then(response => {
                        this.posts = response.data;
                        this.parent = this.posts["elementName"];
                        this.firstElement = false;

                    })
            },
            // This Function Go Back to the last Element
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            goBack: function () {
                 this.element = this.element.replace("'"+ this.parent,"");
                this.loadJSON();
            },
            // This Function Go to the next deeper Element
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            goIn: function (child) {
                console.log(child)
                if (this.isBACnetObject(child) === false ) {
                    console.log(child);
                    if(this.firstElement === false) {
                    this.element = this.element.concat("'"+ child["elementName"]);
                    console.log(this.element)
                    this.loadJSON();
                }
            }
                }
            ,
            // This Function checked to a Datapoint or Structer Element
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            isBACnetObject: function (child){
                console.log(child["elementType"]);
                if(child["elementType"] !== "Structure Element") {
                    console.log("reading BACnet Object");
                    //this.isModalVisible = true;
                    //axios.get("http://localhost:8098/Datapoint/" + "B01'GA01'L01'TEx")
                     //   .then(response => {
                     //      this.root = response.data;
                     //   });


                    return true;
                }else
                    return false;
            }



        }

    };
</script>

<style scoped>
#childButton{margin: 0.2em; background: #FCF9F9;}
#postsButton{background: #EFEAEA;}

</style>