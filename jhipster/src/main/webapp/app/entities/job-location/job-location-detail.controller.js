(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('JobLocationDetailController', JobLocationDetailController);

    JobLocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JobLocation'];

    function JobLocationDetailController($scope, $rootScope, $stateParams, previousState, entity, JobLocation) {
        var vm = this;

        vm.jobLocation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:jobLocationUpdate', function(event, result) {
            vm.jobLocation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
