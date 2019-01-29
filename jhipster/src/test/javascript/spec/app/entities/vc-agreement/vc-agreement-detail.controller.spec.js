'use strict';

describe('Controller Tests', function() {

    describe('Vc_agreement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVc_agreement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVc_agreement = jasmine.createSpy('MockVc_agreement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Vc_agreement': MockVc_agreement
            };
            createController = function() {
                $injector.get('$controller')("Vc_agreementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:vc_agreementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
