<template>
    <div>
        <Navbar></Navbar>

        <div style="margin-left: 200px; margin-right: 200px; margin-top: 50px; padding-top:35px">
             <p class="title" syle="margin-bottom: 30px">
                 {{ model_name }}

                 <b-dropdown class="is-pulled-right" @click='loadUsers'>
            <template #trigger>
                <b-button
                    label="Active users"
                    type="is-white"
                    style="border: 1px solid lightgray"
                    icon-left="users"
                    icon-right="caret-down" />
            </template>


            <b-dropdown-item
                v-for="(menu, index) in users"
                :key="index"
                :value="menu" aria-role="listitem">
                <div class="media">
                    <b-icon class="media-left" icon="user"></b-icon>
                    <div class="media-content">
                        <h3>{{menu}}</h3>
                    </div>
                </div>
            </b-dropdown-item>
        </b-dropdown>
             </p>

            <b-tabs>
                <b-tab-item label="Model">
                    <b-field >
                    <!--v-if="user && (user.role === 'admin' || user.role === 'project_manager')-->
                        <b-button @click="() => { this.form.parent_id = null; this.modal_open = true}" type="is-success">
                            <span class="icon"><i class="fas fa-plus"></i></span> <span>Add Root Requirement</span>
                        </b-button>

                       <b-tooltip label="Undo" style="margin-left: auto">
                    <b-button  type='is-light' @click='undo'>
                        <span class="icon"><i class="fas fa-undo-alt"></i></span>
                    </b-button>
                    </b-tooltip>

                    <b-tooltip label="Redo">
                    <b-button style="margin-left: 10px" type='is-light' @click='redo'>
                        <span class="icon"><i class="fas fa-redo-alt"></i></span>
                    </b-button>
                    </b-tooltip>

                    <b-tooltip label="Revision history">
                    <b-button style="margin-left: 10px" type='is-light' @click='historyModal=true'>
                        <span class="icon"><i class="fas fa-clock"></i></span>
                    </b-button>
                    </b-tooltip>

                    <b-tooltip label="Transform to functional model">
                    <b-button style="margin-left: 10px" type='is-light' @click='transform'>
                        <span class="icon"><i class="fas fa-exchange-alt"></i></span>
                    </b-button>
                    </b-tooltip>

                    <b-button style="margin-left: 10px" type='is-primary' @click='saveModel'>Save</b-button>
                    </b-field>


                    <b-table
                        :data="requirements_no_depth">

                        <b-table-column label="#" v-slot="props">
                            {{ props.row.code }}
                        </b-table-column>

                        <b-table-column label="Title" v-slot="props">
                            {{ props.row.title }}
                        </b-table-column>

                        <b-table-column label="Description" v-slot="props">
                            {{ props.row.description }}
                        </b-table-column>

                        <b-table-column label="Type" v-slot="props">
                            {{ props.row.requirement_type }}
                        </b-table-column>

                        <b-table-column label="Priority" v-slot="props">
                            {{ props.row.priority }}
                        </b-table-column>

                        <b-table-column label="Risk" v-slot="props">
                            {{ props.row.risk }}
                        </b-table-column>

                        <b-table-column label="Actor" v-slot="props">
                            {{ props.row.actor_name }}
                        </b-table-column>

                        <b-table-column v-if="user && (user.role === 'admin' || user.role === 'project_manager')" label="Manage" v-slot="props">
                            <b-button @click="() => { parent_requirement = props.row; modal_open = true}" type="is-success" style="margin-right: 5px">
                                <span class="icon"><i class="fas fa-plus"></i></span>
                            </b-button>

                            <b-button @click="openEditModal(props.row)" type="is-warning" style="margin-right: 5px">
                                <span class="icon"><i class="fas fa-edit"></i></span>
                            </b-button>

                            <b-button @click="deleteRequirement(props.row)" type="is-danger">
                                <span class="icon"><i class="fas fa-trash"></i></span>
                            </b-button>
                        </b-table-column>
                    </b-table>
                </b-tab-item>

                <b-tab-item label="Actors">
                    <b-field v-if="user && (user.role === 'admin' || user.role === 'project_manager')">
                        <b-button @click="actor_modal_open = true" type="is-success">
                            <span class="icon"><i class="fas fa-plus"></i></span> <span>Add Actor</span>
                        </b-button>

                        <b-button style="margin-left: auto" type='is-light' @click='undo' :disabled="undo_stack.length < 1">
                            <span class="icon"><i class="fas fa-undo-alt"></i></span>
                        </b-button>

                        <b-button style="margin-left: 10px" type='is-light' @click='redo' :disabled="redo_stack.length < 1">
                            <span class="icon"><i class="fas fa-redo-alt"></i></span>
                        </b-button>

                        <b-button style="margin-left: 10px" type='is-primary' @click="saveModel">Save</b-button>
                    </b-field>

                    <b-table :data="details.actors">
                        <b-table-column label="Name" v-slot="props">
                            {{ props.row.name }}
                        </b-table-column>
                    </b-table>
                </b-tab-item>
            </b-tabs>
        </div>

        <b-modal :active.sync="historyModal" has-modal-card>
            <div class="modal-card">
                <header class="modal-card-head">
                    <p class="modal-card-title">Revision history</p>
                </header>

                <section class="modal-card-body" style="padding: 40px">

                    <b-table :data="revisions">
                        <b-table-column label="id" v-slot="props">
                            {{props.row.id}}
                        </b-table-column>
                        <b-table-column label="Date" v-slot="props">
                            {{props.row.date}}
                        </b-table-column>
                        <b-table-column label="Author" v-slot="props">
                            {{props.row.author}}
                        </b-table-column>

                        <b-table-column label=" " v-slot="props">
                            <b-button @click="history(props.row.id)" class="is-small" type="is-primary">
                                Restore
                            </b-button>
                        </b-table-column>
                    </b-table>
                </section>
            </div>
        </b-modal>

        <b-modal :active.sync="modal_open" @close="closeModal">
            <div class="modal-card" style="width: auto">
                <header class="modal-card-head">
                    <p class="modal-card-title">Add Requirement</p>
                </header>

                <section class="modal-card-body">
                    <b-field label="Title">
                        <b-input v-model="form.title"></b-input>
                    </b-field>

                    <b-field label="Description">
                        <b-input type="textarea" v-model="form.description"></b-input>
                    </b-field>

                    <b-field label="Type" style="margin-top: 30px">
                        <b-radio-button v-model="form.requirement_type"
                                        native-value="functional" expanded>
                            <span>Functional</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.requirement_type"
                                        native-value="non_functional" expanded>
                            <span>Non-functional</span>
                        </b-radio-button>
                    </b-field>

                    <b-field label="Priority" style="margin-top: 30px">
                        <b-radio-button v-model="form.priority"
                                        native-value="1" expanded>
                            <span>1</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.priority"
                                        native-value="2" expanded>
                            <span>2</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.priority"
                                        native-value="3" expanded>
                            <span>3</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.priority"
                                        native-value="4" expanded>
                            <span>4</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.priority"
                                        native-value="5" expanded>
                            <span>5</span>
                        </b-radio-button>
                    </b-field>

                    <b-field label="Risk" style="margin-top: 30px">
                        <b-radio-button v-model="form.risk"
                                        native-value="low" expanded>
                            <span>Low</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.risk"
                                        native-value="medium" expanded>
                            <span>Medium</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.risk"
                                        native-value="high" expanded>
                            <span>High</span>
                        </b-radio-button>
                    </b-field>

                    <b-field label="Actor" style="margin-top: 30px">
                        <b-select v-model="form.actor_name" placeholder="Select an Actor">
                            <option v-for="(actor, key) in details.actors" :key="key" :value="actor.name">
                                {{ actor.name }}
                            </option>
                        </b-select>
                    </b-field>
                </section>

                <footer class="modal-card-foot">
                    <b-button type="is-success" @click="addRequirement">
                        Save
                    </b-button>
                </footer>
            </div>
        </b-modal>

        <b-modal :active.sync="edit_modal_open" @close="closeModal">
            <div class="modal-card" style="width: auto">
                <header class="modal-card-head">
                    <p class="modal-card-title">Edit Requirement</p>
                </header>

                <section class="modal-card-body">
                    <b-field label="Title">
                        <b-input v-model="form.title"></b-input>
                    </b-field>

                    <b-field label="Description">
                        <b-input type="textarea" v-model="form.description"></b-input>
                    </b-field>

                    <b-field label="Type" style="margin-top: 30px">
                        <b-radio-button v-model="form.requirement_type" native-value="functional" expanded>
                            <span>Functional</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.requirement_type" native-value="non_functional" expanded>
                            <span>Non-functional</span>
                        </b-radio-button>
                    </b-field>

                    <b-field label="Priority" style="margin-top: 30px">
                        <b-radio-button v-model="form.priority"
                                        :native-value="1" expanded>
                            <span>1</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.priority"
                                        :native-value="2" expanded>
                            <span>2</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.priority"
                                        :native-value="3" expanded>
                            <span>3</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.priority"
                                        :native-value="4" expanded>
                            <span>4</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.priority"
                                        :native-value="5" expanded>
                            <span>5</span>
                        </b-radio-button>
                    </b-field>

                    <b-field label="Risk" style="margin-top: 30px">
                        <b-radio-button v-model="form.risk"
                                        native-value="low" expanded>
                            <span>Low</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.risk"
                                        native-value="medium" expanded>
                            <span>Medium</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.risk"
                                        native-value="high" expanded>
                            <span>High</span>
                        </b-radio-button>
                    </b-field>

                    <b-field label="Actor" style="margin-top: 30px">
                        <b-select v-model="form.actor_name" placeholder="Select an Actor">
                            <option v-for="(actor, key) in details.actors" :key="key" :value="actor.name">
                                {{ actor.name }}
                            </option>
                        </b-select>
                    </b-field>
                </section>

                <footer class="modal-card-foot">
                    <b-button type="is-success" @click="editRequirement">
                        Save
                    </b-button>
                </footer>
            </div>
        </b-modal>

        <b-modal :active.sync="actor_modal_open" @close="closeModal">
            <div class="modal-card" style="width: auto">
                <header class="modal-card-head">
                    <p class="modal-card-title">Add Actor</p>
                </header>

                <section class="modal-card-body">
                    <b-field label="Title">
                        <b-input v-model="actor_form.name"></b-input>
                    </b-field>
                </section>

                <footer class="modal-card-foot">
                    <b-button type="is-success" @click="addActor">
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
    name: "RequirementsModel",
    components: {Navbar},
    props: {
        project_name: {
            required: true,
            type: String
        },
        model_name: {
            required: true,
            type: String
        }
    },
    data() {
        return {
            revisions: [],
            historyModal: false,
            users: [],
            modal_open: false,
            edit_modal_open: false,
            actor_modal_open: false,
            parent_requirement: null,
            editing_requirement: null,
            form: {
                title: null,
                description: null,
                requirement_type: null,
                priority: null,
                risk: null,
                actor_name: null
            },
            actor_form: {
                name: null
            },
            model: {},
            requirements_no_depth: [],
            details: {
                requirements: [],
                actors: []
            },
            undo_stack: [
                {
                    requirements: [],
                    actors: []
                }
            ],
            redo_stack: [],
            user: null
        };
    },
    mounted: function() {
        this.user = JSON.parse(sessionStorage.getItem('auth-user'));
        this.render('', this.details.requirements);
        this.load();
    },
    methods: {
        undo: function() {
            this.redo_stack.push(this.undo_stack.pop());
            this.details = JSON.parse(JSON.stringify(this.undo_stack[this.undo_stack.length - 1]));

            this.requirements_no_depth = [];
            this.render('', this.details.requirements);
        },
        redo: function() {
            this.details = JSON.parse(JSON.stringify(this.redo_stack[this.redo_stack.length - 1]));
            this.undo_stack.push(this.redo_stack.pop());

            this.requirements_no_depth = [];
            this.render('', this.details.requirements);
        },
        render: function(base, requirements) {
            requirements.forEach((requirement, index) => {
                this.requirements_no_depth.push(requirement);

                if (base == '') {
                    requirement.code = `${index + 1}`;
                    this.render(`${index + 1}`, requirement.children);
                } else {
                    requirement.code = `${base}.${index + 1}`;
                    this.render(`${base}.${index + 1}`, requirement.children);
                }
            });
        },

        history: function(id) {
            //TODO request kojim se podesava verzija, prosledju je joj se parametar id 

            this.load()
        },
        load: function() {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            axios.get('/core/models', {
                params: {
                    project: this.project_name,
                    model: this.model_name
                }
            }).then((response) => {
                if (response.data) {
                    this.name = response.data._id.name;
                    this.details = JSON.parse(response.data.details);
                    this.undo_stack.push(this.details);
                    this.render('', this.details.requirements);
                } else {
                    this.$router.push('/projects');
                }
            }).catch((error) => {});

            //TODO load history 
            axios.get('/', {
               
            }).then((response) => {
                
                this.revisions = response.data
                //saljes mi id, date i autora

            }).catch((error) => {
            });
        },
        addRequirement: function() {
            if (this.parent_requirement) {
                let split = this.parent_requirement.code.split('.');
                let current = null;
                let root_req = null;

                this.details.requirements.forEach((root) => {
                    if (root.code == split[0]) {
                        current     = root;
                        root_req    = root;
                    }
                });

                for (let i = 1; i < split.length; i++) {
                    current.children.forEach((child) => {
                        if (child.code.split('.')[child.code.split('.').length - 1] == split[i]) {
                            current = child;
                        }
                    });
                }

                current.children.push({
                    project_name: this.project_name,
                    model_name: this.model_name,
                    title: this.form.title,
                    description: this.form.description,
                    requirement_type: this.form.requirement_type,
                    priority: this.form.priority,
                    risk: this.form.risk,
                    actor_name: this.form.actor_name,
                    children: []
                });

                this.requirements_no_depth = [];
                this.render('', this.details.requirements);

            } else {
                let requirement = {
                    project_name: this.project_name,
                    model_name: this.model_name,
                    parent_id: null,
                    title: this.form.title,
                    description: this.form.description,
                    requirement_type: this.form.requirement_type,
                    priority: this.form.priority,
                    risk: this.form.risk,
                    actor_name: this.form.actor_name,
                    children: []
                };

                this.requirements_no_depth = [];
                this.details.requirements.push(requirement);
                this.render('', this.details.requirements);
            }

            this.parent_requirement = null;
            this.modal_open = false;
            this.form = {
                parent_id: null,
                title: null,
                description: null,
                requirement_type: null,
                priority: null,
                risk: null,
                actor_name: null
            };

            this.undo_stack.push(JSON.parse(JSON.stringify(this.details)));
            this.redo_stack = [];
        },
        editRequirement: function() {
            let split = this.editing_requirement.code.split('.');
            let current = null;

            this.details.requirements.forEach((root) => {
                if (root.code == split[0]) {
                    current = root;
                }
            });

            for (let i = 1; i <= split.length; i++) {
                current.children.forEach((child) => {
                    if (child.code.split('.')[child.code.split('.').length - 1] == split[i]) {
                        current = child;
                    }
                });
            }

            current.title = this.form.title;
            current.description = this.form.description;
            current.requirement_type = this.form.requirement_type;
            current.priority = this.form.priority;
            current.risk = this.form.risk;
            current.actor_name = this.form.actor_name;

            this.edit_modal_open = false;
            this.requirements_no_depth = [];
            this.render('', this.details.requirements);

            this.undo_stack.push(JSON.parse(JSON.stringify(this.details)));
            this.redo_stack = [];
        },
        deleteRequirement: function(requirement) {


            //TODO dodaj request, cela ova metoda ide u if ako prodje, 
            // u else ide ovo:
            // this.$buefy.toast.open({
            //         duration: 5000,
            //         message: `Another user is editing this requirement`,
            //         type: 'is-danger'
            //     })

            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            if (requirement.code.split('.').length > 1) {
                let split = requirement.code.split('.');
                let current = null;
                let root_req = null;

                this.details.requirements.forEach((root) => {
                    if (root.code == split[0]) {
                        current     = root;
                        root_req    = root;
                    }
                });

                for (let i = 1; i < split.length - 1; i++) {
                    current.children.forEach((child) => {
                        if (child.code.split('.')[child.code.split('.').length - 1] == split[i]) {
                            current = child;
                        }
                    });
                }

                current.children = current.children.filter((child) => {
                    return child.code !== requirement.code;
                });
            } else {
                this.details.requirements = this.details.requirements.filter((root) => {
                    return root.code !== requirement.code
                });
            }

            this.requirements_no_depth = [];
            this.render('', this.details.requirements);

            this.undo_stack.push(JSON.parse(JSON.stringify(this.details)));
            this.redo_stack = [];
        },
        loadUsers: function(){

            //TODO get request za aktivne usere

            //TODO load history 
            axios.get('/', {
               
            }).then((response) => {
                
                this.users = response.data
                //saljes mi listu usernames

            }).catch((error) => {
            });


        },

        saveModel: function() {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            let body = new FormData();

            body.append('project', this.project_name);
            body.append('model', this.model_name);
            body.append('details', JSON.stringify(this.details));

            axios({
                method: "put",
                url: "/core/models",
                data: body,
                headers: { "Content-Type": "multipart/form-data" },
            }).then((response) => {
                this.load();
            }).catch((error) => {});
        },
        transform: function() {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));
            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }
            let body = new FormData();
            body.append('model', JSON.stringify({
                '_id':{
                    'project': this.project_name,
                    'name': this.model_name 
                },
                'type': 'requirements',
				'details': this.details
            }));
			axios({
                method: 'post',
                url: '/transformer/transform',
                data: body,
                headers: {
					"Content-Type": "multipart/form-data"
				}
            }).then((response) => {
				const mName = response.data._id.name;
				const mProject = response.data._id.project;
				const url = 'http://127.0.0.1:9004/projects/' + mProject + '/models/' + mName + '/functional';
				window.open(url, '_blank');
			});
        },
        addActor: function() {
            this.details.actors.push({
                name: this.actor_form.name
            });

            this.actor_modal_open = false;
            this.actor_form = {
                name: null
            };

            this.stack.push(this.details);
        },
        closeModal: function() {
            this.form = {
                title: null,
                description: null,
                requirement_type: null,
                priority: null,
                risk: null,
                actor_name: null
            };

            this.modal_open = false;
            this.edit_modal_open = false;
        },
        openEditModal: function(requirement) {

            //TODO mozda nije error nego response.data = false ili slicno
            axios.get('/', {
               
            }).then((response) => {
                
                this.editing_requirement = requirement;
                this.form = {
                    parent_id: requirement.parent_id,
                    title: requirement.title,
                    description: requirement.description,
                    requirement_type: requirement.requirement_type,
                    priority: parseInt(requirement.priority),
                    risk: requirement.risk,
                    actor_name: requirement.actor_name
                };

                this.edit_modal_open = true;

            }).catch((error) => {

                this.$buefy.toast.open({
                    duration: 5000,
                    message: `Another user is editing this requirement`,
                    type: 'is-danger'
                })

            });
            

            
        }
    }
}
</script>

<style>
textarea {
    resize: none !important;
}
</style>
