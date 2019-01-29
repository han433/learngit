(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Application_commissionDeleteController',Application_commissionDeleteController);

    Application_commissionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Application_commission'];

    function Application_commissionDeleteController($uibModalInstance, entity, Application_commission) {
        var vm = this;

        vm.application_commission = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Application_commission.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
