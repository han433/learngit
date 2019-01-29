(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SubmittalsAgingReportDetailController', SubmittalsAgingReportDetailController);

    SubmittalsAgingReportDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SubmittalsAgingReport'];

    function SubmittalsAgingReportDetailController($scope, $rootScope, $stateParams, previousState, entity, SubmittalsAgingReport) {
        var vm = this;

        vm.submittalsAgingReport = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:submittalsAgingReportUpdate', function(event, result) {
            vm.submittalsAgingReport = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
