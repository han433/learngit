(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('CreditTransactionDetailController', CreditTransactionDetailController);

    CreditTransactionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CreditTransaction'];

    function CreditTransactionDetailController($scope, $rootScope, $stateParams, previousState, entity, CreditTransaction) {
        var vm = this;

        vm.creditTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:creditTransactionUpdate', function(event, result) {
            vm.creditTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
