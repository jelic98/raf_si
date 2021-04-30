<template>
<!--        <Navbar></Navbar>-->
    <div class="canvas-wrap">
        <canvas width="1200" height="937"></canvas>

        <div class="overlay">
            <b-button type="is-primary" @click="addUseCase">
                New
            </b-button>

            <b-button type="is-primary" @click="() => { this.relation_active = true; this.relation_type = 'extends' }">
                Extends
            </b-button>

            <b-button type="is-primary" @click="() => { this.relation_active = true; this.relation_type = 'includes' }">
                Includes
            </b-button>

            <b-button type="is-primary" @click="() => { this.relation_active = true; this.relation_type = 'generalization' }">
                Generalization
            </b-button>

            <b-button type="is-success" @click="saveDiagram">
                Save
            </b-button>

            <Case v-for="(use_case, index) in use_cases"
                  :key="index"
                  :ref="`draggable-${index}`"
                  :use_case="use_case"
                  @dragged="updatePosition"
                  @setFirst="setFirst"
                  @setSecond="setSecond"
                  @saveUseCase="saveUseCase"
                  @destroyUseCase="destroyUseCase"
                  :relation_active="relation_active">
            </Case>
        </div>
    </div>
</template>

<script>
import Case from "./Case";
export default {
    name: "UseCase",
    components: {Case},
    props: {
        model: {
            required: true
        },
        project: {
            required: true
        }
    },
    data: function() {
        return {
            relation_active: false,
            relation_type: null,
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
    },
    methods: {
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