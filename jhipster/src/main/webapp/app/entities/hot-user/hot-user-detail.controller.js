(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotUserDetailController', HotUserDetailController);

    HotUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HotUser', 'Hot'];

    function HotUserDetailController($scope, $rootScope, $stateParams, previousState, entity, HotUser, Hot) {
        var vm = this;

        vm.hotUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:hotUserUpdate', function(event, result) {
            vm.hotUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
