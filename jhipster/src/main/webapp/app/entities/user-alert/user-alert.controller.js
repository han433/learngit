(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserAlertController', UserAlertController);

    UserAlertController.$inject = ['UserAlert'];

    function UserAlertController(UserAlert) {

        var vm = this;

        vm.userAlerts = [];

        loadAll();

        function loadAll() {
            UserAlert.query(function(result) {
                vm.userAlerts = result;
                vm.searchQuery = null;
            });
        }
    }
})();
