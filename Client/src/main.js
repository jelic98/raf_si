import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router';
import Vuex from 'vuex';
import Buefy from 'buefy';
import 'buefy/dist/buefy.css'
import routes from './routes';

Vue.config.productionTip = false;

window.axios = require('axios').create({
    baseURL: 'http://127.0.0.1:9000/'
});

import "@fortawesome/fontawesome-free/css/all.css";
import "@fortawesome/fontawesome-free/css/fontawesome.css";

Vue.use(VueRouter);
Vue.use(Vuex);
Vue.use(Buefy);

let router = new VueRouter({
    mode: 'history',
    routes: routes
});

new Vue({
    router,
    render: h => h(App),
}).$mount('#app');
