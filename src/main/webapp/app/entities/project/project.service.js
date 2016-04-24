(function() {
    'use strict';
    angular
        .module('demoApp')
        .factory('Project', Project);

    Project.$inject = ['$resource', 'DateUtils'];

    function Project ($resource, DateUtils) {
        var resourceUrl =  'api/projects/:prjId/:action';

        return $resource(resourceUrl, {}, {
            'addDiagram': { method: 'POST', params: {prjId:'@prjId', action: 'diagrams'}},
            'diagrams': { method: 'GET', isArray: true, params: {prjId:'@prjId', action: 'diagrams'}},
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creation_date = DateUtils.convertDateTimeFromServer(data.creation_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
