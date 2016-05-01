(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Project', 'User', 'Diagram'];

    function ProjectDetailController($scope, $rootScope, $stateParams, entity, Project, User, Diagram) {
        var vm = this;
        vm.project = entity;
        vm.load = function (id) {
            Project.getPrj({prjId: id}, function(result) {
                vm.project = result;
            });
        };
        var unsubscribe = $rootScope.$on('demoApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
