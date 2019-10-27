

const state = {
        objectName: String,
        objectType: String,
        subscribedBACnetObject: [],
        objectNameValue: "",
        bacnetProperty: {
            propertyIdentifier: String,
            value: Number
        },
        stateTextValue: [],
        bacnetUnit: String,
        connected: false,
        polarityValue: String,
};
const getters = {
    getObjectName: state => state.objectName,
    getObjectType: state => state.objectType,
    getBACnetObject: state => state.subscribedBACnetObject,
    getBACnetProperty: state => state.bacnetProperty,
    getStateText: state => state.stateTextValue,
    getBACnetUnit: state => state.bacnetUnit,
    getIsConnected: state => state.connected,
    getPolarityValue: state => state.polarityValue,

};

const actions = {
};
export const mutations = {
    setObjectName: (state, objectName) => (state.objectName = objectName),
    setObjectType: (state, objectType) => (state.objectType = objectType),
    setBACnetObject: (state, newBACnetObject) => (state.subscribedBACnetObject = newBACnetObject),
    SetBACnetProperty: (state, newBACnetProperty) => (state.bacnetProperty = newBACnetProperty),
    setStateText: (state, newStateText) => (state.stateTextValue = newStateText),
    setBACnetUnit: (state, bacnetUnit) => (state.bacnetUnit = bacnetUnit),
    setIsConnected: (state, connected) => (state.connected = connected),
    setPolarityValue: (state, polarityValue) => (state.polarityValue = polarityValue),



};

export default{
    state,
    actions,
    mutations,
    getters
};