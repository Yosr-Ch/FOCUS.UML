(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('DiagramDetailController', DiagramDetailController);

    DiagramDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Diagram', 'Project'];

    function DiagramDetailController($scope, $rootScope, $stateParams, entity, Diagram, Project) {
        var vm = this;
        vm.diagram = entity;
        vm.load = function (id) {
            Diagram.get({id: id}, function(result) {
                vm.diagram = result;
            });
        };
        var unsubscribe = $rootScope.$on('demoApp:diagramUpdate', function(event, result) {
            vm.diagram = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
