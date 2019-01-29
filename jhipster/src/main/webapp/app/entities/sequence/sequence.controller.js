(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SequenceController', SequenceController);

    SequenceController.$inject = ['Sequence'];

    function SequenceController(Sequence) {

        var vm = this;

        vm.sequences = [];

        loadAll();

        function loadAll() {
            Sequence.query(function(result) {
                vm.sequences = result;
                vm.searchQuery = null;
            });
        }
    }
})();
