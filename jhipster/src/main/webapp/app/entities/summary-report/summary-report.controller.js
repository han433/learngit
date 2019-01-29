(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SummaryReportController', SummaryReportController);

    SummaryReportController.$inject = ['SummaryReport'];

    function SummaryReportController(SummaryReport) {

        var vm = this;

        vm.summaryReports = [];

        loadAll();

        function loadAll() {
            SummaryReport.query(function(result) {
                vm.summaryReports = result;
                vm.searchQuery = null;
            });
        }
    }
})();
