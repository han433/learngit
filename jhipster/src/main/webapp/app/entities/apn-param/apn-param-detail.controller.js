(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ApnParamDetailController', ApnParamDetailController);

    ApnParamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ApnParam'];

    function ApnParamDetailController($scope, $rootScope, $stateParams, previousState, entity, ApnParam) {
        var vm = this;

        vm.apnParam = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:apnParamUpdate', function(event, result) {
            vm.apnParam = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
