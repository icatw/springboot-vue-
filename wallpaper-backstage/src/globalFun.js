import Vue from 'vue'
import store from './store'
Vue.mixin({
    methods: {
        hasAuth(perm) {
            const authority = store.state.userInfo.permissionList;
            if (authority.includes("**:**:**")){
                return true
            }
            return authority.indexOf(perm) > -1
        }
    }
})
