(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('DiagramDeleteController',DiagramDeleteController);

    DiagramDeleteController.$inject = ['$uibModalInstance', 'entity', 'Diagram'];

    function DiagramDeleteController($uibModalInstance, entity, Diagram) {
        var vm = this;
        vm.diagram = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Diagram.delete({diagId: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
