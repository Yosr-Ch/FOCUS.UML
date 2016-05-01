(function() {
    'use strict';
    angular
        .module('demoApp')
        .factory('Project', Project);

    Project.$inject = ['$resource', 'DateUtils'];

    function Project ($resource, DateUtils) {
        var resourceUrl =  'api/projects/:prjId/:action/:diagId';

        return $resource(resourceUrl, {}, {
            'addDiagram': { method: 'POST', params: {prjId:'@prjId', action: 'diagrams'}},
            'getPrj': {
                method: 'GET',
                params: {prjId:'@prjId'},
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creation_date = DateUtils.convertDateTimeFromServer(data.creation_date);
                    return data;
                }
            },
            'diagrams': { method: 'GET', isArray: true, params: {prjId:'@prjId', action: 'diagrams'}},
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                //params: {prjId:'@prjId'},
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creation_date = DateUtils.convertDateTimeFromServer(data.creation_date);
                    return data;
                }
            },
            'editDiagram': { method: 'PUT', params: {prjId:'@prjId', action: 'diagrams', diagId:'@diagId'}},
            'update': { method:'PUT' }
        });
    }
})();
