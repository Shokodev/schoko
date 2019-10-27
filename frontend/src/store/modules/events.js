const state={

    events:[],
    connected: false,
    stompClient: null

};

const getters={

    getEvents: state => state.events,
    getIsConnected: state => state.events,
    getStompClient: state => state.stompClient,
};

const mutations={

    newEventList: (state, eventList) => (state.events=eventList),
    isConnected: (state, connected) => (state.connected=connected),
    newStompClient: (state, stompClient) => (state.stompClient=stompClient),
};

export default{
    state,
    getters,
    mutations
};