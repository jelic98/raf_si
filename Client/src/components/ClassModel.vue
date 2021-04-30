<template>
    <div>
        <Navbar></Navbar>
        <b-button type='is-primary' @click='saveModel'>Save</b-button>
        <div id='paletteDiv' style='width: 100vw; height: 10vh;'>
            <b-button @click='setState("class")'>Class</b-button>
            <b-button @click='setState("generalization")'>Generalization</b-button>
            <b-button @click='setState("aggregation")'>Aggregation</b-button>
            <b-button @click='setState("composition")'>Composition</b-button>
        </div>
        <div id='diagramDiv' style='width: 100vw; height: 90vh;'></div>
      </div>
</template>
<script>
import Navbar from "./Navbar";
import go from 'gojs';
var $ = go.GraphObject.make;
export default {
    name: 'ClassModel',
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
    data: function() {
        return {
            nodes: [],
            diagram: null,
            nodeKey: 0,
            state: 'class'
        };
    },
    mounted: function() {
        this.loadModel();
        this.initDiagram();
    },
    methods: {
        setState: function(state) {
            this.state = state;
        },
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
        loadModel: function() {
               var json = '[{"key":0,"loc":{"x":125,"y":-178.09375,"s":true},"name":"Professor","properties":[{"name":"classes","type":"List<Course>","visibility":"public","__gohashid":629}],"methods":[{"name":"teach","parameters":[{"name":"class","type":"Course"}],"visibility":"private","__gohashid":651}],"__gohashid":567}]'; 
            this.nodes = JSON.parse(json);
        },
        saveModel: function() {
            console.log(this.nodes);
            console.log(JSON.stringify(this.nodes));
        },
        createNode: function(e) {
            switch (this.state) {
                case 'class':
                    var node = {
                        key: this.nodeKey++,
                        loc: e.diagram.lastInput.documentPoint,
                        name: 'Professor',
                        properties: [{
                            name: 'classes',
                            type: 'List<Course>',
                            visibility: 'public'
                        }],
                        methods: [{
                            name: 'teach',
                            parameters: [{
                                name: 'class',
                                type: 'Course'
                            }],
                            visibility: 'private'
                        }]
                    };
                    this.nodes.push(node);
                    this.diagram.model.addNodeData(node);
                    break;
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
        initDiagram: function() {
            var propertyTemplate =
            $(go.Panel, 'Horizontal',
                $(go.TextBlock, {
                    isMultiline: false,
                    editable: false,
                    width: 12
                }),
                $(go.TextBlock, { isMultiline: false, editable: true },
                new go.Binding('text', 'name').makeTwoWay(),
                new go.Binding('isUnderline', 'scope', function(s) { return s[0] === 'c'; })),
                $(go.TextBlock, '', new go.Binding('text', 'type', function(t) { return (t ? ': ' : ''); })),
                $(go.TextBlock, { isMultiline: false, editable: true },
                new go.Binding('text', 'type').makeTwoWay()),
                $(go.TextBlock, { isMultiline: false, editable: false },
                new go.Binding('text', 'default', function(s) { return s ? ' = ' + s : ''; })),
                new go.Binding('text', 'visibility', this.convertVisibility));

        var methodTemplate =
            $(go.Panel, 'Horizontal',
                $(go.TextBlock, {
                        isMultiline: false,
                        editable: false,
                        width: 12
                    },
                    new go.Binding('text', 'visibility', this.convertVisibility)),
                $(go.TextBlock, { isMultiline: false, editable: true },
                new go.Binding('text', 'name').makeTwoWay(),
                new go.Binding('isUnderline', 'scope', function(s) { return s[0] === 'c'; })),
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
                $(go.TextBlock, '', new go.Binding('text', 'type', function(t) { return (t ? ': ' : ''); })),
                $(go.TextBlock, { isMultiline: false, editable: true },
                new go.Binding('text', 'type').makeTwoWay())
            );

        this.diagram =
            $(go.Diagram, 'diagramDiv', {
                'undoManager.isEnabled': true,
                'draggingTool.dragsLink': true,
                'draggingTool.isGridSnapEnabled': true,
                'linkingTool.isUnconnectedLinkValid': true,
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
                    $(go.Shape, { fill: 'lightyellow' }),
                    $(go.Panel, 'Table', { defaultRowSeparatorStroke: 'black' },
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
                        $(go.TextBlock, 'Properties', { row: 1, font: 'italic 10pt sans-serif' },
                        new go.Binding('visible', 'visible', function(v) { return !v; }).ofObject('PROPERTIES')),
                        $(go.Panel, 'Vertical', { name: 'PROPERTIES' },
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
                            new go.Binding('visible', 'properties', function(arr) { return arr.length > 0; })),
                        $(go.TextBlock, 'Methods', { row: 2, font: 'italic 10pt sans-serif' },
                            new go.Binding('visible', 'visible', function(v) { return !v; }).ofObject('METHODS')),
                        $(go.Panel, 'Vertical', { name: 'METHODS' },
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
                            new go.Binding('visible', 'methods', function(arr) { return arr.length > 0; }))
                    ),
                    this.makePort('T', go.Spot.Top),
                    this.makePort('R', go.Spot.Right),
                    this.makePort('B', go.Spot.Bottom),
                    this.makePort('L', go.Spot.Left), {
                        mouseEnter: function(e, node) { node.ports.each(function(p) { p.fill = 'rgba(0,0,0,.3)'; });},
                        mouseLeave: function(e, node) { node.ports.each(function(p) { p.fill = null; });}
                    }),
                linkTemplate: $(go.Link,
                    new go.Binding('points').makeTwoWay(),
                    $(go.Shape, { isPanelMain: true, strokeWidth: 1 }),
                    $(go.Shape, { toArrow: 'Standard', stroke: null }),
                    $(go.Panel, 'Auto',
                        new go.Binding('visible', 'isSelected').ofObject(),
                        $(go.Shape, 'RoundedRectangle', { fill: '#F8F8F8', stroke: null }),
                        $(go.TextBlock, {
                                textAlign: 'center',
                                font: '10pt helvetica, arial, sans-serif',
                                stroke: '#919191',
                                margin: 2,
                                minSize: new go.Size(10, NaN),
                                editable: true
                            },
                            new go.Binding('text').makeTwoWay())
                    )
                )
            });
            this.diagram.model = $(go.GraphLinksModel);
			this.diagram.model.nodeDataArray = this.nodes;
            this.diagram.addDiagramListener('BackgroundSingleClicked', this.createNode);
        }
    }
}
</script>
