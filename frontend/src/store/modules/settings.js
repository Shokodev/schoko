import axios from 'axios';

const state = {
    settings: {},
    syncSettings: null
};

export const actions = {
    async newSettings({commit}, settings) {
        commit('synchroniseSettings', true);
        axios.post(
            "http://localhost:8098/settings", settings
                )
            .then( res => {
                commit('setSettings', res.data);
                commit('synchroniseSettings', false);
            });

    },
    async readSettings({commit}) {
        const response = await axios.get(
            "http://localhost:8098/settings"
        );
        commit('backendSettings', response.data);
        console.log(response.data)
    }
};
export const mutations = {
    setSettings: (state, settings) => (state.settings = settings),
    backendSettings: (state, settings) => (state.settings = settings),
    synchroniseSettings: (state, isSyncing) => (state.syncSettings = isSyncing),
};

const getters = {
  getSettings: state => state.settings,
  getSiteName: state => state.settings.siteName,
  getSyncSettings: state => state.syncSettings,
};

export default {
    actions,
    mutations,
    getters,
    state
}
