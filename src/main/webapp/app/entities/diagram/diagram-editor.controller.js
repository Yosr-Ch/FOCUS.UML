(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('DiagramEditorController', DiagramEditorController);

    DiagramEditorController.$inject = ['$scope', '$state', '$stateParams', 'Diagram', 'entity', 'DiagramSearch', 'ParseLinks', 'AlertService', 'Project'];

    function DiagramEditorController ($scope, $state, $stateParams, Diagram, entity, DiagramSearch, ParseLinks, AlertService, Project) {
        var vm = this;

        vm.width = window.innerWidth - 250;

        if(vm.width < 400) vm.width = 400;
        if(vm.width > 950) vm.width = 950;
        vm.app = new Application( { id: 'umldiagram', width: vm.width, height: 580 } );

        $scope.myFunc = function() {
            //vm.testxml = vm.app.getCurrentXMLString();
            var x2js = new X2JS();
            var xmlText = vm.app.getCurrentXMLString();
            $scope.jsonObj = x2js.xml_str2json( xmlText );
            console.log("teeeeeeeeeeeeest here !!  "+JSON.stringify(jsonObj));
            $scope.xmltojs = JSON.stringify(jsonObj);
        };

        var vm = this;
        vm.diagram = entity;
        //vm.projects = Project.query();
        vm.load = function(id) {
            Diagram.get({id : id}, function(result) {
                vm.diagram = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('demoApp:diagramUpdate', result);
            //$uibModalInstance.close(result);
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
                vm.diagram.name = 'tessssssssssst';
                //var x2js = new X2JS();
                var xmlText = vm.app.getCurrentXMLString();
                //var jsonObj = x2js.xml_str2json( xmlText );
                //$scope.xmltojs = JSON.stringify(jsonObj);
                //vm.diagram.content = JSON.stringify(jsonObj);
                vm.diagram.content = xmlText;
                vm.diagram.prjId = $stateParams.prjId;
                 console.log('  jdhflksjh dflkfjs hlkjsdh ' + $stateParams.prjId);
                Project.addDiagram(vm.diagram, onSaveSuccess, onSaveError);
            }
        };

        /*vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };*/
    }

})();

/**
 * Created by yosr on 4/18/16.
 */
