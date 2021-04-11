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
                        <b-button @click="() => {form.parent_id = null; modal_open = true}" type="is-success">
                            <span class="icon"><i class="fas fa-plus"></i></span> <span>Add Root Requirement</span>
                        </b-button>
                    </b-field>

                    <b-table
                        :data="requirements_no_depth">

                        <b-table-column label="#" v-slot="props">
                            {{ props.row.details.code }}
                        </b-table-column>

                        <b-table-column label="Title" v-slot="props">
                            {{ props.row.details.title }}
                        </b-table-column>

                        <b-table-column label="Description" v-slot="props">
                            {{ props.row.details.description }}
                        </b-table-column>

                        <b-table-column label="Type" v-slot="props">
                            {{ props.row.details.requirement_type }}
                        </b-table-column>

                        <b-table-column label="Priority" v-slot="props">
                            {{ props.row.details.priority }}
                        </b-table-column>

                        <b-table-column label="Risk" v-slot="props">
                            {{ props.row.details.risk }}
                        </b-table-column>

                        <b-table-column label="Actor" v-slot="props">
                            {{ getActorName(props.row.details.actor_id) }}
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

                    <b-table :data="actors">
                        <b-table-column label="Name" v-slot="props">
                            {{ props.row.details.name }}
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
                        <b-radio-button v-model="form.type"
                                        native-value="functional" expanded>
                            <span>Functional</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.type"
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
                        <b-select v-model="form.actor_id" placeholder="Select an Actor">
                            <option v-for="actor in actors" :key="actor.id" :value="actor.id">
                                {{ actor.details.name }}
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
                        <b-radio-button v-model="form.type"
                                        native-value="functional" expanded>
                            <span>Functional</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.type"
                                        native-value="non_functional" expanded>
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
                        <b-select v-model="form.actor_id" placeholder="Select an Actor">
                            <option v-for="actor in actors" :key="actor.id" :value="actor.id">
                                {{ actor.details.name }}
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
                        <b-input v-model="form.actor_name"></b-input>
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
            // test_requirements: [
            //     {
            //         id: 1,
            //         code: '1',
            //         name: 'req',
            //         priority: 1,
            //         risk: 'low',
            //         children: [
            //             {
            //                 id: 2,
            //                 parent_id: 1,
            //                 code: '1.1',
            //                 name: 'req',
            //                 priority: 1,
            //                 risk: 'low',
            //                 children: [
            //                     {
            //                         id: 5,
            //                         parent_id: 1,
            //                         code: '1.1.1',
            //                         name: 'req',
            //                         priority: 1,
            //                         risk: 'low',
            //                         children: []
            //                     }
            //                 ]
            //             },
            //             {
            //                 id: 3,
            //                 parent_id: 1,
            //                 code: '1.2',
            //                 name: 'req',
            //                 priority: 1,
            //                 risk: 'low',
            //                 children: [
            //                     {
            //                         id: 5,
            //                         parent_id: 1,
            //                         code: '1.2.1',
            //                         name: 'req',
            //                         priority: 1,
            //                         risk: 'low',
            //                         children: []
            //                     },
            //                     {
            //                         id: 5,
            //                         parent_id: 1,
            //                         code: '1.2.2',
            //                         name: 'req',
            //                         priority: 1,
            //                         risk: 'low',
            //                         children: []
            //                     }
            //                 ]
            //             },
            //             {
            //                 id: 4,
            //                 parent_id: 1,
            //                 code: '1.3',
            //                 name: 'req',
            //                 priority: 1,
            //                 risk: 'low',
            //                 children: [
            //                     {

            //                     }
            //                 ]
            //             }
            //         ]
            //     }
            // ],
            modal_open: false,
            edit_modal_open: false,
            actor_modal_open: false,
            parent_requirement: null,
            editing_requirement: null,
            form: {
                code: null,
                title: null,
                description: null,
                requirement_type: null,
                priority: null,
                risk: null,
                actor_id: null,
                actor_name: null
            },
            model: {
                elements: []
            },
            requirements: [
                // {
                // model_name: 'model-name',
                // id: 1,
                // type: 'requirement',
                // details: {
                //     parent_id: null,
                //     title: 'Requirement name',
                //     description: 'Test Test Test Test Test Test Test Test Test Test Test Test',
                //     requirement_type: 'functional',
                //     priority: 1,
                //     risk: 'low',
                //     actor_id: 1,
                //     children: [
                //         {
                //             model_name: 'model-name',
                //             id: 2,
                //             type: 'requirement',
                //             details: {
                //                 parent_id: 1,
                //                 title: 'Requirement name 2',
                //                 description: 'Test Test Test Test Test Test Test Test Test Test Test Test',
                //                 requirement_type: 'functional',
                //                 priority: 1,
                //                 risk: 'low',
                //                 actor_id: 1,
                //                 children: []
                //             }
                //         },
                //         {
                //             model_name: 'model-name',
                //             id: 3,
                //             type: 'requirement',
                //             details: {
                //                 parent_id: 1,
                //                 title: 'Requirement name 3',
                //                 description: 'Test Test Test Test Test Test Test Test Test Test Test Test',
                //                 requirement_type: 'functional',
                //                 priority: 1,
                //                 risk: 'low',
                //                 actor_id: 1,
                //                 children: []
                //             }
                //         }
                //     ]
                // }
            //}
            ],
            actors: [
            //     {
            //     model: 'model-name',
            //     id: 1,
            //     type: 'actor',
            //     details: {
            //         name: 'Actor 1'
            //     }
            // }
            ],
            requirements_no_depth: []
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

                    requirement.details.code = `${index + 1}`;

                    this.render(`${index + 1}`, requirement.details.children);

                } else {

                    requirement.details.code = `${base}.${index + 1}`;

                    this.render(`${base}.${index + 1}`, requirement.details.children);
                }
            });
        },
        load: function() {
            let jwt = sessionStorage.getItem('auth-token');

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            axios.get('/core/models', {
                params: {
                    project: this.project_name,
                    model: this.model_name
                }
            }).then((response) => {
                this.model = response.data.model;

                this.model.elements.forEach((element) => {
                    if (element.type === 'requirement') {
                        this.requirements.push(element);
                    } else if (element.type === 'actor') {
                        this.actors.push(element);
                    }
                });

                this.render('', this.requirements);
            }).catch((error) => {

            });
        },
        addRequirement: function() {

            if (this.parent_requirement) {
                let split = this.parent_requirement.details.code.split('.');
                let current = null;
                let root_req = null;

                this.requirements.forEach((root) => {
                    if (root.details.code == split[0]) {
                        current     = root;
                        root_req    = root;
                    }
                });

                for (let i = 1; i < split.length; i++) {
                    current.details.children.forEach((child) => {
                        if (child.details.code == split[i]) {
                            current = child;
                        }
                    })
                }

                current.details.children.append({
                    model_name: 'model-name',
                    type: 'requirement',
                    details: {
                        parent_id: current.id,
                        title: this.form.name,
                        description: this.form.name,
                        requirement_type: this.form.requirement_type,
                        priority: this.form.priority,
                        risk: this.form.risk,
                        actor_id: this.form.actor_id,
                        children: []
                    }
                });

                axios.put('/core/elements', {
                    element: root_req.id,
                    details: root_req.details
                }).then((response) => {
                    this.load();
                }).catch((error) => {

                });

            } else {

                axios.post('/core/elements', {
                    type: 'requirement',
                    model: this.model_name,
                    details: {
                        parent_id: null,
                        title: this.form.title,
                        description: this.form.description,
                        type: this.form.type,
                        priority: this.form.priority,
                        risk: this.form.risk,
                        actor_id: this.form.actor_id
                    }
                }).then((response) => {
                    this.load();
                }).catch((error) => {

                });
            }
        },
        editRequirement: function() {
            let split = this.editing_requirement.details.code.split('.');
            let current = null;
            let root_req = null;

            this.requirements.forEach((root) => {
                if (root.details.code == split[0]) {
                    current     = root;
                    root_req    = root;
                }
            });

            for (let i = 1; i < split.length; i++) {
                current.details.children.forEach((child) => {
                    if (child.details.code == split[i]) {
                        current = child;
                    }
                })
            }

            current.details.title = this.form.title;
            current.details.description = this.form.description;
            current.details.requirement_type = this.form.requirement_type;
            current.details.priority = this.form.priority;
            current.details.risk = this.form.risk;
            current.details.actor_id = this.form.actor_id;

            axios.put('/core/elements', {
                element: root_req.id,
                details: root_req.details
            }).then((response) => {
                this.load();
            }).catch((error) => {

            });
        },
        deleteRequirement: function(requirement) {
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
                    current.details.children.forEach((child) => {
                        if (child.details.code == split[i]) {
                            current = child;
                        }
                    })
                }

                current.details.children = current.details.children.filter((child) => {
                    return child.id !== requirement.id;
                });

                axios.put('/core/elements', {
                    element: root_req.id,
                    details: root_req.details
                }).then((response) => {
                    this.load();
                }).catch((error) => {

                });
            } else {
                axios.delete('/core/elements', {
                    element: requirement.id
                }).then((response) => {
                    this.load();
                }).catch((error) => {

                });
            }
        },
        addActor: function() {
            axios.post('/core/elements', {
                type: 'actor',
                model: this.model_name,
                details: {
                    name: this.form.actor_name
                }
            }).then((response) => {
                this.load();
            }).catch((error) => {

            });
        },
        closeModal: function() {
            this.form = {
                parent_id: null,
                code: null,
                title: null,
                description: null,
                type: null,
                priority: null,
                risk: null,
                actor_id: null,
                actor_name: null
            };

            this.modal_open = false;
            this.edit_modal_open = false;
        },
        openEditModal: function(requirement) {
            this.editing_requirement = requirement;
            this.edit_modal_open = true;
        },
        getActorName(actor_id) {
            let str = '';

            this.actors.forEach((actor) => {
                if (actor_id == actor.id) {
                    str = actor.details.name;
                }
            });

            return str;
        }
    }
}
</script>

<style>
.ant-pagination {
    margin-right: 30px !important;
}

textarea {
    resize: none !important;
}
</style>