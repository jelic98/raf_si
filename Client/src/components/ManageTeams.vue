<template>
    <div>
        <Navbar></Navbar>

        <div v-if="user && user.role === 'admin'" class="has-text-centered" style="margin-top: 60px">
            <b-field>
                <b-button @click="modal_open = true" type="is-success">
                    <span class="icon"><i class="fas fa-plus"></i></span> <span>Add Team</span>
                </b-button>
            </b-field>
        </div>

        <div v-for="team in teams" :key="team.name"  class="columns" style="margin-left: 200px; margin-right: 200px; margin-top: 30px">
            <div class="column" style="margin: 0 30px;">
                <div class="card">
                    <header class="card-header">
                        <p class="card-header-title">
                            {{ team.name }}
                        </p>
                        <b-button @click="() => { editing_team_name = team.name; edit_modal_open = true; }"
                                  type="is-light"
                                  style="margin-top: 5px; margin-right: 5px; margin-bottom: 5px;">
                            <span class="icon"><i class="fas fa-edit"></i></span>
                        </b-button>
                        <b-button @click="deleteTeam(team.name)"
                                  type="is-light"
                                  style="margin-top: 5px; margin-right: 5px; margin-bottom: 5px;">
                            <span class="icon"><i class="fas fa-trash"></i></span>
                        </b-button>
                    </header>

                    <section class="card-content" style="padding: 50px">
                        <p>
                            Users: {{ toString(team.users) }}
                        </p>
                    </section>
                </div>
            </div>
        </div>

        <b-modal :active.sync="modal_open" @close="closeModal">
            <div class="modal-card" style="width: auto">
                <header class="modal-card-head">
                    <p class="modal-card-title">New Team</p>
                </header>

                <section class="modal-card-body">
                    <b-field label="Team Name">
                        <b-input v-model="form.name">
                        </b-input>
                    </b-field>
                </section>

                <footer class="modal-card-foot">
                    <b-button type="is-success" @click="addTeam">
                        Save
                    </b-button>
                </footer>
            </div>
        </b-modal>

        <b-modal :active.sync="edit_modal_open" @close="closeModal">
            <div class="modal-card" style="width: auto">
                <header class="modal-card-head">
                    <p class="modal-card-title">Edit Team</p>
                </header>

                <section class="modal-card-body">
                    <b-field label="Username">
                        <b-input v-model="new_username"></b-input>
                    </b-field>

                    <b-button type="is-success" @click="() => { form.users.push(new_username); new_username = null; }">
                        Add User
                    </b-button>

                    <p style="margin-top: 20px">Users to be added: <b>{{ form.users.join(', ') }}</b></p>
                </section>

                <footer class="modal-card-foot">
                    <b-button type="is-success" @click="editTeam">
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
    name: "ManageTeams",
    components: {Navbar},
    data: function() {
        return {
            modal_open: false,
            edit_modal_open: false,
            editing_team_name: null,
            user: null,
            new_username: null,
            form: {
                name: null,
                users: []
            },
            teams: [
                // {
                //     id: 1,
                //     name: 'Team 1',
                //     users: [
                //         {
                //             username: 'user1'
                //         },
                //         {
                //             username: 'user2'
                //         },
                //         {
                //             username: 'user3'
                //         }
                //     ]
                // },
                // {
                //     id: 2,
                //     name: 'Team 2',
                //     users: [
                //         {
                //             username: 'user1'
                //         },
                //         {
                //             username: 'user2'
                //         },
                //         {
                //             username: 'user3'
                //         }
                //     ]
                // },
                // {
                //     id: 3,
                //     name: 'Team 3',
                //     users: [
                //         {
                //             username: 'user1'
                //         },
                //         {
                //             username: 'user2'
                //         }
                //     ]
                // }
            ]
        }
    },
    mounted: function() {
        this.user = JSON.parse(sessionStorage.getItem('auth-user'));

        this.load();
    },
    methods: {
        load: function() {
            let jwt = sessionStorage.getItem('auth-token');

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            axios.get('/auth/teams').then((response) => {
                this.teams = response.data.teams;
            }).catch((error) => {

            });
        },
        addTeam: function () {
            let jwt = sessionStorage.getItem('auth-token');

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            axios.post('/auth/teams', {
                name: this.form.name
            }).then((response) => {
                this.load();
            }).catch((error) => {

            });
        },
        editTeam: function() {
            let jwt = sessionStorage.getItem('auth-token');

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            axios.put('/auth/teams', {
                team: this.editing_team_name,
                users: this.form.users
            }).then((response) => {
                this.load();
            }).catch((error) => {

            });
        },
        deleteTeam: function(team_name) {
            this.$buefy.dialog.confirm({
                title: 'Are you sure?',
                message: 'Are you sure you would like to delete this team?',
                onConfirm: () => {
                    axios.delete('/auth/teams', {
                        team: team_name
                    }).then((response) => {
                        this.load();
                    }).catch((error) => {

                    });
                }
            });
        },
        closeModal() {
            const $scope = this;

            $scope.form = {
                name: null,
                users: []
            };

            $scope.modal_open           = false;
            $scope.edit_modal_open      = false;
            $scope.editing_team_name    = null;
        },
        toString(users) {
            let string = '';

            users.forEach((user) => {
                string += `${user.username}, `
            });

            return string.slice(0, string.length - 2);
        }
    }
}
</script>

<style scoped>

</style>