<template>
    <div>
        <Navbar></Navbar>

        <div class="container">
            <div class="columns" style="margin-top: 150px">
                <div class="column is-half is-offset-one-quarter">
                    <div class="card">
                        <header class="card-header">
                            <p class="card-header-title">
                                Change Password
                            </p>
                        </header>

                        <section class="card-content" style="padding: 50px">
                            <b-field label="Password">
                                <b-input v-model="form.password" type="password"></b-input>
                            </b-field>

                            <div style="padding-left: 40px; padding-right: 40px;">
                                <b-button @click="changePassword" type="is-success" style="margin-top: 35px" expanded>
                                    Change Password
                                </b-button>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Navbar from "./Navbar";
export default {
    name: "Profile",
    components: {Navbar},
    data: function() {
        return {
            user: {},
            form: {
                password: null
            }
        }
    },
    mounted: function () {
        this.load();
    },
    methods: {
        load: function() {
            let jwt = sessionStorage.getItem('auth-token');

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            axios.get('/auth/user', {
                params: {
                    user: jwt
                }
            }).then((response) => {
                this.user = response.data.user;
            }).catch((error) => {

            });
        },
        changePassword: function() {
            let jwt = sessionStorage.getItem('auth-token');

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            axios.put('/auth/user', {
                user: jwt,
                email: this.user.email,
                username: this.user.username,
                password: this.form.password,
                passwordOld: this.user.password
            }).then((response) => {
                this.load();
            }).catch((error) => {

            });
        }
    }
}
</script>

<style scoped>

</style>