const state={
    events:[],
    connected: false,
    stompClient: null,
    hasNewAlarm: false,
};

const getters={
    getEvents: state => state.events,
    getIsConnectedEvents: state => state.connected,
    getStompClient: state => state.stompClient,
    getHasNewAlarm: state => state.hasNewAlarm,
};
// If a new alarm gets recieved the alarmlist gets updated
// @author Vogt Andreas,Daniel Reiter, Rafael Grimm
// @version 1.0
export const actions = {
    newEvent({commit}, newAlarm) {
        commit('alarmHasUpdated', newAlarm);
    }
};
const mutations={
    newEventList: (state, eventList) => (state.events=eventList),
    isConnectedEvents: (state, connected) => (state.connected=connected),
    newStompClient: (state, stompClient) => (state.stompClient=stompClient),
    alarmHasUpdated: (state, newAlarm) => (state.hasNewAlarm=newAlarm),
};
export default{
    state,
    getters,
    mutations,
    actions
};