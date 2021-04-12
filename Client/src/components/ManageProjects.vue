<template>
    <div>
        <Navbar></Navbar>

        <div v-if="user && user.role === 'admin'" class="has-text-centered" style="margin-top: 60px">
            <b-field>
                <b-button @click="modal_open = true" type="is-success">
                    <span class="icon"><i class="fas fa-plus"></i></span> <span>Add Project</span>
                </b-button>
            </b-field>
        </div>

        <div v-for="project in projects" :key="project.name"  class="columns" style="margin-left: 200px; margin-right: 200px; margin-top: 30px">
            <div class="column" style="margin: 0 30px;">

                <router-link v-if="user && user.role === 'user'" :to="`/projects/${slugify(project.name)}`">
                    <div class="card">
                        <header class="card-header">
                            <p class="card-header-title">
                                {{ project.name }}
                            </p>
                        </header>

                        <section class="card-content" style="padding: 50px">
                            <p>
                                Teams: {{ toString(project.teams) }}
                            </p>
                        </section>
                    </div>
                </router-link>

                <div v-else class="card">
                    <header class="card-header">
                        <p class="card-header-title">
                            {{ project.name }}
                        </p>
                        <b-button @click="() => { editing_project_name = project.name; edit_modal_open = true; }"
                                  type="is-light"
                                  style="margin-top: 5px; margin-right: 5px; margin-bottom: 5px;">
                            <span class="icon"><i class="fas fa-edit"></i></span>
                        </b-button>
                        <b-button @click="deleteProject(project.name)"
                                  type="is-light"
                                  style="margin-top: 5px; margin-right: 5px; margin-bottom: 5px;">
                            <span class="icon"><i class="fas fa-trash"></i></span>
                        </b-button>
                    </header>

                    <section class="card-content" style="padding: 50px">
                        
                    </section>
                </div>
            </div>
        </div>

        <b-modal :active.sync="modal_open" @close="closeModal">
            <div class="modal-card" style="width: auto">
                <header class="modal-card-head">
                    <p class="modal-card-title">New Project</p>
                </header>

                <section class="modal-card-body">
                    <b-field label="Project Name">
                    <b-input v-model="form.title"></b-input>
                    </b-field>
                </section>

                <footer class="modal-card-foot">
                    <b-button type="is-success" @click="saveProject">
                        Save
                    </b-button>
                </footer>
            </div>
        </b-modal>

        <b-modal :active.sync="edit_modal_open" @close="closeModal">
            <div class="modal-card" style="width: auto">
                <header class="modal-card-head">
                    <p class="modal-card-title">Edit Project</p>
                </header>

                <section class="modal-card-body">
                    <b-field v-for="team in teams" :key="team.id">
                        <b-checkbox v-model="form.teams" size="is-medium" :native-value="team.name">{{ team.name }}</b-checkbox>
                    </b-field>
                </section>

                <footer class="modal-card-foot">
                    <b-button type="is-success" @click="editProject">
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
    name: "ManageProjects",
    components: {Navbar},
    data: function() {
        return {
            modal_open: false,
            edit_modal_open: false,
            editing_project_name: null,
            user: null,
            form: {
                title: null,
                teams: []
            },
            projects: [
                // {
                //     id: 1,
                //     name: 'Project 1',
                //     teams: [
                //         {
                //             name: 'Team 1',
                //         },
                //         {
                //             name: 'Team 2',
                //         },
                //         {
                //             name: 'Team 3',
                //         }
                //     ],
                //     models: [
                //         {
                //             name: 'Model 1'
                //         },
                //         {
                //             name: 'Model 2'
                //         },
                //         {
                //             name: 'Model 3'
                //         }
                //     ]
                // },
                // {
                //     id: 2,
                //     name: 'Project 2',
                //     teams: [],
                //     models: []
                // },
                // {
                //     id: 3,
                //     name: 'Project 3',
                //     teams: [],
                //     models: []
                // },
                // {
                //     id: 4,
                //     name: 'Project 4',
                //     teams: [],
                //     models: []
                // }
            ],
            teams: [
                // {
                //     id: 1,
                //     name: 'Team 1'
                // },
                // {
                //     id: 2,
                //     name: 'Team 2'
                // },
                // {
                //     id: 3,
                //     name: 'Team 3'
                // },
                // {
                //     id: 4,
                //     name: 'Team 4'
                // },
                // {
                //     id: 5,
                //     name: 'Team 5'
                // }
            ]
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

			axios.get('/auth/projects/all')
			.then((projects) => {
                this.projects   = projects.data;
            }).catch((error) => {

            });
           
            axios.get('/auth/teams/all')
			.then((teams) => {
                this.teams   = teams.data;
            }).catch((error) => {

            });
        },
        saveProject: function () {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

			let body = new FormData();
			body.append('name', this.form.title);
			axios({
					method: "post",
					url: "/auth/projects/",
					data: body,
					headers: { "Content-Type": "multipart/form-data" },
            }).then((response) => {
                this.projects.push(response.data.name);
                this.load();
		this.modal_open = false;
            }).catch((error) => {
            
			});
        },
        editProject: function() {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            axios.put('/auth/projects', {
                teams: this.form.teams
            }).then((response) => {

                this.load();

            }).catch((error) => {

            });
        },
        deleteProject: function(project_name) {
            this.$buefy.dialog.confirm({
                title: 'Are you sure?',
                message: 'Are you sure you would like to delete this project?',
                onConfirm: () => {
                    axios.delete('/auth/projects', {
                        project: project_name
                    }).then((response) => {
                        this.load();
                    }).catch((error) => {

                    });
                }
            });
        },
        closeModal() {
            this.form = {
                name: null
            };

            this.editing_project_name     = null;
            this.edit_modal_open        = false;
            this.modal_open             = false;
        },
        toString(array) {
		
            let string = '';

            array.forEach((element) => {
                string += `${element.name}, `
            });

            return string.slice(0, string.length - 2);
        },
        slugify(string) {
            return string.replace(/[^a-z0-9 -]/g, '') // remove invalid chars
                .replace(/\s+/g, '-') // collapse whitespace and replace by -
                .replace(/-+/g, '-'); // collapse dashes
        }
    }
}
</script>

<style>

</style>
