(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ActivityReportDetailController', ActivityReportDetailController);

    ActivityReportDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ActivityReport'];

    function ActivityReportDetailController($scope, $rootScope, $stateParams, previousState, entity, ActivityReport) {
        var vm = this;

        vm.activityReport = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:activityReportUpdate', function(event, result) {
            vm.activityReport = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
