(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Github_talentDetailController', Github_talentDetailController);

    Github_talentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Github_talent'];

    function Github_talentDetailController($scope, $rootScope, $stateParams, previousState, entity, Github_talent) {
        var vm = this;

        vm.github_talent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:github_talentUpdate', function(event, result) {
            vm.github_talent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
