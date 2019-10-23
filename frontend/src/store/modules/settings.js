import axios from 'axios';

const state = {
    settings: [
        {
        siteName: "Default Site Name",
        port: Number,
        siteDescription: String,
        bacnetSeparator: String,
        }
]
};

export const actions = {
    async newSettings({commit}, settings) {
        const response = axios.post(
            "http://localhost:8098/settings", settings
        );
        commit('setSettings', response.data)
    },
    async readSettings({commit}) {
        const response = await axios.get(
            "http://localhost:8098/settings"
        );
        commit('backendSettings', response.data)
        console.log(response.data)
    }
};
export const mutations = {
    setSettings: (state, settings) => (state.settings = settings),
    backendSettings: (state, settings) => (state.settings = settings)

};

const getters = {
  getSettings: state => state.settings
};

export default {
    actions,
    mutations,
    getters,
    state
}
