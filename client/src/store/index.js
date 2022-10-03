import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store ({
    state: {
        lastSearchTxt: "",
        filterInfo: {
            lastSelectedMin: new Date().getFullYear() - 3,
            lastSelectedMax: new Date().getFullYear(),
            lastCheckedTag: []
        },
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
            //localStorage.setItem('lastSearchTxt', newValue);
            state.lastSearchTxt = newValue;
        },
        changeFilterInfo(state, {newMinYear, newMaxYear, newTags}) {
            state.filterInfo.lastSelectedMin = newMinYear;
            state.filterInfo.lastSelectedMax = newMaxYear;
            state.filterInfo.lastCheckedTag = newTags;
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
        },
        changeFilterInfo(context, {newMinYear, newMaxYear, newTags}) {
            context.commit('changeFilterInfo', {newMinYear, newMaxYear, newTags});
        },
        changeSearchTxt(context, newValue) {
            context.commit('changeSearchTxt', newValue);
        }
    }
})