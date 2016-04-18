(function() {
    'use strict';

    angular
        .module('demoApp')
        .factory('DiagramSearch', DiagramSearch);

    DiagramSearch.$inject = ['$resource'];

    function DiagramSearch($resource) {
        var resourceUrl =  'api/_search/diagrams/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
