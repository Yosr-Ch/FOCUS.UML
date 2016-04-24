(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('ProjectDiagramsController', ProjectDiagramsController);

    ProjectDiagramsController.$inject = ['$scope', '$rootScope', '$stateParams', 'diagrams', 'Project', 'User', 'Diagram'];

    function ProjectDiagramsController($scope, $rootScope, $stateParams, diagrams, Project, User, Diagram) {
        var vm = this;
        console.log('bbbbbbbbbbbb'+$stateParams.prjId);
        vm.projectId = $stateParams.prjId;
        vm.diagrams = diagrams;
        vm.load = function (prjId) {
            Project.diagrams({prjId: prjId}, function(result) {
                vm.diagrams = result;
            });
        };
        var unsubscribe = $rootScope.$on('demoApp:projectUpdate', function(event, result) {
            vm.diagrams = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
