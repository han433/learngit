(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Event_userDeleteController',Event_userDeleteController);

    Event_userDeleteController.$inject = ['$uibModalInstance', 'entity', 'Event_user'];

    function Event_userDeleteController($uibModalInstance, entity, Event_user) {
        var vm = this;

        vm.event_user = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Event_user.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
