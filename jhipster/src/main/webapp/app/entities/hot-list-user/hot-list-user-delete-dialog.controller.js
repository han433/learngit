(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotListUserDeleteController',HotListUserDeleteController);

    HotListUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'HotListUser'];

    function HotListUserDeleteController($uibModalInstance, entity, HotListUser) {
        var vm = this;

        vm.hotListUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HotListUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
