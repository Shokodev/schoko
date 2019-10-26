

const state = {
        objectName: String,
        objectType: String,
        subscribedBACnetObject: [],
        objectNameValue: "",
        bacnetProperty: {
            propertyIdentifier: String,
            value: Number
        }
};
const getters = {
    getObjectName: state => state.objectName,
    getObjectType: state => state.objectType,
    getBACnetObject: state => state.subscribedBACnetObject,
    getBACnetProperty: state => state.bacnetProperty,

};

const actions = {

};

export const mutations = {
    setObjectName: (state, objectName) => (state.objectName = objectName),
    setObjectType: (state, objectType) => (state.objectType = objectType),
    setBACnetObject: (state, newBACnetObject) => (state.subscribedBACnetObject = newBACnetObject),
    SetBACnetProperty: (state, newBACnetProperty) => (state.bacnetProperty = newBACnetProperty),



};

export default{
    state,
    actions,
    mutations,
    getters
};