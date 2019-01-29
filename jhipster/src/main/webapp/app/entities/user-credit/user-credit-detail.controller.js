(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserCreditDetailController', UserCreditDetailController);

    UserCreditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserCredit'];

    function UserCreditDetailController($scope, $rootScope, $stateParams, previousState, entity, UserCredit) {
        var vm = this;

        vm.userCredit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:userCreditUpdate', function(event, result) {
            vm.userCredit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
