(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ApnParamController', ApnParamController);

    ApnParamController.$inject = ['ApnParam'];

    function ApnParamController(ApnParam) {

        var vm = this;

        vm.apnParams = [];

        loadAll();

        function loadAll() {
            ApnParam.query(function(result) {
                vm.apnParams = result;
                vm.searchQuery = null;
            });
        }
    }
})();
