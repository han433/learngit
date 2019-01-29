(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TalentLocationController', TalentLocationController);

    TalentLocationController.$inject = ['TalentLocation'];

    function TalentLocationController(TalentLocation) {

        var vm = this;

        vm.talentLocations = [];

        loadAll();

        function loadAll() {
            TalentLocation.query(function(result) {
                vm.talentLocations = result;
                vm.searchQuery = null;
            });
        }
    }
})();
