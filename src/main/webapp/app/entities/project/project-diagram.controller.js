(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('ProjectDiagramsController', ProjectDiagramsController);

    ProjectDiagramsController.$inject = ['$scope', '$rootScope', '$stateParams', 'diagrams', 'Project', 'User', 'Diagram'];

    function ProjectDiagramsController($scope, $rootScope, $stateParams, diagrams, Project, User, Diagram) {
        var vm = this;
        vm.diagrams = diagrams;
        vm.load = function (id) {
            Project.diagrams({id: id}, function(result) {
                vm.diagrams = result;
            });
        };
        var unsubscribe = $rootScope.$on('demoApp:projectUpdate', function(event, result) {
            vm.diagrams = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
