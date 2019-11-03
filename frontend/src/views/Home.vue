<template>
    <div>
        <div>
            <h1 class="subtitle is 3">
                Home
            </h1>
            <div class="posts">
            <span>
                <span id="postsButton" class="button is-fullwidth level-left " v-on:click="goBack">{{posts.elementName}} ({{(posts.elementDescription)}})</span>
            </span>
            </div>
        </div>
        <div class="container is-fluid">
            <div class="child">
            <span v-if="posts.length!==0">
                <span id="childButton" class="button is-fullwidth level-left" v-on:click="goIn(child)" :key="child.elementName" v-for="child in posts.children">
                    {{child.elementName}}<span id="description" v-if="child.elementDescription!==''">({{child.elementDescription}})</span>
                </span>
            </span>
            </div>
            <BACnetObjectModal v-if="isModalVisible" @close="isModalVisible = false" >
            </BACnetObjectModal>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';
    import BACnetObjectModal from "../components/BACnetObjectModal"
    import { mapActions , mapGetters} from "vuex"

    export default {
        name: 'structure',
        components:{ BACnetObjectModal} ,
        data() {
            return {
                posts: {},
                element: "",
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
                loading: 'pending'
            };
          },
        mounted() {
            this.loadSite();
            this.loadJSON();
        },
        methods: {
            ...mapActions(["readObjectName"]),

            loadSite: function(){
                this.element = this.getSiteName;
            },
            // This Function gets the needed hierarchy element from the server
            // Add this method the the vuex store
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            loadJSON: function() {
                {
                axios.get("http://localhost:8098/structure/" + this.element)
                    .then(response => {
                        this.posts = response.data;
                        this.parent = this.posts["elementName"];
                        this.firstElement = false;
                    })}
            },
            // This Functions goes one element back
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            goBack: function () {
                 this.element = this.element.replace("'"+ this.parent,"");
                this.loadJSON();
            },
            // This Function goes one element deeper
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            goIn: function (child) {
                if (this.isBACnetObject(child) === false ) {
                    if(this.firstElement === false) {
                    this.element = this.element.concat("'"+ child["elementName"]);
                    this.loadJSON();
                        }
                    }
                }
            ,
            // This Function checkes if the element is e Structure element or a BACnet object
            // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
            // @version 1.0
            isBACnetObject: function (child){
                if(child["elementType"] !== "Structure Element") {
                    var name= this.element + "'" + child["elementName"];
                    var re = new RegExp((this.getSiteName + "'"), "i");
                    this.$store.commit('setObjectName', name.replace(re,""));
                    this.$store.commit('setObjectType', (child["elementType"]));
                    this.isModalVisible= true;
                }else
                    return false;
            }
        }, computed: {
            ...mapGetters([
                'getStompClient','getSiteName',
            ]),
        }
    };
</script>

<style scoped>
#childButton{margin: 0.2em; background: #FCF9F9;}
#postsButton{margin: 0.2em; background: #EFEAEA;}
#description{padding-left: 0.5em}
    .child{
        padding-right: 1em;
    }
    .posts{
        padding-right: 1em;
    }
</style>