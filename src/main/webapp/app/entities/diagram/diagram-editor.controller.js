(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('DiagramEditorController', DiagramEditorController);

    DiagramEditorController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'Diagram', 'entity', 'DiagramSearch', 'ParseLinks', 'AlertService', 'Project'];

    function DiagramEditorController ($rootScope, $scope, $state, $stateParams, Diagram, entity, DiagramSearch, ParseLinks, AlertService, Project) {
        var vm = this;
        vm.diagram = entity;
        //vm.diagIsValid = true;

        vm.width = window.innerWidth - 250;

        if(vm.width < 400) vm.width = 400;
        if(vm.width > 950) vm.width = 950;
        vm.app = new Application( { id: 'umldiagram', width: vm.width, height: 580 } );

        //vm.diagram = angular.copy(entity);
        console.log('entity' +JSON.stringify(entity));
        console.log('teeeeeeeeeestt' +JSON.stringify(vm.diagram));
        if (vm.diagram && vm.diagram.id !== null){
            vm.app.setXMLString(vm.diagram.content);
            vm.diagram.name = vm.app._diagrams[vm.app._selected].getName();
        }
        /*if (vm.diagram.id !== null) {
         //vm.reload();
         vm.load($stateParams.diagId);
         //$state.go('diagram-editor.edit', {prjId:$stateParams.prjId, diagId:$stateParams.diagId}, {reload: true});
         vm.app.setXMLString(vm.diagram.content);

         }*/

        //vm.reload();
        /*vm.loadDiag = function(){

         location.reload();
         console.log('9a3ed y3aytelha !!!!!!!!!!')
         if (vm.diagram.id !== null) {
         //vm.reload();
         vm.load($stateParams.diagId);
         //$state.go('diagram-editor.edit', {prjId:$stateParams.prjId, diagId:$stateParams.diagId}, {reload: true});
         vm.app.setXMLString(vm.diagram.content);
         }

         }*/

        var unsubscribe = $rootScope.$on('demoApp:diagramUpdate', function(event, result) {
            vm.diagram = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.myFunc = function() {
            //vm.testxml = vm.app.getCurrentXMLString();
            /*var x2js = new X2JS();
             var xmlText = vm.app.getCurrentXMLString();
             $scope.jsonObj = x2js.xml_str2json( xmlText );
             console.log("teeeeeeeeeeeeest here !!  "+JSON.stringify(jsonObj));
             $scope.xmltojs = JSON.stringify(jsonObj);*/
            //vm.app.setXMLString(vm.diagram.content);
            if (vm.diagram.id !== null) {
                vm.diagram.prjId = $stateParams.prjId;
                vm.diagram.id = $stateParams.diagId;

                console.log('raw fel valiiiiiiiiiiiidaaaaate');
                vm.diagram.content = vm.app.getCurrentXMLString();
            Project.validateDiagram(vm.diagram, onSaveSuccess, onSaveError);
            }
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('demoApp:diagramUpdate', result);
            //$uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        var onValidateSuccess = function () {
            $scope.$emit('demoApp:diagramUpdate', result);
            vm.diagIsValid = false;
        };
        var onValidateError = function () {
            $scope.$emit('demoApp:diagramUpdate', result);
            vm.diagIsValid = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.diagram.id !== null) {
                //vm.diagram.prjId = $stateParams.prjId;
                vm.diagram.prjId = $stateParams.prjId;
                vm.diagram.id = $stateParams.diagId;

                // vm.diagram.name='same as other';
                console.log('raw fel updaaaaaaaate');
                vm.diagram.content = vm.app.getCurrentXMLString();
                Project.editDiagram(vm.diagram, onSaveSuccess, onSaveError);
            } else {
                console.log (' $stateParams.prjId  ' +  $stateParams.prjId );
                console.log (' vm.diagram.prjId  ' +  vm.diagram.prjId );

                vm.diagram.name = vm.app._diagrams[vm.app._selected].getName();
                //var x2js = new X2JS();
                //var xmlText = vm.app.getCurrentXMLString();
                //var jsonObj = x2js.xml_str2json( xmlText );
                //$scope.xmltojs = JSON.stringify(jsonObj);
                //vm.diagram.content = JSON.stringify(jsonObj);
                vm.diagram.content = vm.app.getCurrentXMLString();
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
