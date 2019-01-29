(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Application_commissionController', Application_commissionController);

    Application_commissionController.$inject = ['Application_commission'];

    function Application_commissionController(Application_commission) {

        var vm = this;

        vm.application_commissions = [];

        loadAll();

        function loadAll() {
            Application_commission.query(function(result) {
                vm.application_commissions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
