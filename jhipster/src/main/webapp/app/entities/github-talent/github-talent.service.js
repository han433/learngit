(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Github_talent', Github_talent);

    Github_talent.$inject = ['$resource'];

    function Github_talent ($resource) {
        var resourceUrl =  'api/github-talents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
