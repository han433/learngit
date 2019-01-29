(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TalentLocationDetailController', TalentLocationDetailController);

    TalentLocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TalentLocation'];

    function TalentLocationDetailController($scope, $rootScope, $stateParams, previousState, entity, TalentLocation) {
        var vm = this;

        vm.talentLocation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:talentLocationUpdate', function(event, result) {
            vm.talentLocation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
