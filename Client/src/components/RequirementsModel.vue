<template>
    <div>
        <Navbar></Navbar>

        <div style="margin-left: 200px; margin-right: 200px; margin-top: 30px">
             <p class="title">
                 {{ model_name }}
             </p>

            <b-tabs>
                <b-tab-item label="Model">
                    <b-field>
                        <b-button @click="() => { this.form.parent_id = null; this.modal_open = true}" type="is-success">
                            <span class="icon"><i class="fas fa-plus"></i></span> <span>Add Root Requirement</span>
                        </b-button>

                        <b-button style="margin-left: auto" type='is-primary' @click="saveModel">Save</b-button>
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

                        <b-table-column label="Manage" v-slot="props">
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
                    <b-field>
                        <b-button @click="actor_modal_open = true" type="is-success">
                            <span class="icon"><i class="fas fa-plus"></i></span> <span>Add Actor</span>
                        </b-button>
                    </b-field>

                    <b-table :data="details.actors">
                        <b-table-column label="Name" v-slot="props">
                            {{ props.row.name }}
                        </b-table-column>
                    </b-table>
                </b-tab-item>
            </b-tabs>
        </div>

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
            }
        };
    },
    mounted: function() {
        this.render('', this.requirements);
        this.load();
    },
    methods: {
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
        load: function() {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            let body = new FormData();
            body.append('project', this.project_name)
            body.append('model', this.model_name)

            axios({
                method: "get",
                url: "/core/models",
                data: body,
                headers: { "Content-Type": "multipart/form-data" },
            }).then((response) => {
                this.name = response.data.name;
                this.details = response.data.details;
                this.render('', this.details.requirements);
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
                    parent_id: current.id,
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
        },
        deleteRequirement: function(requirement) {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            if (requirement.parent_id) {
                let split = requirement.details.code.split('.');
                let current = null;
                let root_req = null;

                this.requirements.forEach((root) => {
                    if (root.details.code == split[0]) {
                        current     = root;
                        root_req    = root;
                    }
                });

                for (let i = 1; i < split.length; i++) {
                    current.children.forEach((child) => {
                        if (child.details.code.split('.')[child.details.code.split('.').length - 1] == split[i]) {
                            current = child;
                        }
                    })
                }

                current.children = current.children.filter((child) => {
                    return child.id !== requirement.id;
                });
            } else {
                let body = new FormData();
                body.append('element', requirement.id);

                // axios({
                //     method: "delete",
                //     url: "/core/elements",
                //     data: body,
                //     headers: { "Content-Type": "multipart/form-data" },
                // }).then((response) => {
                //     this.load();
                // }).catch((error) => {
                //
                // });
            }
        },
        saveModel: function() {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            let body = new FormData();

            body.append('project', this.project_name);
            body.append('model', this.model_name);
            body.append('details', this.details);

            axios({
                method: "put",
                url: "/core/models",
                data: body,
                headers: { "Content-Type": "multipart/form-data" },
            }).then((response) => {
                this.load();
            }).catch((error) => {

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
        }
    }
}
</script>

<style>
textarea {
    resize: none !important;
}
</style>
