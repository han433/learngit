(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Vc_agreement', Vc_agreement);

    Vc_agreement.$inject = ['$resource', 'DateUtils'];

    function Vc_agreement ($resource, DateUtils) {
        var resourceUrl =  'api/vc-agreements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.sign_date = DateUtils.convertLocalDateFromServer(data.sign_date);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.sign_date = DateUtils.convertLocalDateToServer(copy.sign_date);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.sign_date = DateUtils.convertLocalDateToServer(copy.sign_date);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
