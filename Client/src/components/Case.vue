<template>
    <div :id="use_case.id" :draggable="true" style="position: absolute; margin: auto; height: 100px; width: 100px; background-color: #b199c4">
        <p>{{ use_case.details.text }}</p>
        <b-button type="is-danger" @click="destroy">Delete</b-button>

        <b-modal
            title="Manage"
            :active.sync="modal_open"
            has-modal-card>
            <div class="modal-card">
                <header class="modal-card-head">
                    Edit Use Case
                </header>
                <section class="modal-card-body">
                    <b-input v-model="form.text"/>
                </section>
                <footer class="modal-card-foot">
                    <b-button @click="save">Save</b-button>
                </footer>
            </div>
        </b-modal>
    </div>
</template>

<script>
export default {
    name: "Case",
    props: {
        use_case: {
            required: true
        },
        relation_active: {
            required: true,
            type: Boolean
        }
    },
    data: function() {
        return {
            modal_open: false,
            form: {
                text: this.use_case.details.text
            },
            id: null,
            posX: null,
            posY: null,
            relations: []
        }
    },
    mounted: function() {
        const $scope = this;

        let draggable = document.getElementById($scope.use_case.id);

        draggable.addEventListener('dragstart', function(e) {
            if ($scope.relation_active) {
                $scope.$emit('setFirst', $scope.use_case);
            }
        });

        draggable.addEventListener('dragend', function(e) {
            if ($scope.relation_active) {
                $scope.$emit('setSecond', e);
            } else {
                $scope.drag(e);
            }
        });

        draggable.addEventListener('dblclick', function(e) {
            $scope.modal_open = true;
        });
    },
    methods: {
        drag: function(e) {
            const $scope = this;

            let draggable = document.getElementById($scope.use_case.id);

            let width = parseInt(draggable.style.width.substr(0, draggable.style.width.length - 2));
            let height = parseInt(draggable.style.height.substr(0, draggable.style.height.length - 2));

            draggable.style.left = `${e.clientX - width / 2}px`;
            draggable.style.top = `${e.clientY - height / 2}px`;

            $scope.$emit('dragged', {
                event: e,
                id: $scope.use_case.id
            });
        },
        save: function() {
            const $scope = this;

            $scope.$emit('saveUseCase', {
                use_case: $scope.use_case,
                data: $scope.form
            });

            $scope.modal_open = false;
        },
        destroy: function() {
            const $scope = this;

            $scope.$destroy();

            $scope.$el.parentNode.removeChild($scope.$el);

            $scope.$emit('destroyUseCase', $scope.use_case)
        }
    }
}
</script>

<style scoped>

</style>