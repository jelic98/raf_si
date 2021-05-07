<template>
    <div>
        <Navbar></Navbar>

        <div class="has-text-centered" style="margin-top: 60px">
            <b-field>
                <b-button @click="modal_open = true" type="is-success">
                    <span class="icon"><i class="fas fa-plus"></i></span> <span>Add Model</span>
                </b-button>
            </b-field>
        </div>

        <div v-for="model in models" :key="model._id.name"  class="columns" style="margin-left: 200px; margin-right: 200px; margin-top: 30px">
            <div class="column" style="margin: 0 30px;">

                <div class="card">
                    <header class="card-header">
                        <p class="card-header-title">
                            {{ model._id.name }}
                        </p>
                        <router-link class="button" :to="`/projects/${project_name}/models/${model._id.name}/${model.type}`"
                                  type="is-light"
                                  style="margin-top: 5px; margin-right: 5px; margin-bottom: 5px;">
                            <span class="icon"><i class="fas fa-door-open"></i></span>
                        </router-link>
                    </header>

                    <section class="card-content" style="padding: 50px">
                        <p>
                            Type: {{ model.type }}
                        </p>
                    </section>
                </div>
            </div>
        </div>

        <b-modal :active.sync="modal_open" @close="closeModal">
            <div class="modal-card" style="width: auto">
                <header class="modal-card-head">
                    <p class="modal-card-title">New Model</p>
                </header>

                <section class="modal-card-body">
                    <b-field label="Model Name">
                        <b-input v-model="form.name">
                        </b-input>
                    </b-field>

                    <b-field label="Model Type" style="margin-top: 30px">
                        <b-radio-button v-model="form.type"
                                        native-value="requirements" expanded>
                            <span>Requirements</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.type"
                                        native-value="class" expanded>
                            <span>Class</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.type"
                                        native-value="functional" expanded>
                            <span>Functional</span>
                        </b-radio-button>
                    </b-field>
                </section>

                <footer class="modal-card-foot">
                    <b-button type="is-success" @click="saveModel">
                        Save
                    </b-button>
                </footer>
            </div>
        </b-modal>
    </div>
</template>

<script>
import Navbar from "./Navbar";
export default {
    name: "Project",
    components: {Navbar},
    props: {
        project_name: {
            required: true,
            type: String
        }
    },
    data: function() {
        return {
            modal_open: false,
            user: null,
            form: {
                name: null,
                type: null
            },
            models: []
        }
    },
    mounted: function() {
        this.user = JSON.parse(sessionStorage.getItem('auth-user'));

        this.load();
    },
    methods: {
        load: function () {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            axios.get('/core/models/all', {
                params: {
                    project: this.project_name
                }
            }).then((response) => {
                if (response.data) {
                    this.models = response.data;
                } else {
                    this.$router.push('/projects');
                }
            }).catch((error) => {});
        },
        saveModel: function () {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            let body = new FormData();
            body.append('name', this.form.name);
            body.append('project', this.project_name);
            body.append('type', this.form.type);

            axios({
                method: "post",
                url: "/core/models",
                data: body,
                headers: { "Content-Type": "multipart/form-data" },
            }).then((response) => {
                this.projects.push(response.data.project);
                this.closeModal();
            }).catch((error) => {
                this.closeModal();
            });

        },
        closeModal() {
            this.form = {
                name: null,
                type: null
            };

            this.modal_open = false;
        },
        toString(array) {
            let string = '';
            return string.slice(0, string.length - 2);
        }
    }
}
</script>

<style>

</style>
