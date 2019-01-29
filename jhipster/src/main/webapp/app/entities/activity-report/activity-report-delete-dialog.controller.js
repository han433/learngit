(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ActivityReportDeleteController',ActivityReportDeleteController);

    ActivityReportDeleteController.$inject = ['$uibModalInstance', 'entity', 'ActivityReport'];

    function ActivityReportDeleteController($uibModalInstance, entity, ActivityReport) {
        var vm = this;

        vm.activityReport = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ActivityReport.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
