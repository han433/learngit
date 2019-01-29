(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Event_userDetailController', Event_userDetailController);

    Event_userDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Event_user'];

    function Event_userDetailController($scope, $rootScope, $stateParams, previousState, entity, Event_user) {
        var vm = this;

        vm.event_user = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:event_userUpdate', function(event, result) {
            vm.event_user = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
