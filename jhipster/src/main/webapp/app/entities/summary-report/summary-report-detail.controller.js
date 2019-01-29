(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SummaryReportDetailController', SummaryReportDetailController);

    SummaryReportDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SummaryReport'];

    function SummaryReportDetailController($scope, $rootScope, $stateParams, previousState, entity, SummaryReport) {
        var vm = this;

        vm.summaryReport = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:summaryReportUpdate', function(event, result) {
            vm.summaryReport = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
