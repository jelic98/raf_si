<template>
    <div>
        <Navbar></Navbar>

        <div class="card">
            <section class="card-content" style="padding: 50px">
                <b-field>
                    <b-radio-button v-for="state in states" :key="state.value" v-model="active_state" :native-value="state">
                        <span>{{ state.display  }}</span>
                    </b-radio-button>

                    <b-button style="margin-left: auto" type='is-primary' @click='saveModel'>Save</b-button>
                </b-field>
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
        // model: {
        //     required: true
        // },
        // project: {
        //     required: true
        // }
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
            second: null
        }
    },
    mounted: function() {
        this.init();
    },
    methods: {
        saveModel: function() {

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
        init: function() {
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
                        img: "https://cdn.discordapp.com/attachments/558738094795522049/838866440282177566/44-444921_stick-man-fight-kungfu-side-stick-figure-kung.png"
                    });
                });

            }
        },
        createLink: function(e) {
            this.diagram.model.setDataProperty(e.subject.data, 'category', this.active_state.category);
            this.diagram.model.setDataProperty(e.subject.data, 'text', this.active_state.text);
            this.diagram.model.setDataProperty(e.subject.data, 'relationship', this.active_state.value);
        },
        addUseCase: function() {
            let use_case = {
                type: 'use-case',
                model: this.model,
                details: {
                    text: 'New Use Case',
                    pos_x: null,
                    pos_y: null,
                    relations: []
                }
            };

            axios.post('/core/elements', use_case).then((response) => {
                use_case.id = response.data.new_use_case.id;

                this.use_cases.push(use_case);

                axios.put('/core/models', {
                    elements: this.use_cases,
                    model: this.model,
                    project: this.project
                });
            });
        },
        updatePosition: function(data) {
            const $scope = this;

            let draggable = $scope.use_cases.filter((use_case) => {
                return use_case.id === data.id;
            })[0];

            draggable.details.pos_x = data.event.clientX;
            draggable.details.pos_y = data.event.clientY;

            $scope.clearCanvas();
            $scope.drawRelations();
        },
        setFirst: function(data) {
            const $scope = this;
            $scope.first = data;
        },
        setSecond: function(data) {
            const $scope = this;
            let second = null;

            $scope.use_cases.forEach((use_case) => {
                if (data.clientX > use_case.details.pos_x - 50 &&
                    data.clientX < use_case.details.pos_x + 50 &&
                    data.clientY > use_case.details.pos_y - 50 &&
                    data.clientY < use_case.details.pos_y + 50) {
                    second = use_case;
                }
            });

            if (second) {
                $scope.second = second;
            }

            $scope.first.details.relations.push({
                type: $scope.relation_type,
                use_case: $scope.second
            });

            axios.put('/core/details');

            $scope.first            = null;
            $scope.second           = null;
            $scope.relation_active  = false;
            $scope.relation_type    = null;

            $scope.clearCanvas();
            $scope.drawRelations();
        },
        clearCanvas: function() {
            const canvas = document.querySelector('canvas');
            const ctx = canvas.getContext('2d');
            ctx.clearRect(0, 0, canvas.width, canvas.height);
        },
        drawRelations: function() {
            const $scope = this;

            const canvas = document.querySelector('canvas');
            const ctx = canvas.getContext('2d');

            $scope.use_cases.forEach((use_case) => {
                use_case.details.relations.forEach((relation) => {
                    ctx.beginPath();
                    ctx.moveTo(use_case.details.pos_x, use_case.details.pos_y);
                    ctx.lineTo(relation.use_case.details.pos_x, relation.use_case.details.pos_y);

                    ctx.font = "10px Comic Sans MS";
                    ctx.textAlign = "center";
                    ctx.fillText(relation.type, (use_case.details.pos_x + relation.use_case.details.pos_x) / 2, (use_case.details.pos_y + relation.use_case.details.pos_y) / 2);

                    ctx.stroke();
                });
            });
        },
        saveUseCase: function(data) {
            const $scope = this;

            let use_case = $scope.use_cases.filter((use_case) => {
                return use_case.id === data.use_case.id;
            })[0];

            use_case.details.text = data.data.details.text;
        },
        destroyUseCase: function(data) {
            const $scope = this;

            $scope.use_cases = $scope.use_cases.filter((use_case) => {
                return use_case.id !== data.id;
            });
        },
        saveDiagram: function() {
            this.use_cases.forEach((use_case) => {
                axios.put('/core/details', use_case).then((response) => {

                });
            });
        }
    }
}
</script>

<style scoped>
.canvas-wrap { position:relative; width:800px; height:600px }
.canvas-wrap canvas { position:absolute; top:0; left:0; z-index:0 }
</style>