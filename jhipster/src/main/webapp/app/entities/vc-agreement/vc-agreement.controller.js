(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('Vc_agreementController', Vc_agreementController);

    Vc_agreementController.$inject = ['Vc_agreement'];

    function Vc_agreementController(Vc_agreement) {

        var vm = this;

        vm.vc_agreements = [];

        loadAll();

        function loadAll() {
            Vc_agreement.query(function(result) {
                vm.vc_agreements = result;
                vm.searchQuery = null;
            });
        }
    }
})();
