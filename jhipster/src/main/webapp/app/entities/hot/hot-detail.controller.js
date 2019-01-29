(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotDetailController', HotDetailController);

    HotDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Hot', 'HotUser'];

    function HotDetailController($scope, $rootScope, $stateParams, previousState, entity, Hot, HotUser) {
        var vm = this;

        vm.hot = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:hotUpdate', function(event, result) {
            vm.hot = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
