<template>
    <div class="container">
        <div class="columns" style="margin-top: 150px">
            <div class="column is-half is-offset-one-quarter">
                <div class="card">
                    <header class="card-header">
                        <p class="card-header-title">
                            Login
                        </p>
                    </header>
                    <section class="card-content" style="padding: 50px">
                        <b-field label="Username">
                            <b-input v-model="form.username"></b-input>
                        </b-field>

                        <b-field label="Password">
                            <b-input v-model="form.password" type="password"></b-input>
                        </b-field>

                        <div style="padding-left: 40px; padding-right: 40px;">
                            <b-button @click="submit" type="is-success" style="margin-top: 35px" expanded>
                                Login
                            </b-button>
                        </div>

                        <div class="has-text-centered" style="margin-top: 30px">
                            <router-link to="/register">
                                <small class="is-italic" style="text-decoration: underline;">
                                    Don't have an account?
                                </small>
                            </router-link>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "Login",
    data: function() {
        return {
            form: {
                username: null,
                password: null
            }
        }
    },
    mounted: function() {
        let user = sessionStorage.getItem('auth-user');
        if(user) {
            this.$router.push('/projects');
        }
    },
    methods: {
        submit: function() {

            axios.post('/auth/users/login', {
                username: this.form.username,
                password: this.form.password
            }).then((response) => {
                sessionStorage.setItem('auth-token', response.data.accessToken);
                sessionStorage.setItem('auth-user', JSON.stringify(response.data));
                this.$router.push('/projects');
            }).catch((error) => {

            });
        }
    }
}
</script>

<style scoped>

</style>