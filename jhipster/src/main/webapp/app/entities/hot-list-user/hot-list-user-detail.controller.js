(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotListUserDetailController', HotListUserDetailController);

    HotListUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HotListUser'];

    function HotListUserDetailController($scope, $rootScope, $stateParams, previousState, entity, HotListUser) {
        var vm = this;

        vm.hotListUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:hotListUserUpdate', function(event, result) {
            vm.hotListUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
