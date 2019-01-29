(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotTalentDetailController', HotTalentDetailController);

    HotTalentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'HotTalent', 'Hot'];

    function HotTalentDetailController($scope, $rootScope, $stateParams, previousState, entity, HotTalent, Hot) {
        var vm = this;

        vm.hotTalent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:hotTalentUpdate', function(event, result) {
            vm.hotTalent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
