(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TeamUserDetailController', TeamUserDetailController);

    TeamUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TeamUser'];

    function TeamUserDetailController($scope, $rootScope, $stateParams, previousState, entity, TeamUser) {
        var vm = this;

        vm.teamUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:teamUserUpdate', function(event, result) {
            vm.teamUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
