(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('DiagramEditorController', DiagramEditorController);

    DiagramEditorController.$inject = ['$scope', '$state', 'Diagram', 'DiagramSearch', 'ParseLinks', 'AlertService'];

    function DiagramEditorController ($scope, $state, Diagram, DiagramSearch, ParseLinks, AlertService) {
        var vm = this;

        vm.width = window.innerWidth - 250;

        if(vm.width < 400) vm.width = 400;
        if(vm.width > 1000) vm.width = 1000;
        vm.app = new Application( { id: 'umldiagram', width: 950, height: 580 } );


        /*vm.diagrams = [];
        vm.predicate = 'id';
        vm.reverse = true;
        vm.page = 0;
        vm.loadAll = function() {
            if (vm.currentSearch) {
                DiagramSearch.query({
                    query: vm.currentSearch,
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Diagram.query({
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.diagrams.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };
        vm.reset = function() {
            vm.page = 0;
            vm.diagrams = [];
            vm.loadAll();
        };
        vm.loadPage = function(page) {
            vm.page = page;
            vm.loadAll();
        };

        vm.search = function (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.diagrams = [];
            vm.links = null;
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.loadAll();
        };

        vm.clear = function () {
            vm.diagrams = [];
            vm.links = null;
            vm.page = 0;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.searchQuery = null;
            vm.currentSearch = null;
            vm.loadAll();
        };

        vm.loadAll();*/

    }
})();

/**
 * Created by yosr on 4/18/16.
 */
