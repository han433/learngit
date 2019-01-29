(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ActivityDetailsReportController', ActivityDetailsReportController);

    ActivityDetailsReportController.$inject = ['ActivityDetailsReport'];

    function ActivityDetailsReportController(ActivityDetailsReport) {

        var vm = this;

        vm.activityDetailsReports = [];

        loadAll();

        function loadAll() {
            ActivityDetailsReport.query(function(result) {
                vm.activityDetailsReports = result;
                vm.searchQuery = null;
            });
        }
    }
})();
