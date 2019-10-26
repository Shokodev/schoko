

const state = {
        objectName: String,
        subscribedBACnetObject: [],
        objectNameValue: "",
        bacnetProperty: {
            propertyIdentifier: String,
            value: Number
        }
};
const getters = {
    getObjectName: state => state.objectName,
    getBACnetObject: state => state.subscribedBACnetObject,
    getBACnetProperty: state => state.bacnetProperty,

};

const actions = {

};

export const mutations = {
    setObjectName: (state, objectName) => (state.objectName = objectName),
    setBACnetObject: (state, newBACnetObject) => (state.subscribedBACnetObject = newBACnetObject),
    SetBACnetProperty: (state, newBACnetProperty) => (state.bacnetProperty = newBACnetProperty),



};

export default{
    state,
    actions,
    mutations,
    getters
};