(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ActivityReportController', ActivityReportController);

    ActivityReportController.$inject = ['ActivityReport'];

    function ActivityReportController(ActivityReport) {

        var vm = this;

        vm.activityReports = [];

        loadAll();

        function loadAll() {
            ActivityReport.query(function(result) {
                vm.activityReports = result;
                vm.searchQuery = null;
            });
        }
    }
})();
