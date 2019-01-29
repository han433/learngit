(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SubmittalsAgingReportController', SubmittalsAgingReportController);

    SubmittalsAgingReportController.$inject = ['SubmittalsAgingReport'];

    function SubmittalsAgingReportController(SubmittalsAgingReport) {

        var vm = this;

        vm.submittalsAgingReports = [];

        loadAll();

        function loadAll() {
            SubmittalsAgingReport.query(function(result) {
                vm.submittalsAgingReports = result;
                vm.searchQuery = null;
            });
        }
    }
})();
