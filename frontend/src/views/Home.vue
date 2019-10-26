<template>
    <div>
        <div v-if="this.this.getSiteName">
            <h1 class="subtitle is 3">
                Home
            </h1>
            <span>
              <span id="postsButton" class="button is-fullwidth level-left " v-on:click="goBack">{{posts.elementName}} ({{(posts.elementDescription)}})</span>
            </span>
        </div>
        <div class="container is-fluid">
            <div>

            <span v-if="posts.length!==0">
                <span id="childButton" class="button is-fullwidth level-left" v-on:click="goIn(child)" :key="child.elementName" v-for="child in posts.children">
                    {{child.elementName}}<span id="description" v-if="child.elementDescription!==''">({{child.elementDescription}})</span>
                </span>
            </span>
            </div>
            <modal v-if="isModalVisible" @close="isModalVisible = false" :node="root">

            </modal>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';
    import modal from "../components/modal"
    import { mapActions , mapGetters} from "vuex"


    //TODO make this work
    export default {
        name: 'structure',
        components:{ modal} ,
        data() {
            return {
                posts: {},
                element: this.getSiteName,
                parent: "",
                firstElement: true,
                properties: [],
                isModalVisible: false,
                root: [],
                child: {
                    elementDescription: ""
                },
                color: '#000000',
                size: '1000px',
                loading:true
            };
          },
        mounted() {
           this.loadJSON();
        },
        methods: {

            ...mapActions(["readObjectName"]),

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
                if (this.isBACnetObject(child) === false ) {
                    //console.log(child);
                    if(this.firstElement === false) {
                    this.element = this.element.concat("'"+ child["elementName"]);
                    //console.log(this.element)
                    this.loadJSON();
                }
            }
                }
            ,
            // This Function checked to a Datapoint or Structer Element
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            isBACnetObject: function (child){
                //console.log(child["elementType"]);
                if(child["elementType"] !== "Structure Element") {
                    //console.log("reading BACnet Object");
                    var name= this.element + "'" + child["elementName"];
                    this.$store.commit('setObjectName', name.replace(/Anlage'/i,""));
                    this.isModalVisible= true;
                }else
                    return false;
            }
        }, computed: {
            ...mapGetters([
                'getSiteName'
            ])
        }
    };
</script>

<style scoped>
#childButton{margin: 0.2em; background: #FCF9F9;}
#postsButton{background: #EFEAEA;}
#description{padding-left: 0.5em}
</style>