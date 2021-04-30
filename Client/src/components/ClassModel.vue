<template>
<div id="myDiagramDiv"
style="width:100vw; height:100vh"></div>
</template>
<script>
import go from 'gojs';
var $ = go.GraphObject.make;
export default {
    name: 'Diagram',
    mounted() {
        var myDiagram =
            $(go.Diagram, "myDiagramDiv", {
                "undoManager.isEnabled": true,
                layout: $(go.TreeLayout, {
                    angle: 90,
                    path: go.TreeLayout.PathSource,
                    setsPortSpot: false,
                    setsChildPortSpot: false,
					isInitial: false,
					isOngoing: false,
                    arrangement: go.TreeLayout.ArrangementHorizontal
                })
            });

        function convertVisibility(v) {
            switch (v) {
                case "public":
                    return "+";
                case "private":
                    return "-";
                case "protected":
                    return "#";
                case "package":
                    return "~";
                default:
                    return v;
            }
        }

        function convertIsTreeLink(r) {
            return r === "generalization";
        }

        function convertFromArrow(r) {
            switch (r) {
                case "generalization":
                    return "";
                default:
                    return "";
            }
        }

        function convertToArrow(r) {
            switch (r) {
                case "generalization":
                    return "Triangle";
                case "aggregation":
                    return "StretchedDiamond";
                default:
                    return "";
            }
        }

        var propertyTemplate =
            $(go.Panel, "Horizontal",
                $(go.TextBlock, {
                        isMultiline: false,
                        editable: false,
                        width: 12
                    }),
                $(go.TextBlock, {
                        isMultiline: false,
                        editable: true
                    },
                    new go.Binding("text", "name").makeTwoWay(),
                    new go.Binding("isUnderline", "scope", function(s) {
                        return s[0] === 'c'
                    })),
                $(go.TextBlock, "",
                    new go.Binding("text", "type", function(t) {
                        return (t ? ": " : "");
                    })),
                $(go.TextBlock, {
                        isMultiline: false,
                        editable: true
                    },
                    new go.Binding("text", "type").makeTwoWay()),
                $(go.TextBlock, {
                        isMultiline: false,
                        editable: false
                    },
                    new go.Binding("text", "default", function(s) {
                        return s ? " = " + s : "";
                    })),
                new go.Binding("text", "visibility", convertVisibility));

        var methodTemplate =
            $(go.Panel, "Horizontal",
                $(go.TextBlock, {
                        isMultiline: false,
                        editable: false,
                        width: 12
                    },
                    new go.Binding("text", "visibility", convertVisibility)),
                $(go.TextBlock, {
                        isMultiline: false,
                        editable: true
                    },
                    new go.Binding("text", "name").makeTwoWay(),
                    new go.Binding("isUnderline", "scope", function(s) {
                        return s[0] === 'c'
                    })),
                $(go.TextBlock, "()",
                    new go.Binding("text", "parameters", function(parr) {
                        var s = "(";
                        for (var i = 0; i < parr.length; i++) {
                            var param = parr[i];
                            if (i > 0) s += ", ";
                            s += param.name + ": " + param.type;
                        }
                        return s + ")";
                    })),
                $(go.TextBlock, "",
                    new go.Binding("text", "type", function(t) {
                        return (t ? ": " : "");
                    })),
                $(go.TextBlock, {
                        isMultiline: false,
                        editable: true
                    },
                    new go.Binding("text", "type").makeTwoWay())
            );

        myDiagram.nodeTemplate =
            $(go.Node, "Auto", {
                    locationSpot: go.Spot.Center,
                    fromSpot: go.Spot.AllSides,
                    toSpot: go.Spot.AllSides
                },
				new go.Binding("location", "loc").makeTwoWay(),
                $(go.Shape, {
                    fill: "lightyellow"
                }),
                $(go.Panel, "Table", {
                        defaultRowSeparatorStroke: "black"
                    },
                    $(go.TextBlock, {
                            row: 0,
                            columnSpan: 2,
                            margin: 3,
                            alignment: go.Spot.Center,
                            font: "bold 12pt sans-serif",
                            isMultiline: false,
                            editable: true
                        },
                        new go.Binding("text", "name").makeTwoWay()),
                    $(go.TextBlock, "Properties", {
                            row: 1,
                            font: "italic 10pt sans-serif"
                        },
                        new go.Binding("visible", "visible", function(v) {
                            return !v;
                        }).ofObject("PROPERTIES")),
                    $(go.Panel, "Vertical", {
                            name: "PROPERTIES"
                        },
                        new go.Binding("itemArray", "properties"), {
                            row: 1,
                            margin: 3,
                            stretch: go.GraphObject.Fill,
                            defaultAlignment: go.Spot.Left,
                            background: "lightyellow",
                            itemTemplate: propertyTemplate
                        }
                    ),
                    $("PanelExpanderButton", "PROPERTIES", {
                            row: 1,
                            column: 1,
                            alignment: go.Spot.TopRight,
                            visible: false
                        },
                        new go.Binding("visible", "properties", function(arr) {
                            return arr.length > 0;
                        })),
                    $(go.TextBlock, "Methods", {
                            row: 2,
                            font: "italic 10pt sans-serif"
                        },
                        new go.Binding("visible", "visible", function(v) {
                            return !v;
                        }).ofObject("METHODS")),
                    $(go.Panel, "Vertical", {
                            name: "METHODS"
                        },
                        new go.Binding("itemArray", "methods"), {
                            row: 2,
                            margin: 3,
                            stretch: go.GraphObject.Fill,
                            defaultAlignment: go.Spot.Left,
                            background: "lightyellow",
                            itemTemplate: methodTemplate
                        }
                    ),
                    $("PanelExpanderButton", "METHODS", {
                            row: 2,
                            column: 1,
                            alignment: go.Spot.TopRight,
                            visible: false
                        },
                        new go.Binding("visible", "methods", function(arr) {
                            return arr.length > 0;
                        }))
                )
            );

		myDiagram.addDiagramListener("BackgroundSingleClicked", addNode);

        myDiagram.linkTemplate =
            $(go.Link, {
                    routing: go.Link.Orthogonal
                },
                new go.Binding("isLayoutPositioned", "relationship", convertIsTreeLink),
                $(go.Shape),
                $(go.Shape, {
                        scale: 1.3,
                        fill: "white"
                    },
                    new go.Binding("fromArrow", "relationship", convertFromArrow)),
                $(go.Shape, {
                        scale: 1.3,
                        fill: "white"
                    },
                    new go.Binding("toArrow", "relationship", convertToArrow))
            );

        var nodedata = [{
                key: 1,
                name: "BankAccount",
                properties: [{
                        name: "owner",
                        type: "String",
                        visibility: "public"
                    },
                    {
                        name: "balance",
                        type: "Currency",
                        visibility: "public",
                        default: "0"
                    }
                ],
                methods: [{
                        name: "deposit",
                        parameters: [{
                            name: "amount",
                            type: "Currency"
                        }],
                        visibility: "public"
                    },
                    {
                        name: "withdraw",
                        parameters: [{
                            name: "amount",
                            type: "Currency"
                        }],
                        visibility: "public"
                    }
                ]
            },
            {
                key: 11,
                name: "Person",
                properties: [{
                        name: "name",
                        type: "String",
                        visibility: "public"
                    },
                    {
                        name: "birth",
                        type: "Date",
                        visibility: "protected"
                    }
                ],
                methods: [{
                    name: "getCurrentAge",
                    type: "int",
                    visibility: "public"
                }]
            },
            {
                key: 12,
                name: "Student",
                properties: [{
                    name: "classes",
                    type: "List<Course>",
                    visibility: "public"
                }],
                methods: [{
                        name: "attend",
                        parameters: [{
                            name: "class",
                            type: "Course"
                        }],
                        visibility: "private"
                    },
                    {
                        name: "sleep",
                        visibility: "private"
                    }
                ]
            },
            {
                key: 13,
                name: "Professor",
                properties: [{
                    name: "classes",
                    type: "List<Course>",
                    visibility: "public"
                }],
                methods: [{
                    name: "teach",
                    parameters: [{
                        name: "class",
                        type: "Course"
                    }],
                    visibility: "private"
                }]
            },
            {
                key: 14,
                name: "Course",
                properties: [{
                        name: "name",
                        type: "String",
                        visibility: "public"
                    },
                    {
                        name: "description",
                        type: "String",
                        visibility: "public"
                    },
                    {
                        name: "professor",
                        type: "Professor",
                        visibility: "public"
                    },
                    {
                        name: "location",
                        type: "String",
                        visibility: "public"
                    },
                    {
                        name: "times",
                        type: "List<Time>",
                        visibility: "public"
                    },
                    {
                        name: "prerequisites",
                        type: "List<Course>",
                        visibility: "public"
                    },
                    {
                        name: "students",
                        type: "List<Student>",
                        visibility: "public"
                    }
                ]
            }
        ];
        var linkdata = [{
                from: 12,
                to: 11,
                relationship: "generalization"
            },
            {
                from: 13,
                to: 11,
                relationship: "generalization"
            },
            {
                from: 14,
                to: 13,
                relationship: "aggregation"
            }
        ];
        myDiagram.model = $(go.GraphLinksModel, {
            copiesArrays: true,
            copiesArrayObjects: true,
            nodeDataArray: nodedata,
            linkDataArray: linkdata
        });
		function addNode(e) {
			let point = e.diagram.lastInput.documentPoint;
			myDiagram.model.addNodeData({
				loc: point,
                key: 15,
                name: "Random",
                properties: [{
                        name: "name",
                        type: "String",
                        visibility: "public"
                    }
                ]
            });
		}
    }
}
</script>
