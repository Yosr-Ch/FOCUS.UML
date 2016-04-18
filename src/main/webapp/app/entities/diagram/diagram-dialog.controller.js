(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('DiagramDialogController', DiagramDialogController);

    DiagramDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Diagram', 'Project'];

    function DiagramDialogController ($scope, $stateParams, $uibModalInstance, entity, Diagram, Project) {
        var vm = this;
        vm.diagram = entity;
        vm.projects = Project.query();
        vm.load = function(id) {
            Diagram.get({id : id}, function(result) {
                vm.diagram = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('demoApp:diagramUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.diagram.id !== null) {
                Diagram.update(vm.diagram, onSaveSuccess, onSaveError);
            } else {
                Diagram.save(vm.diagram, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
