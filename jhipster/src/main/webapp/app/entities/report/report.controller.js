(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['Report'];

    function ReportController(Report) {

        var vm = this;

        vm.reports = [];

        loadAll();

        function loadAll() {
            Report.query(function(result) {
                vm.reports = result;
                vm.searchQuery = null;
            });
        }
    }
})();
