(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('ProjectDialogController', ProjectDialogController);

    ProjectDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Project', 'User', 'Diagram'];

    function ProjectDialogController ($scope, $stateParams, $uibModalInstance, entity, Project, User, Diagram) {
        var vm = this;
        vm.project = entity;
        vm.users = User.query();
        vm.diagrams = Diagram.query();
        vm.load = function(id) {
            Project.get({id : id}, function(result) {
                vm.project = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('demoApp:projectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.project.id !== null) {
                Project.update(vm.project, onSaveSuccess, onSaveError);
            } else {
                Project.save(vm.project, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.creation_date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
