(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ApnParamDeleteController',ApnParamDeleteController);

    ApnParamDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApnParam'];

    function ApnParamDeleteController($uibModalInstance, entity, ApnParam) {
        var vm = this;

        vm.apnParam = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ApnParam.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
