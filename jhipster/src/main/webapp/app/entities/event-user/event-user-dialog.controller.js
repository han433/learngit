(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Event_userDialogController', Event_userDialogController);

    Event_userDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Event_user'];

    function Event_userDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Event_user) {
        var vm = this;

        vm.event_user = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.event_user.id !== null) {
                Event_user.update(vm.event_user, onSaveSuccess, onSaveError);
            } else {
                Event_user.save(vm.event_user, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:event_userUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
