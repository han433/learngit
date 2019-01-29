(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SubmittalsAgingReportDeleteController',SubmittalsAgingReportDeleteController);

    SubmittalsAgingReportDeleteController.$inject = ['$uibModalInstance', 'entity', 'SubmittalsAgingReport'];

    function SubmittalsAgingReportDeleteController($uibModalInstance, entity, SubmittalsAgingReport) {
        var vm = this;

        vm.submittalsAgingReport = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SubmittalsAgingReport.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
