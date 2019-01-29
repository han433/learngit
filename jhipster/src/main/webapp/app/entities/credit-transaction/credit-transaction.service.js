(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('CreditTransaction', CreditTransaction);

    CreditTransaction.$inject = ['$resource'];

    function CreditTransaction ($resource) {
        var resourceUrl =  'api/credit-transactions/:id';

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
