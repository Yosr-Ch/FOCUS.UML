(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('DiagramDeleteController',DiagramDeleteController);

    DiagramDeleteController.$inject = ['$uibModalInstance', 'entity', '$stateParams', 'Diagram', 'Project'];

    function DiagramDeleteController($uibModalInstance, entity, $stateParams, Diagram, Project) {
        var vm = this;
        vm.diagram = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.diagram.prjId = $stateParams.prjId;
        //vm.diagram.id = $stateParams.diagId;
        console.log("amaaaaaaaaane!!",$stateParams.diagId)
        vm.confirmDelete = function (prjId,diagId) {
            //Diagram.delete({prjId: pId},{diagId: dId},
            Project.deleteDiagram({prjId: prjId, diagId: diagId},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
