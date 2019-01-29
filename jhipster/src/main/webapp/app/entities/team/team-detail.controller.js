(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Team'];

    function TeamDetailController($scope, $rootScope, $stateParams, previousState, entity, Team) {
        var vm = this;

        vm.team = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
