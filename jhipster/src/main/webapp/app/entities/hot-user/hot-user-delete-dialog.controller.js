(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotUserDeleteController',HotUserDeleteController);

    HotUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'HotUser'];

    function HotUserDeleteController($uibModalInstance, entity, HotUser) {
        var vm = this;

        vm.hotUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HotUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
