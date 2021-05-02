<template>
    <div>
        <Navbar></Navbar>

        <div class="card">
            <section class="card-content" style="padding: 50px">
                <b-field>
                    <b-radio-button v-for="state in states" :key="state.value" v-model="active_state" :native-value="state.value">
                        <span>{{ `${state.value.charAt(0).toUpperCase()}${state.value.slice(1)}`  }}</span>
                    </b-radio-button>

                    <b-button style="margin-left: auto" type='is-primary' @click='saveModel'>Save</b-button>
                </b-field>
            </section>
        </div>

        <b-modal :active.sync="modal_open" has-modal-card>
            <div class="modal-card">
                <header class="modal-card-head">
                    <p class="modal-card-title">
                        Add Node
                    </p>
                </header>

                <section class="modal-card-body">
                    <b-field label="Name">
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
                                <option value="string">String</option>
                                <option value="int">Integer</option>
                            </b-select>
                        </b-table-column>
                    </b-table>

                    <hr/>

                    <p class="subtitle">
                        Methods
                        <b-button @click="form.methods.push({ name: null, visibility: null, type: null})" class="is-pulled-right is-small" type="is-primary">
                            <span class="icon"><i class="fas fa-plus"></i></span>
                        </b-button>
                    </p>

                    <b-table :data="form.methods">
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
                                <option value="string">String</option>
                                <option value="int">Integer</option>
                            </b-select>
                        </b-table-column>
                    </b-table>
                </section>

                <footer class="modal-card-foot">
                    <b-button style="margin-left: 10px" type='is-success' @click="createNode" expanded>Save</b-button>
                </footer>
            </div>
        </b-modal>

        <div id='diagramDiv' style='width: 100vw; height: 90vh;'></div>
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
            event: null
        };
    },
    mounted: function() {
        this.loadModel();
        this.initDiagram();
    },
    methods: {
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
            var json = '[{"key":0,"loc":{"x":125,"y":-178.09375,"s":true},"name":"Professor","properties":[{"name":"classes","type":"List<Course>","visibility":"public","__gohashid":629}],"methods":[{"name":"teach","parameters":[{"name":"class","type":"Course"}],"visibility":"private","__gohashid":651}],"__gohashid":567}]';

            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            let body = new FormData();
            body.append('project', this.project_name)
            body.append('model', this.model_name)

            axios({
                method: "get",
                url: "/core/models/",
                data: body,
                headers: {
                    "Content-Type": "multipart/form-data"
                },
            }).then((response) => {
                this.name = response.data.name;
                this.model.elements = response.data.elements;

                this.nodes = [];
                this.links = [];

                this.model.elements.forEach((element) => {
                    if (element.type === 'class') {
                        this.nodes.push(element);
                    } else if (element.type === 'link') {
                        this.links.push(element);
                    }
                });
                this.nodes = JSON.parse(json);
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
            body.append('elements', this.nodes.concat(this.links));

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
            }).catch((error) => {

            });
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
            // var node = {
            //     key: this.nodeKey++,
            //     loc: this.event.diagram.lastInput.documentPoint,
            //     name: 'Professor',
            //     properties: [{
            //         name: 'classes',
            //         type: 'List<Course>',
            //         visibility: 'public'
            //     }, {
            //         name: 'classes2',
            //         type: 'List<Course>',
            //         visibility: 'public'
            //     }],
            //     methods: [{
            //         name: 'teach',
            //         parameters: [{
            //             name: 'class',
            //             type: 'Course'
            //         }],
            //         visibility: 'private'
            //     }, {
            //         name: 'teach2',
            //         parameters: [{
            //             name: 'class',
            //             type: 'Course'
            //         }],
            //         visibility: 'private'
            //     }]
            // };

            let node = {
                key: this.nodeKey++,
                loc: this.event.diagram.lastInput.documentPoint,
                name: this.form.name,
                properties: this.form.properties,
                methods: this.form.methods
            };

            this.active_state = null;

            var jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            var body = new FormData();

            this.modal_open = false;
            this.event = null;

            body.append('type', 'class');
            body.append('model', this.model_name);
            body.append('details', node);

            axios({
                method: 'post',
                url: '/core/elements',
                data: body,
                headers: { "Content-Type": "multipart/form-data" },
            }).then((response) => {
                var body = new FormData();

                body.append('project', this.project_name);
                body.append('model', this.model_name);
                body.append('elements', this.nodes.concat(this.links));

                axios({
                    method: 'put',
                    url: '/core/models',
                    data: body,
                    headers: {
                        "Content-Type": "multipart/form-data"
                    }
                });

                this.loadModel();
                this.initDiagram();
            }).catch((error) => {

            });

            // this.nodes.push(node);
            this.diagram.model.addNodeData(node);
        },
        createLink: function(e) {
            let from = this.nodes.filter((node) => {
                return node.key === e.subject.data.from;
            })[0];

            let to = this.nodes.filter((node) => {
                return node.key === e.subject.data.to;
            })[0];

            this.links.push({
                from: from,
                to: to,
                type: this.active_state
            });

            this.diagram.model.setDataProperty(e.subject.data, 'relationship', this.active_state);

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
        deleteNode: function(e) {
            let to_delete = { nodes: [], links: [] };

            this.diagram.selection.each((part) => {
                if (part instanceof go.Node) {
                    let node = this.nodes.filter((node) => {
                        return node.key === part.key;
                    })[0];

                    to_delete.nodes.push(node);
                }
            });

            to_delete.nodes.forEach((node) => {
                let body = new FormData();
                body.append('element', node.id);

                axios({
                    method: "delete",
                    url: "/core/elements",
                    data: body,
                    headers: { "Content-Type": "multipart/form-data" },
                }).then((response) => {
                }).catch((error) => {
                    body = new FormData();

                    body.append('project', this.project_name);
                    body.append('model', this.model_name);
                    body.append('elements', this.nodes.concat(this.links));

                    axios({
                        method: 'put',
                        url: '/core/models',
                        data: body,
                        headers: {
                            "Content-Type": "multipart/form-data"
                        }
                    });
                });
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
                    'linkingTool.isUnconnectedLinkValid': true,
                    'commandHandler.deleteSelection': this.deleteNode,
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
                            toSpot: go.Spot.AllSides
                        },
                        new go.Binding('location', 'loc').makeTwoWay(),
                        $(go.Shape, {
                            fill: 'lightyellow'
                        }),
                        $(go.Panel, 'Table', {
                                defaultRowSeparatorStroke: 'black'
                            },
                            $(go.TextBlock, {
                                    row: 0,
                                    columnSpan: 2,
                                    margin: 3,
                                    alignment: go.Spot.Center,
                                    font: 'bold 12pt sans-serif',
                                    isMultiline: false,
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
                            $(go.Panel, 'Vertical', {
                                    name: 'PROPERTIES'
                                },
                                new go.Binding('itemArray', 'properties'), {
                                    row: 1,
                                    margin: 3,
                                    stretch: go.GraphObject.Fill,
                                    defaultAlignment: go.Spot.Left,
                                    background: 'lightyellow',
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
                            $(go.Panel, 'Vertical', {
                                    name: 'METHODS'
                                },
                                new go.Binding('itemArray', 'methods'), {
                                    row: 2,
                                    margin: 3,
                                    stretch: go.GraphObject.Fill,
                                    defaultAlignment: go.Spot.Left,
                                    background: 'lightyellow',
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
            this.diagram.model = $(go.GraphLinksModel);
            this.diagram.model.nodeDataArray = this.nodes;
            this.diagram.addDiagramListener('BackgroundSingleClicked', this.openModal);
            this.diagram.addDiagramListener('LinkDrawn', this.createLink);
        }
    }
}
</script>
