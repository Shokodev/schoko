const state = {
    events: []

};
const getters = {
    getEvents: state => state.events,

};

export const actions = {

};

export const mutations = {
    setEventList: (state, eventList) => (state.events = eventList),

};

export default{
    state,
    actions,
    mutations,
    getters
};