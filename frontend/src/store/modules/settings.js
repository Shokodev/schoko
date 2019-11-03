import axios from 'axios';

const state = {
    settings: {},
    syncSettings: null
};
export const actions = {
    // Using axios for the REST communication. The new settings are being send to the backend.
    // Waiting on a response from the server for committing the data to the store and proceeding the application
    // Maybe add a timeout if the server is down
    // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
    // @version 1.0
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
    // Read the settings from the server and ad them to the store
    // @author Vogt Andreas,Daniel Reiter, Rafael Grimm
    // @version 1.0
    async readSettings({commit}) {
        const response = await axios.get(
            "http://localhost:8098/settings"
        );
        commit('backendSettings', response.data);
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
