(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Vc_agreementDeleteController',Vc_agreementDeleteController);

    Vc_agreementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Vc_agreement'];

    function Vc_agreementDeleteController($uibModalInstance, entity, Vc_agreement) {
        var vm = this;

        vm.vc_agreement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Vc_agreement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
