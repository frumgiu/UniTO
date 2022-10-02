import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store ({
    state: {
        lastSearchTxt: '',
        lastSelectedMin: new Date().getFullYear(),
        lastSelectedMax: new Date().getFullYear(),
        lastCheckedTag: [],
        pictureInfo: {
            namePicture: "",
            yearPicture: 0,
            regionPicture: "",
            countryPicture: "",
        }
    },
    getters: {
    },
    mutations: {
        changeSearchTxt(state, newValue) {
            localStorage.setItem('lastSearchTxt', newValue);
        },
        changeMinYear(state, newValue) {
            localStorage.setItem('lastSelectedMin', newValue);
        },
        changeMaxYear(state, newValue) {
            localStorage.setItem('lastSelectedMax', newValue);
        },
        changeCheckedTags(state, newValue) {
            localStorage.setItem('lastCheckedTag', newValue);
        },
        changePictureInfo(state, {newName, newYear, newRegion, newCountry}) {
            state.pictureInfo.namePicture = newName;
            state.pictureInfo.yearPicture = newYear;
            state.pictureInfo.regionPicture = newRegion;
            state.pictureInfo.countryPicture = newCountry;
        }
    },
    actions: {
        changePictureInfo(context, {newName, newYear, newRegion, newCountry}) {
            context.commit('changePictureInfo', {newName, newYear, newRegion, newCountry});
        }
    }
})