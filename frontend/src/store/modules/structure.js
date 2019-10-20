import axios from 'axios';

const state = {
    structure: {}
};
const getters = {
    getHierarchy: state => state.structure
};
export const actions = {
    async completeHierarchy({commit}) {
        const response = await axios.get(
            "http://localhost:8098/hierarchy"
        );
        commit('setHierarchy', response.data)
    }
};
export const mutations = {
    setHierarchy: (state, structure) => (state.structure = structure)
};
export default{
    state,
    actions,
    mutations,
    getters
};