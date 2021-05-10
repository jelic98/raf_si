<template>
    <div>
        <Navbar></Navbar>

        <div  v-if="user && (user.role === 'admin' || user.role === 'project_manager')" class="card">
            <section class="card-content" style="padding: 50px">
                <b-field>
                    <b-radio-button v-for="state in states" :key="state.value" v-model="active_state" :native-value="state.value">
                        <span>{{ `${state.value.charAt(0).toUpperCase()}${state.value.slice(1)}`  }}</span>
                    </b-radio-button>

                    <b-button style="margin-left: auto" type='is-light' @click='undo'>
                        <span class="icon"><i class="fas fa-undo-alt"></i></span>
                    </b-button>

                    <b-button style="margin-left: 10px" type='is-light' @click='redo'>
                        <span class="icon"><i class="fas fa-redo-alt"></i></span>
                    </b-button>

                    <b-button style="margin-left: 10px" type='is-primary' @click='saveModel'>Save</b-button>

                </b-field>

                <b-message v-for="(error, key) in errors" :key="key" type="is-danger">
                    {{ error }}
                </b-message>
            </section>
        </div>

        <b-modal :active.sync="modal_open" has-modal-card @close="modalClose">
            <div class="modal-card">
                <header class="modal-card-head">
                    <p class="modal-card-title">
                        Add Node
                    </p>
                </header>

                <section class="modal-card-body">
                    <b-field>
                        <b-radio-button v-model="form.type" native-value="class">
                            <span>Class</span>
                        </b-radio-button>

                        <b-radio-button v-model="form.type" native-value="interface">
                            <span>Interface</span>
                        </b-radio-button>
                    </b-field>

                    <b-field :type="name_error === true ? 'is-danger' : ''" :message="name_error === true ? 'Name is required' : ''" label="Name">
                        <b-input v-model="form.name" placeholder="Enter a Name"></b-input>
                    </b-field>

                    <hr/>

                    <p class="subtitle">
                        Properties
                        <b-button @click="form.properties.push({ name: null, visibility: null, type: null})" class="is-pulled-right is-small" type="is-primary">
                            <span class="icon"><i class="fas fa-plus"></i></span>
                        </b-button>
                    </p>

                    <b-table :data="form.properties">
                        <b-table-column placeholder="Enter a Name" label="Name" v-slot="props">
                            <b-input v-model="props.row.name"></b-input>
                        </b-table-column>

                        <b-table-column placeholder="Select a Visbility" label="Visibility" v-slot="props">
                            <b-select v-model="props.row.visibility">
                                <option value="private">Public</option>
                                <option value="public">Private</option>
                            </b-select>
                        </b-table-column>

                        <b-table-column placeholder="Select a Type" label="Type" v-slot="props">
                            <b-select v-model="props.row.type">
                                <option v-for="(type, index) in nodeTypes" :key="index" :value="type.value">{{ type.display }}</option>
                            </b-select>
                        </b-table-column>
                    </b-table>

                    <hr/>

                    <p class="subtitle">
                        Methods
                        <b-button @click="form.methods.push({ name: null, visibility: null, type: null, parameters: []})" class="is-pulled-right is-small" type="is-primary">
                            <span class="icon"><i class="fas fa-plus"></i></span>
                        </b-button>
                    </p>

                    <b-table :data="form.methods">
                        <b-table-column label="Name" v-slot="props">
                            <b-input placeholder="Enter a Name" v-model="props.row.name"></b-input>
                        </b-table-column>

                        <b-table-column label="Visibility" v-slot="props">
                            <b-select placeholder="Select a Visbility" v-model="props.row.visibility">
                                <option value="private">Public</option>
                                <option value="public">Private</option>
                            </b-select>
                        </b-table-column>

                        <b-table-column label="Type" v-slot="props">
                            <b-select placeholder="Select a Type" v-model="props.row.type">
                                <option v-for="(type, index) in nodeTypes" :key="index" :value="type.value">{{ type.display }}</option>
                            </b-select>
                        </b-table-column>

                        <b-table-column label="Parameters" v-slot="props">
                            <b-button @click="openParameterModal(props.row)">
                                Open
                            </b-button>
                        </b-table-column>
                    </b-table>
                </section>

                <footer class="modal-card-foot">
                    <b-button style="margin-left: 10px" type='is-success' @click="() => { editing_node === true ? saveEditNode() : createNode()}" expanded>Save</b-button>
                </footer>
            </div>
        </b-modal>

        <b-modal :active.sync="parameter_modal_open" has-modal-card @close="modalClose" :can-cancel="false">
            <div class="modal-card">
                <header class="modal-card-head">
                    <p class="modal-card-title">Parameters</p>
                </header>

                <section class="modal-card-body">

                    <b-button @click="parameters_temp.push({ name: null, type: null })" class="is-small" type="is-primary">
                        <span class="icon"><i class="fas fa-plus"></i></span>
                    </b-button>

                    <b-table :data="active_method.parameters.concat(parameters_temp)">
                        <b-table-column label="Name" v-slot="props">
                            <b-input placeholder="Enter a Name" v-model="props.row.name"></b-input>
                        </b-table-column>

                        <b-table-column label="Type" v-slot="props">
                            <b-select placeholder="Select a Type" v-model="props.row.type">
                                <option v-for="(type, index) in nodeTypes" :key="index" :value="type.value">{{ type.display }}</option>
                            </b-select>
                        </b-table-column>
                    </b-table>
                </section>

                <footer class="modal-card-foot">
                    <b-button style="margin-left: 10px" type='is-light' @click="() => { parameter_modal_open = false; active_method = {parameters: []}; parameters_temp = []; }" expanded>Cancel</b-button>
                    <b-button style="margin-left: 10px" type='is-success' @click="saveParameters" expanded>Save</b-button>
                </footer>
            </div>
        </b-modal>

        <div id='diagramDiv' style='width: 100vw; height: 75vh;'></div>
    </div>
