<template>
    <div>
        <Navbar></Navbar>

        <div class="card">
            <section class="card-content" style="padding: 50px">
                <b-field>
                    <b-radio-button v-for="state in states" :key="state.value" v-model="active_state" :native-value="state">
                        <span>{{ state.display  }}</span>
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

        <div id='diagramDiv' style='width: 100vw; height: 75vh;'></div>
    </div>
</template>

<script>
import Navbar from "./Navbar";
import go from "gojs";

const $ = go.GraphObject.make;  // for conciseness in defining templates

export default {
    name: "UseCase",
    components: {Navbar},
    props: {
        project_name: {
            required: true
        },
        model_name: {
            required: true
        }
    },
    data: function() {
        return {
            node_key: 0,
            nodes: [],
            links: [],
            active_state: null,
            relation_active: false,
            relation_type: null,
            diagram: null,
            states: [
                {
                    display: 'Use Case',
                    value: 'use_case'
                },
                {
                    display: 'Actor',
                    value: 'actor'
                },
                {
                    display: 'Generalization',
                    value: 'generalization',
                    text: '',
                    category: 'no_text'
                },
                {
                    display: 'Extends',
                    value: 'extends',
                    text: '<<extends>>',
                    category: 'text'
                },
                {
                    display: 'Includes',
                    value: 'includes',
                    text: '<<includes>>',
                    category: 'text'
                },
                {
                    display: 'Association',
                    value: 'association',
                    text: '',
                    category: 'no_text'
                }
            ],
            use_cases: [
                {
                    id: 'draggable1',
                    type: 'use-case',
                    model: 'model1',
                    details: {
                        text: 'Use Case 1',
                        pos_x: null,
                        pos_y: null,
                        relations: []
                    }
                },
                {
                    id: 'draggable2',
                    type: 'use-case',
                    model: 'model1',
                    details: {
                        text: 'Use Case 2',
                        pos_x: null,
                        pos_y: null,
                        relations: []
                    }
                },
                {
                    id: 'draggable3',
                    type: 'use-case',
                    model: 'model1',
                    details: {
                        text: 'Use Case 3',
                        pos_x: null,
                        pos_y: null,
                        relations: []
                    }
                }
            ],
            first: null,
            second: null,
            errors: []
        }
    },
    mounted: function() {
        this.loadModel();
    },
    methods: {
        undo: function() {
            this.diagram.undoManager.undo();
        },
        redo: function () {
            this.diagram.undoManager.redo();
        },
        convertToArrow: function(r) {
            switch (r) {
                case 'association':
                    return '';
                default:
                    return 'Triangle';
            }
        },
        convertFill: function(r) {
            switch (r) {
                case 'generalization':
                    return 'white';
                default:
                    return 'Black';
            }
        },
        loadModel: function() {
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
                this.nodes = [];
                this.links = [];

                this.nodes = response.data.details.nodes;
                this.links = response.data.details.links;

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
                'type': 'functional',
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
                    this.init();
                }).catch((error) => {});

            }).catch((error) => {
                this.errors = error.data.errors;
            });
        },
        initDiagram: function() {
            this.diagram =
                $(go.Diagram, "diagramDiv",
                    {
                        initialAutoScale: go.Diagram.Uniform,
                        contentAlignment: go.Spot.Center,
                        layout: $(go.TreeLayout, {
                            angle: 90,
                            path: go.TreeLayout.PathSource,
                            setsPortSpot: false,
                            setsChildPortSpot: false,
                            isOngoing: false,
                            arrangement: go.TreeLayout.ArrangementHorizontal
                        })
                    });

            let actorTemplate =
                $(go.Node, "Vertical",
                    { locationSpot: go.Spot.Center },
                    new go.Binding('location', 'loc').makeTwoWay(),
                    $(go.Picture,
                        { maxSize: new go.Size(50, 50) },
                        new go.Binding("source", "img")),
                    $(go.TextBlock,
                        {
                            editable: true,
                            margin: new go.Margin(3, 0, 0, 0),
                            maxSize: new go.Size(100, 30),
                            isMultiline: false
                        },
                        new go.Binding("text", "text")),
                    this.makePort('T', go.Spot.Top), {
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
                    });

            let useCaseTemplate =
                $(go.Node, "Auto",  // the whole node panel
                    { locationSpot: go.Spot.Center },
                    new go.Binding('location', 'loc').makeTwoWay(),
                    // define the node's outer shape, which will surround the TextBlock
                    $(go.Shape, "Rectangle",
                        { fill: $(go.Brush, "Linear", { 0: "rgb(254, 201, 0)", 1: "rgb(254, 162, 0)" }), stroke: "black" }),
                    $(go.TextBlock,
                        { editable: true, font: "bold 10pt helvetica, bold arial, sans-serif", margin: 4 },
                        new go.Binding("text", "text")),
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
                    });

            let textLinkTemplate =
                $(go.Link, {
                        routing: go.Link.Orthogonal
                    },
                    $(go.Shape),
                    $(go.Panel, "Auto",
                        $(go.Shape,
                            {
                                fill: $(go.Brush, "Radial", { 0: "rgb(240, 240, 240)", 0.3: "rgb(240, 240, 240)", 1: "rgba(240, 240, 240, 0)" }),
                                stroke: null
                            }),
                        $(go.TextBlock,  // the label text
                            {
                                textAlign: "center",
                                font: "10pt helvetica, arial, sans-serif",
                                stroke: "#555555",
                                margin: 4
                            },
                            new go.Binding("text", "text"),
                            new go.Binding("toArrow", "relationship", this.convertToArrow),
                            new go.Binding("fill", "relationship", this.convertFill)
                        ),
                    ),
                    $(go.Shape, {
                            scale: 1.5
                        },
                        new go.Binding("toArrow", "relationship", this.convertToArrow),
                        new go.Binding("fill", "relationship", this.convertFill))
                );

            let noTextLinkTemplate =
                $(go.Link, {
                        routing: go.Link.Orthogonal
                    },
                    $(go.Shape),
                    $(go.Panel, "Auto",
                        $(go.TextBlock,  // the label text
                            {
                                textAlign: "center",
                                font: "10pt helvetica, arial, sans-serif",
                                stroke: "#555555",
                                margin: 4
                            },
                            new go.Binding("text", "text"),
                            new go.Binding("toArrow", "relationship", this.convertToArrow),
                            new go.Binding("fill", "relationship", this.convertFill)
                        ),
                    ),
                    $(go.Shape, {
                            scale: 1.5
                        },
                        new go.Binding("toArrow", "relationship", this.convertToArrow),
                        new go.Binding("fill", "relationship", this.convertFill))
                );

            let nodeTemplateMap = new go.Map();
            nodeTemplateMap.add('use_case', useCaseTemplate);
            nodeTemplateMap.add('actor', actorTemplate);

            let linkTemplateMap = new go.Map();
            linkTemplateMap.add('text', textLinkTemplate);
            linkTemplateMap.add('no_text', noTextLinkTemplate);

            this.diagram.nodeTemplateMap = nodeTemplateMap;
            this.diagram.linkTemplateMap = linkTemplateMap;
            this.diagram.model.undoManager.isEnabled = true;

            this.diagram.model = new go.GraphLinksModel(this.nodes, this.links);

            this.diagram.addDiagramListener('TextEdited', (e) => {
                let node = this.nodes.filter((node) => {
                    return node.key === e.subject.part.key;
                })[0];

                node.text = e.subject.text;
            });

            this.diagram.addDiagramListener('BackgroundSingleClicked', this.createNode);

            this.diagram.addDiagramListener('LinkDrawn', this.createLink);
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
        createNode: function(e) {
            if (this.active_state.value === 'use_case' || this.active_state.value === 'actor') {

                this.diagram.commit((d) => {
                    d.model.addNodeData({
                        key: this.node_key++,
                        loc: e.diagram.lastInput.documentPoint,
                        text: this.active_state.value === 'use_case' ? 'New Use Case' : 'New Actor',
                        category: this.active_state.value,
                        img: "https://cdn.discordapp.com/attachments/558738094795522049/839263865623019551/unknown.png"
                    });
                });

            }
        },
        createLink: function(e) {
            this.diagram.model.setDataProperty(e.subject.data, 'category', this.active_state.category);
            this.diagram.model.setDataProperty(e.subject.data, 'text', this.active_state.text);
            this.diagram.model.setDataProperty(e.subject.data, 'relationship', this.active_state.value);
        }
    }
}
</script>

<style scoped>
.canvas-wrap { position:relative; width:800px; height:600px }
.canvas-wrap canvas { position:absolute; top:0; left:0; z-index:0 }
</style>
