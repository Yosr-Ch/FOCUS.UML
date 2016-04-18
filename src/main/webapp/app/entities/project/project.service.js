(function() {
    'use strict';
    angular
        .module('demoApp')
        .factory('Project', Project);

    Project.$inject = ['$resource', 'DateUtils'];

    function Project ($resource, DateUtils) {
        var resourceUrl =  'api/projects/:id/:action';

        return $resource(resourceUrl, {}, {
            'diagrams': { method: 'GET', isArray: true, params: {id:'@id', action: 'diagrams'}},
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
