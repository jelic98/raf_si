<template>
    <div>
        <Navbar></Navbar>

        <p class="title" style="padding-left: 50px; padding-top: 50px; padding-right: 50px">
            {{ model_name }}

            <b-dropdown class="is-pulled-right" >
            <template #trigger>
                <b-button
                @click = 'loadUsers'
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

        <div class="card">
        <!--  v-if="user && (user.role === 'admin' || user.role === 'project_manager')" -->
            <section class="card-content" style="padding: 50px">
                <b-field>
                    <b-radio-button v-for="state in states" :key="state.value" v-model="active_state" :native-value="state">
                        <span>{{ state.display  }}</span>
                    </b-radio-button>

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

                    <b-button style="margin-left: 10px" type='is-primary' @click='saveModel'>Save</b-button>
                </b-field>

                <b-message v-for="(error, key) in errors" :key="key" type="is-danger">
                    {{ error }}
                </b-message>
            </section>
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
                            <b-button @click="view(props.row.id)" class="is-small" type="is-primary">
                                View
                            </b-button>
                        </b-table-column>
                    </b-table>
                </section>
            </div>
        </b-modal>

        <b-modal :active.sync="viewModal" has-modal-card full-screen :can-cancel="false">
            <div class="modal-card">
                <header class="modal-card-head">
                    <p class="modal-card-title">{{currentRevision}}</p>
                    <button
                            type="button"
                            class="delete"
                            @click="()=>{viewModal=false; historyModal=true;}"/>
                </header>

                <section class="modal-card-body" style="padding: 40px">
                    <div id="view_revision" style='width: 80vw; height: 60vh;'>
                    </div>
                </section>
                <footer class="modal-card-foot">
                <div style="width:100%; text-align:center">
                    <b-button @click="history(props.row.id)" type="is-primary">
                                Restore
                            </b-button>
                </div>
                </footer>
            </div>
        </b-modal>

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
            working: false,
            currentRevision: null,
            node_key: 0,
            nodes: [],
            links: [],
            active_state: null,
            relation_active: false,
            relation_type: null,
            diagram: null,
            viewDiagram: null,
            viewNodes: [],
            viewLinks: [],
            user: [],
            users: [],
            historyModal: false,
            viewModal: false,
            revisions: [
                               {
                    author: "user1",
                    date:"1/1/1111",
                    id:"1234"
                },
                {
                    author: "user2",
                    date:"1/1/1112",
                    id:"1235"
                }
            ],
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
        this.user = JSON.parse(sessionStorage.getItem('auth-user'));
        this.loadModel();
        this.loadUsername();
    },
    methods: {
        loadUsername: function () {
                let jwt = JSON.parse(sessionStorage.getItem('auth-token'));
                if (jwt) {
                    axios.defaults.headers.common['Authorization'] = jwt;
                }
                axios.get('/auth/users/username', {}).then((response) => {
                    this.username = response.data;
                    console.log(this.username)
                }).catch((error) => {
                });
            },
        initView: function(){
            
            this.viewDiagram =
                $(go.Diagram, "view_revision",
                    {
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
                        { maxSize: new go.Size(60, 60) },
                        new go.Binding("source", "img")),
                    $(go.TextBlock,
                        {
                            editable: true,
                            margin: new go.Margin(3, 0, 0, 0),
                            maxSize: new go.Size(100, 30),
                            isMultiline: false
                        },
                        new go.Binding("text", "text")),
                    );

            let useCaseTemplate =
                $(go.Node, "Auto",  // the whole node panel
                    { locationSpot: go.Spot.Center },
                    new go.Binding('location', 'loc').makeTwoWay(),
                    // define the node's outer shape, which will surround the TextBlock
                    $(go.Shape, "Ellipse",
                        { fill: "rgba(245, 246, 247)", stroke: "black" }),
                    $(go.TextBlock,
                        { editable: true, font: "bold 11pt helvetica, bold arial, sans-serif", margin: 13 },
                        new go.Binding("text", "text")),
                    );

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

            this.viewDiagram.nodeTemplateMap = nodeTemplateMap;
            this.viewDiagram.linkTemplateMap = linkTemplateMap;

            this.viewDiagram.model = new go.GraphLinksModel(this.viewNodes, this.viewLinks);

        },
        view: function(id){

            this.currentRevision=id;

            let jwt = JSON.parse(sessionStorage.getItem('auth-token'));

            if (jwt) {
                axios.defaults.headers.common['Authorization'] = jwt;
            }

            // axios.get('/core/models', {
            //     params: {
            //         project: this.project_name,
            //         model: this.model_name
            //     }
            // }).then((response) => {
                this.viewNodes = [];
                this.viewLinks = [];

                // if (response.data) {let detalji = JSON.parse(response.data.details);
                //     this.viewNodes = detalji.nodes;
                //     this.viewLinks = detalji.links;
                // } 
                this.viewNodes=this.nodes;
                this.viewLinks = this.links;
                this.historyModal = false;
                this.viewModal = true;
                setTimeout(()=>{this.initView()}, 250)
                // this.initView();
                
            // }).catch((error) => {
            //     // this.initDiagram();
            // });

        },
        loadUsers: function () {

                //TODO get request za aktivne usere //ne bi se reklo da radi
                console.log("AAAAAAAAAAAAAAA");
                axios.get('/core/models/activeUsers', {
                    params: {
                        project: this.project_name,
                        model: this.model_name
                    }
                }).then((response) => {
                    console.log(response);
                    this.users = response.data;
                    //saljes mi listu usernames
                    console.log(this.users)
                }).catch((error) => {
                });


        },
        history: function(id) {
            //TODO request kojim se podesava verzija (restoruje se stara verzija i ona postaje najnovija), prosledju je joj se parametar id 
            this.load()
        },
        undo: function() {
            this.diagram.undoManager.undo();
            //this.diagram.updateAllTargetBindings();
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

                if (response.data) {let detalji = JSON.parse(response.data.details);
                    this.nodes = detalji.nodes;
                    this.links = detalji.links;
                    console.log(detalji)
                } else {
                    this.$router.push('/projects');
                }

                this.initDiagram();
            }).catch((error) => {
                this.initDiagram();
            });

            //TODO load history 
            axios.get('/', {
               
            }).then((response) => {
                
                this.revisions = response.data
                //saljes mi id, date i autora

            }).catch((error) => {
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
            console.log(this.nodes)
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
                        this.initDiagram();
                    }).catch((error) => {});
                }
            }).catch((error) => {});
        },
        initDiagram: function() {
            this.diagram =
                $(go.Diagram, "diagramDiv",
                    {
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
                        { maxSize: new go.Size(60, 60) },
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
                    $(go.Shape, "Ellipse",
                        { fill: "rgba(245, 246, 247)", stroke: "black" }),
                    $(go.TextBlock,
                        { editable: true, font: "bold 11pt helvetica, bold arial, sans-serif", margin: 13 },
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
            console.log(this.nodes)
            console.log(this.links)

            this.diagram.model = new go.GraphLinksModel(this.nodes, this.links);

            this.diagram.addDiagramListener('TextEdited', (e) => {
                let node = this.nodes.filter((node) => {
                    return node.key === e.subject.part.key;
                })[0];

                node.text = e.subject.text;
            });

            this.diagram.addDiagramListener('BackgroundSingleClicked', this.createNode);
            this.diagram.addDiagramListener('LinkDrawn', this.createLink);
            this.diagram.addDiagramListener('ChangedSelection', this.selected);
        },
        refresh: function(){

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
                // this.nodes = [];
                // this.links = [];

                if (response.data) {let detalji = JSON.parse(response.data.details);
                    // this.nodes = detalji.nodes;
                    // this.links = detalji.links;
                    console.log(detalji)
                    
                    let nodes = []
                    let links = []
                    detalji.nodes.forEach((node)=>{
                        nodes.push({
                            category: node.category,
                            img: node.img,
                            text: node.text,
                            editing: node.editing,
                            key: node.key,
                            loc: new go.Point(node.loc.x, node.loc.y)
                        })
                    })
                    this.diagram.clear();
                     this.diagram.model.addNodeDataCollection(nodes);
                     this.diagram.model.addLinkDataCollection(detalji.links);
                } else {
                    this.$router.push('/projects');
                }

                // this.initDiagram();
            }).catch((error) => {
                // this.initDiagram();
            });

        },
        selected: function(e){

            if (e.diagram.selection.size <= 0) {
                    console.log("deselektovano");
                    axios.get('/core/models/deselect', {
                        params: {
                            project: this.project_name,
                            model: this.model_name,
                            user: this.username
                        }
                    });
                    if(this.working){
                        this.working =false
                    }
                    else{
                        this.refresh();
                    }
                    
                } else {
                    e.diagram.selection.each((node) => {
                        if (!(node instanceof go.Node)) {
                            return;
                        }
                        // this.selectedNodes.push(node.data);
                        axios.get('/core/models/select', {
                            params: {
                                project: this.project_name,
                                model: this.model_name,
                                nodeId: node.data.key,
                                user: this.username
                            }
                        }).then((response) => {
                            console.log(response.data);
                            if (!response.data) {
                                e.diagram.clearSelection();
                            } else {
                                console.log("loading Period");
                                // this.loadPeriodically();
                            }
                        }).catch((error) => {
                            console.log(error);
                        });

                    });

                }
            
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
            if (this.active_state && (this.active_state.value === 'use_case' || this.active_state.value === 'actor')) {
                this.working=true;
                // setTimeout(()=>{this.working=false}, 1000)
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
            this.working=true;
            this.diagram.model.setDataProperty(e.subject.data, 'category', this.active_state.category);
            this.diagram.model.setDataProperty(e.subject.data, 'text', this.active_state.text);
            this.diagram.model.setDataProperty(e.subject.data, 'relationship', this.active_state.value);
            this.active_state = null;
        }
    }
}
</script>

<style scoped>
.canvas-wrap { position:relative; width:800px; height:600px }
.canvas-wrap canvas { position:absolute; top:0; left:0; z-index:0 }
</style>

