(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SequenceDetailController', SequenceDetailController);

    SequenceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sequence'];

    function SequenceDetailController($scope, $rootScope, $stateParams, previousState, entity, Sequence) {
        var vm = this;

        vm.sequence = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:sequenceUpdate', function(event, result) {
            vm.sequence = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
