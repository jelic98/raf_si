<template>
    <div class="container">
        <div class="columns" style="margin-top: 100px">
            <div class="column is-half is-offset-one-quarter">
                <div class="card">
                    <header class="card-header">
                        <p class="card-header-title">
                            Register
                        </p>
                    </header>

                    <section class="card-content" style="padding: 50px">
                        <b-field label="Username">
                            <b-input v-model="form.username"></b-input>
                        </b-field>

                        <b-field label="Email">
                            <b-input v-model="form.email" type="email" ref="err_email"></b-input>
                        </b-field>

                        <b-field label="Password">
                            <b-input v-model="form.password" ref="err_pass" type="password" minlength="6"></b-input>
                        </b-field>

                        <b-field label="Register as:" style="margin-top: 30px">
                            <b-radio-button v-model="form.role"
                                            native-value="admin" expanded>
                                <span>Admin</span>
                            </b-radio-button>

                            <b-radio-button v-model="form.role"
                                            native-value="engineer" expanded>
                                <span>Engineer</span>
                            </b-radio-button>

                            <b-radio-button v-model="form.role"
                                            native-value="project_manager" expanded>
                                <span>Project Manager</span>
                            </b-radio-button>
                        </b-field>

                        <div style="padding-left: 40px; padding-right: 40px;">
                            <b-button @click="submit" type="is-success" style="margin-top: 35px" expanded>
                                Register
                            </b-button>
                        </div>

                        <div class="has-text-centered" style="margin-top: 30px">
                            <router-link to="/login">
                                <small class="is-italic" style="text-decoration: underline;">
                                    Already have an account?
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
    name: "Register",
    data: function() {
        return {
            form: {
                username: null,
                email: null,
                password: null,
                role: 'user'
                
            },
            has_err: false
        }
    },
    methods: {
        submit: function() {

            if (this.$refs.err_email.checkHtml5Validity()==false || this.$refs.err_pass.checkHtml5Validity()==false){
                this.$buefy.toast.open({
                    duration: 5000,
                    message: `Invalid input`,
                    type: 'is-danger'
                })
            }
            else{
				let body = new FormData();
				body.append('username', this.form.username);
				body.append('email', this.form.email);
				body.append('password', this.form.password);
				body.append('role', this.form.role);
				axios({
					method: "post",
					url: "/auth/users/",
					data: body,
					headers: { "Content-Type": "multipart/form-data" },
				})
            .then((response) => {
                this.$router.push('/login');
            }).catch((error) => {
                
                this.$buefy.toast.open({
                    duration: 5000,
                    message: `Username already exists`,
                    type: 'is-danger'
                })
            
            });
                
            }
            

            
        }
    }
}
</script>

<style scoped>

</style>
