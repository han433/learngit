(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UserCreditController', UserCreditController);

    UserCreditController.$inject = ['UserCredit'];

    function UserCreditController(UserCredit) {

        var vm = this;

        vm.userCredits = [];

        loadAll();

        function loadAll() {
            UserCredit.query(function(result) {
                vm.userCredits = result;
                vm.searchQuery = null;
            });
        }
    }
})();
