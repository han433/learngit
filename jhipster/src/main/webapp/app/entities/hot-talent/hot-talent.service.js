(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('HotTalent', HotTalent);

    HotTalent.$inject = ['$resource'];

    function HotTalent ($resource) {
        var resourceUrl =  'api/hot-talents/:id';

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
