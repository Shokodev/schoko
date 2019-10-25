

const state = {

    object: {
        objectName: String,
        properties: [],
        subscribedBACnetObject: null

            }
};
const getters = {
    getObjectName: state => state.object.objectName,
    getBACnetObject: state => state.object.subscribedBACnetObject

};

/*export const actions = {
    readObjectName({commit}, objectName) {
    }

};*/

export const mutations = {
    setObjectName: (state, objectName) => (state.object.objectName = objectName),
    setProperties: (state, properties) => (state.object.properties = properties),
    setBACnetObject: (state, newBACnetObject) => (state.object.subscribedBACnetObject = newBACnetObject),

};

export default{
    state,
    // actions,
    mutations,
    getters
};