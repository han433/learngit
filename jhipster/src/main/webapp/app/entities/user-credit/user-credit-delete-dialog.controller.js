(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserCreditDeleteController',UserCreditDeleteController);

    UserCreditDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserCredit'];

    function UserCreditDeleteController($uibModalInstance, entity, UserCredit) {
        var vm = this;

        vm.userCredit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserCredit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
