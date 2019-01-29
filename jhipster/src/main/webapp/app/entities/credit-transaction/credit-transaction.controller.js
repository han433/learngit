(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('CreditTransactionController', CreditTransactionController);

    CreditTransactionController.$inject = ['CreditTransaction'];

    function CreditTransactionController(CreditTransaction) {

        var vm = this;

        vm.creditTransactions = [];

        loadAll();

        function loadAll() {
            CreditTransaction.query(function(result) {
                vm.creditTransactions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
