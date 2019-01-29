(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SummaryReportDeleteController',SummaryReportDeleteController);

    SummaryReportDeleteController.$inject = ['$uibModalInstance', 'entity', 'SummaryReport'];

    function SummaryReportDeleteController($uibModalInstance, entity, SummaryReport) {
        var vm = this;

        vm.summaryReport = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SummaryReport.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
