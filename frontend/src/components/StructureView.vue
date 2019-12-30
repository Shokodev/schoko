<template>
    <div v-if="node.children">
        <div
                @click="nodeClicked"
                :style="{'margin-left': `${depth * 20}px`}"
                class="node"
        >
      <span
              v-if="hasChildren"
              class="type"
      >
                {{expanded ? '&#9661;' : '&#9658;'}}
        </span>
        <span
                class="type" v-else
        >
            {{expanded = '&#9671;' }}
        </span>
        <span>{{node.objectName}} <span v-if="hasDescription"> ({{node.description}})</span> </span>
        </div>
        <div v-if="expanded">
            <StructureView
                    v-for="child in node.children"
                    :node="child"
                    :key="child.objectName"
                    :depth="depth + 1"
                    @onClick="(node) => $emit('onClick', node)"
            />
        </div>
    </div>
</template>

<script>
    export default {
        name: 'StructureView',
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
            //If a node gets clicked its gets checked for children and therefore emitted to the parent class
            nodeClicked() {
                this.expanded = !this.expanded;
                if (!this.hasChildren) {
                    this.$emit('onClick', this.node);
                }
            }
        },
        computed: {
            hasChildren() {
                return this.node.children.length > 0;
            },
            hasDescription() {
                return this.node["description"] !== "";
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