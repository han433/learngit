(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserFavoriteJobDetailController', UserFavoriteJobDetailController);

    UserFavoriteJobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserFavoriteJob'];

    function UserFavoriteJobDetailController($scope, $rootScope, $stateParams, previousState, entity, UserFavoriteJob) {
        var vm = this;

        vm.userFavoriteJob = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:userFavoriteJobUpdate', function(event, result) {
            vm.userFavoriteJob = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
