(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserAlertDeleteController',UserAlertDeleteController);

    UserAlertDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserAlert'];

    function UserAlertDeleteController($uibModalInstance, entity, UserAlert) {
        var vm = this;

        vm.userAlert = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserAlert.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
