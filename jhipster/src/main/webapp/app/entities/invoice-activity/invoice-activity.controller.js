(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('InvoiceActivityController', InvoiceActivityController);

    InvoiceActivityController.$inject = ['InvoiceActivity'];

    function InvoiceActivityController(InvoiceActivity) {

        var vm = this;

        vm.invoiceActivities = [];

        loadAll();

        function loadAll() {
            InvoiceActivity.query(function(result) {
                vm.invoiceActivities = result;
                vm.searchQuery = null;
            });
        }
    }
})();
