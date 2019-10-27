

const state = {
        objectName: String,
        objectType: String,
        subscribedBACnetObject: [],
        objectNameValue: "",
        bacnetProperty: {
            propertyIdentifier: String,
            value: Number
        },
        stateTextValue: []
};
const getters = {
    getObjectName: state => state.objectName,
    getObjectType: state => state.objectType,
    getBACnetObject: state => state.subscribedBACnetObject,
    getBACnetProperty: state => state.bacnetProperty,
    getStateText: state => state.stateTextValue,

};

const actions = {

};
export const mutations = {
    setObjectName: (state, objectName) => (state.objectName = objectName),
    setObjectType: (state, objectType) => (state.objectType = objectType),
    setBACnetObject: (state, newBACnetObject) => (state.subscribedBACnetObject = newBACnetObject),
    SetBACnetProperty: (state, newBACnetProperty) => (state.bacnetProperty = newBACnetProperty),
    setStateText: (state, newStateText) => (state.stateTextValue = newStateText),



};

export default{
    state,
    actions,
    mutations,
    getters
};