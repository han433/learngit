(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Vc_agreementDetailController', Vc_agreementDetailController);

    Vc_agreementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vc_agreement'];

    function Vc_agreementDetailController($scope, $rootScope, $stateParams, previousState, entity, Vc_agreement) {
        var vm = this;

        vm.vc_agreement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:vc_agreementUpdate', function(event, result) {
            vm.vc_agreement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
