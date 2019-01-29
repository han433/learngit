(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserAlertDetailController', UserAlertDetailController);

    UserAlertDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserAlert'];

    function UserAlertDetailController($scope, $rootScope, $stateParams, previousState, entity, UserAlert) {
        var vm = this;

        vm.userAlert = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:userAlertUpdate', function(event, result) {
            vm.userAlert = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
