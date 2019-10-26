const state={

    events:[]

};

const getters={

    getEvents: state => state.events
};

const mutations={

    newEventList: (state, eventList) => (state.events=eventList)
};

export default{
    state,
    getters,
    mutations
};