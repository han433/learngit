(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HotTalentController', HotTalentController);

    HotTalentController.$inject = ['HotTalent'];

    function HotTalentController(HotTalent) {

        var vm = this;

        vm.hotTalents = [];

        loadAll();

        function loadAll() {
            HotTalent.query(function(result) {
                vm.hotTalents = result;
                vm.searchQuery = null;
            });
        }
    }
})();