</template>
<script>
import Navbar from "./Navbar";
import go from 'gojs';
var $=go.GraphObject.make;
export default {
    name: 'ClassModel',
    components: {
        Navbar
    },
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
    data: function() {
        return {
            form: {
                type: null,
                name: null,
                properties: [],
                methods: []
            },
            // properties: [
            //     {
            //         name: 'Property 1',
            //         visibility: 'public',
            //         type: 'string'
            //     },
            //     {
            //         name: 'Property 2',
            //         visibility: 'public',
            //         type: 'int'
            //     },
            //     {
            //         name: 'Property 3',
            //         visibility: 'private',
            //         type: 'string'
            //     }
            // ],
            // methods: [
            //     {
            //         name: 'Method 1',
            //         visibility: 'public',
            //         type: 'string'
            //     },
            //     {
            //         name: 'Method 2',
            //         visibility: 'public',
            //         type: 'int'
            //     },
            //     {
            //         name: 'Method 3',
            //         visibility: 'private',
            //         type: 'string'
            //     }
            // ],
            modal_open: false,
            active_state: null,
            nodes: [],
            links: [],
            diagram: null,
            nodeKey: 0,
            states: [
                { value: 'class' },
                { value: 'generalization' },
                { value: 'aggregation' },
                { value: 'composition' }
            ],
            name_error: false,
            event: null,
            nodeTypes: [
                {
                    value: 'int',
                    display: 'Integer'
                },
                {
                    value: 'string',
                    display: 'String'
                }
            ],
            errors: [],
            user: null,
            parameter_modal_open: false,
            active_method: {
                parameters: []
            },
            parameters_temp: [],
            editing_node: false,
            editing_node_key: null
        };
    },
    mounted: function() {
        this.user = JSON.parse(sessionStorage.getItem('auth-user'));

        this.loadModel();
    },
    methods: {
        saveParameters: function() {
            this.parameters_temp.forEach((param) => {
                this.active_method.parameters.push(param);
            });

            this.active_method = {
                parameters: []
            };

            this.parameters_temp = [];
            this.parameter_modal_open = false;
        },
        openParameterModal: function(method) {
            this.parameter_modal_open = true;
            this.active_method = method;
        },
        undo: function() {
            this.diagram.undoManager.undo();
        },
        redo: function () {
            this.diagram.undoManager.redo();
        },
        // getState: function() {
		// 	console.log(this.states)
        //     for(let i = 0; i < this.states.length; i++) {
        //         let s = this.states[i];
        //         if(s.active) {
        //             return s.value;
        //         }
        //     }
        //
        //     return this.states[0].value;
        // },
        convertVisibility: function(v) {
            switch (v) {
                case 'public':
                    return '+';
                case 'private':
                    return '-';
                case 'protected':
                    return '#';
                case 'package':
                    return '~';
                default:
                    return v;
            }
        },
        convertToArrow: function(r) {
            switch (r) {
                case 'generalization':
                    return 'Triangle';
                case 'aggregation':
                    return 'StretchedDiamond';
                case 'composition':
                    return 'StretchedDiamond';
                default:
                    return '';
            }
        },
        convertFill: function(r) {
            switch (r) {
                case 'generalization':
                case 'aggregation':
                    return 'white';
                case 'composition':
                    return 'black';
                default:
                    return '';
            }
        },
        loadModel: function() {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            // TODO: Ne radi
            // var axios = require('axios');
            // var FormData = require('form-data');
            // var data = new FormData();
            // data.append('name', 'asd');
            // data.append('project', 'MojProjekat');
            // data.append('details', 'a');
            //
            // var config = {
            //     method: 'get',
            //     url: '/core/models',
            //     headers: {
            //         'Authorization': 'bla bla bla'
            //     },
            //     data : data
            // };
            //
            // axios(config)
            //     .then(function (response) {
            //         console.log(JSON.stringify(response.data));
            //     })
            //     .catch(function (error) {
            //         console.log(error);
            //     });

            axios.get('/core/models', {
                params: {
                    project: this.project_name,
                    model: this.model_name
                }
            }).then((response) => {
                this.nodes = [];
                this.links = [];

                if (response.data) {let detalji = JSON.parse(response.data.details);
                    this.nodes = detalji.nodes;
                    this.links = detalji.links;
                } else {
                    this.$router.push('/projects');
                }

                this.initDiagram();
            }).catch((error) => {
                this.initDiagram();
            });
        },
        saveModel: function() {
            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            let body = new FormData();
            body.append('model', JSON.stringify({
                'project': this.project_name,
                'model': this.model_name,
                'type': 'class',
                'details': {
                    nodes: this.nodes,
                    links: this.links
                }
            }));

            axios({
                method: 'post',
                url: '/validator/validate',
                data: body,
                headers: {
                    "Content-Type": "multipart/form-data"
                }
            }).then((response) => {
                if (response.data.errors && response.data.errors.length > 0) {
                    this.errors = response.data.errors;
                } else {
                    body = new FormData();

                    body.append('project', this.project_name);
                    body.append('model', this.model_name);
                    body.append('details', JSON.stringify({
                        nodes: this.nodes,
                        links: this.links
                    }));

                    axios({
                        method: 'put',
                        url: '/core/models',
                        data: body,
                        headers: {
                            "Content-Type": "multipart/form-data"
                        }
                    }).then((response) => {
                        this.loadModel();
                        this.initDiagram();
                    }).catch((error) => {});
                }
            }).catch((error) => {});
        },
        openModal: function(e) {
            switch (this.active_state) {
                case 'class':
                    this.event = e;
                    this.modal_open = true;
                    break;
            }
        },
        createNode: function() {
            let node = {
                key: this.nodeKey++,
                loc: this.event.diagram.lastInput.documentPoint,
                properties: this.form.properties,
                methods: this.form.methods,
                type: this.form.type
            };

            if (this.form.type === 'interface') {
                node.name = `<<Interface>>\n${this.form.name}`;
            } else {
                node.name = this.form.name;
            }

            if (!this.form.name) {
                this.name_error = true;
            } else {
                this.active_state = null;

                this.event = null;

                // this.nodes.push(node);
                this.diagram.commit((d) => {
                    d.model.addNodeData(node);
                });

                this.nodeTypes.push({
                    value: node.name,
                    display: node.name
                })

                this.modalClose();
            }
        },
        saveEditNode: function() {
            let data = this.diagram.model.findNodeDataForKey(this.editing_node_key);
            console.log(this.form);
            console.log(data);
            this.diagram.model.set(data, 'type', this.form.type);
            this.diagram.model.set(data, 'name', this.form.name);
            this.diagram.model.set(data, 'properties', this.form.properties);
            this.diagram.model.set(data, 'methods', this.form.methods);
            
            this.diagram.updateAllTargetBindings();

            this.editing_node = false;
            this.editing_node_key = null;
            this.modal_open = false;
            this.initDiagram();
        },
        createLink: function(e) {
            let from = this.nodes.filter((node) => {
                return node.key === e.subject.data.from;
            })[0];

            let to = this.nodes.filter((node) => {
                return node.key === e.subject.data.to;
            })[0];

            // this.links.push({
            //     from: from,
            //     to: to,
            //     type: this.active_state
            // });

            this.diagram.model.setDataProperty(e.subject.data, 'relationship', this.active_state);
            // this.diagram.model.setDataProperty(e.subject.data, 'from_node', from);
            // this.diagram.model.setDataProperty(e.subject.data, 'to_node', to);

            this.active_state = null;
        },
        makePort: function(name, spot) {
            return $(go.Shape, 'Rectangle', {
                fill: 'rgba(0, 0, 0, 0.3)',
                stroke: null,
                desiredSize: new go.Size(10, 10),
                alignment: spot,
                alignmentFocus: spot,
                portId: name,
                fromSpot: spot,
                toSpot: spot,
                fromLinkable: true,
                toLinkable: true,
                cursor: 'pointer'
            });
        },
        initDiagram: function() {
            var propertyTemplate =
                $(go.Panel, 'Horizontal',
                    $(go.TextBlock, {
                        isMultiline: false,
                        editable: false,
                        width: 12
                    }),
                    $(go.TextBlock, {
                            isMultiline: false,
                            editable: true
                        },
                        new go.Binding('text', 'name').makeTwoWay(),
                        new go.Binding('isUnderline', 'scope', function(s) {
                            return s[0] === 'c';
                        })),
                    $(go.TextBlock, '', new go.Binding('text', 'type', function(t) {
                        return (t ? ': ' : '');
                    })),
                    $(go.TextBlock, {
                            isMultiline: false,
                            editable: true
                        },
                        new go.Binding('text', 'type').makeTwoWay()),
                    $(go.TextBlock, {
                            isMultiline: false,
                            editable: false
                        },
                        new go.Binding('text', 'default', function(s) {
                            return s ? ' = ' + s : '';
                        })),
                    new go.Binding('text', 'visibility', this.convertVisibility));

            var methodTemplate =
                $(go.Panel, 'Horizontal',
                    $(go.TextBlock, {
                            isMultiline: false,
                            editable: false,
                            width: 12
                        },
                        new go.Binding('text', 'visibility', this.convertVisibility)),
                    $(go.TextBlock, {
                            isMultiline: false,
                            editable: true
                        },
                        new go.Binding('text', 'name').makeTwoWay(),
                        new go.Binding('isUnderline', 'scope', function(s) {
                            return s[0] === 'c';
                        })),
                    $(go.TextBlock, '()',
                        new go.Binding('text', 'parameters', function(parr) {
                            var s = '(';
                            for (var i = 0; i < parr.length; i++) {
                                var param = parr[i];
                                if (i > 0) s += ', ';
                                s += param.name + ': ' + param.type;
                            }
                            return s + ')';
                        })),
                    $(go.TextBlock, '', new go.Binding('text', 'type', function(t) {
                        return (t ? ': ' : '');
                    })),
                    $(go.TextBlock, {
                            isMultiline: false,
                            editable: true
                        },
                        new go.Binding('text', 'type').makeTwoWay())
                );

            this.diagram =
                $(go.Diagram, 'diagramDiv', {
                    'undoManager.isEnabled': true,
                    'draggingTool.dragsLink': true,
                    'draggingTool.isGridSnapEnabled': true,
                    layout: $(go.TreeLayout, {
                        angle: 90,
                        path: go.TreeLayout.PathSource,
                        setsPortSpot: false,
                        setsChildPortSpot: false,
                        isOngoing: false,
                        arrangement: go.TreeLayout.ArrangementHorizontal
                    }),
                    nodeTemplate: $(go.Node, 'Auto', {
                            locationSpot: go.Spot.Center,
                            fromSpot: go.Spot.AllSides,
                            toSpot: go.Spot.AllSides,
                            doubleClick: this.editNode
                        },
                        new go.Binding('location', 'loc').makeTwoWay(),
                        $(go.Shape, {
                            fill: 'white'
                        }),
                        $(go.Panel, 'Table', {
                                defaultRowSeparatorStroke: 'black'
                            },
                            $(go.TextBlock, {
                                    row: 0,
                                    columnSpan: 2,
                                    margin: 8,
                                    alignment: go.Spot.Center,
                                    textAlign: 'center',
                                    font: 'bold 12pt sans-serif',
                                    isMultiline: true,
                                    editable: true
                                },
                                new go.Binding('text', 'name').makeTwoWay()),
                            $(go.TextBlock, 'Properties', {
                                    row: 1,
                                    font: 'italic 10pt sans-serif'
                                },
                                new go.Binding('visible', 'visible', function(v) {
                                    return !v;
                                }).ofObject('PROPERTIES')),
                            $(go.Panel, 'Vertical', 
                            {
                                    name: 'PROPERTIES'
                                },
                                new go.Binding('itemArray', 'properties'), {
                                    row: 1,
                                    margin: 3,
                                    stretch: go.GraphObject.Fill,
                                    defaultAlignment: go.Spot.Left,
                                    background: 'white',
                                    itemTemplate: propertyTemplate
                                }
                            ),
                            $('PanelExpanderButton', 'PROPERTIES', {
                                    row: 1,
                                    column: 1,
                                    alignment: go.Spot.TopRight,
                                    visible: false
                                },
                                new go.Binding('visible', 'properties', function(arr) {
                                    return arr.length > 0;
                                })),
                            $(go.TextBlock, 'Methods', {
                                    row: 2,
                                    font: 'italic 10pt sans-serif'
                                },
                                new go.Binding('visible', 'visible', function(v) {
                                    return !v;
                                }).ofObject('METHODS')),
                            $(go.Panel, 'Vertical', 
                            {
                                    name: 'METHODS'
                                },
                                new go.Binding('itemArray', 'methods'), {
                                    row: 2,
                                    margin: 3,
                                    stretch: go.GraphObject.Fill,
                                    defaultAlignment: go.Spot.Left,
                                    background: 'white',
                                    itemTemplate: methodTemplate
                                }
                            ),
                            $('PanelExpanderButton', 'METHODS', {
                                    row: 2,
                                    column: 1,
                                    alignment: go.Spot.TopRight,
                                    visible: false
                                },
                                new go.Binding('visible', 'methods', function(arr) {
                                    return arr.length > 0;
                                }))
                        ),
                        this.makePort('T', go.Spot.Top),
                        this.makePort('R', go.Spot.Right),
                        this.makePort('B', go.Spot.Bottom),
                        this.makePort('L', go.Spot.Left), {
                            mouseEnter: function(e, node) {
                                node.ports.each(function(p) {
                                    p.fill = 'rgba(0,0,0,.3)';
                                });
                            },
                            mouseLeave: function(e, node) {
                                node.ports.each(function(p) {
                                    p.fill = null;
                                });
                            }
                        }),
                    linkTemplate: $(go.Link, {
                            routing: go.Link.Orthogonal
                        },
                        $(go.Shape),
                        $(go.Shape, {
                                scale: 1.5
                            },
                            new go.Binding("toArrow", "relationship", this.convertToArrow),
                            new go.Binding("fill", "relationship", this.convertFill))
                    )
                });
            this.diagram.model = new go.GraphLinksModel(this.nodes, this.links);
            //this.diagram.model = $(go.GraphLinksModel);
            //this.diagram.model.nodeDataArray = this.nodes;
            this.diagram.addDiagramListener('BackgroundSingleClicked', this.openModal);
            this.diagram.addDiagramListener('LinkDrawn', this.createLink);
        },
        modalClose: function() {
            this.name_error = false;
            this.modal_open = false;

            this.form = {
                type: null,
                name: null,
                properties: [],
                methods: []
            };
        },
        editNode: function(e, node) {
            this.editing_node = true;
            this.editing_node_key = node.data.key;

            this.form = {
                type: node.data.type,
                name: node.data.name,
                properties: node.data.properties,
                methods: node.data.methods,
            }

            this.modal_open = true;
        }
    }
}
</script>
