(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ActivityDetailsReportDetailController', ActivityDetailsReportDetailController);

    ActivityDetailsReportDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ActivityDetailsReport'];

    function ActivityDetailsReportDetailController($scope, $rootScope, $stateParams, previousState, entity, ActivityDetailsReport) {
        var vm = this;

        vm.activityDetailsReport = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:activityDetailsReportUpdate', function(event, result) {
            vm.activityDetailsReport = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
