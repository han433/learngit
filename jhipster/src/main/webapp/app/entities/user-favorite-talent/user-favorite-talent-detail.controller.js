(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserFavoriteTalentDetailController', UserFavoriteTalentDetailController);

    UserFavoriteTalentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserFavoriteTalent'];

    function UserFavoriteTalentDetailController($scope, $rootScope, $stateParams, previousState, entity, UserFavoriteTalent) {
        var vm = this;

        vm.userFavoriteTalent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:userFavoriteTalentUpdate', function(event, result) {
            vm.userFavoriteTalent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
