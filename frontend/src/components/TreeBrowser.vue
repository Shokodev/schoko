<template>
    <div>
        <div
                @click="nodeClicked"
                :style="{'margin-left': `${depth * 20}px`}"
                class="node"
        >
      <span
              v-if="hasChildren"
              class="type"
      >
                {{expanded ? '&#9660;' : '&#9658;'}}
        </span>
        <span
                class="type" v-else
        >
            {{expanded = '&#9671;' }}
        </span>
        <span
                :style="getStyle(node)">{{node.elementName}} <span v-if="hasDescription"> ({{node.elementDescription}})</span> </span>
        </div>
        <div v-if="expanded">
            <TreeBrowser
                    v-for="child in node.children"
                    :node="child"
                    :key="child.elementName"
                    :depth="depth + 1"
                    @onClick="(node) => $emit('onClick', node)"
            />
        </div>
    </div>
</template>

<script>
    export default {
        name: 'TreeBrowser',
        props: {
            node: Object,
            depth: {
                type: Number,
                default: 0,
            }
        },
        data() {
            return {
                expanded: false
            }
        },
        methods: {
            nodeClicked() {
                this.expanded = !this.expanded;
                if (!this.hasChildren) {
                    this.$emit('onClick', this.node);
                }
            },
            getStyle(node) {
                if(typeof this.node.children != "undefined") {
                    let color = 'black';
                    if (!node.children) {
                        color = 'red';
                    }
                    return {
                        color,
                    }
                }
            }
        },
        computed: {
            hasChildren() {
                return this.node.children.length > 0;
            },
            hasDescription() {
                return this.node["elementDescription"] !== "";
            }
        }

            }
</script>

<style scoped>
    .node {

        font-size: 24px;
    }
    .type {
        margin-right: 10px;
    }
</style>