(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('talent-location', {
            parent: 'entity',
            url: '/talent-location',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.talentLocation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/talent-location/talent-locations.html',
                    controller: 'TalentLocationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('talentLocation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('talent-location-detail', {
            parent: 'talent-location',
            url: '/talent-location/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.talentLocation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/talent-location/talent-location-detail.html',
                    controller: 'TalentLocationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('talentLocation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TalentLocation', function($stateParams, TalentLocation) {
                    return TalentLocation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'talent-location',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('talent-location-detail.edit', {
            parent: 'talent-location-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/talent-location/talent-location-dialog.html',
                    controller: 'TalentLocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TalentLocation', function(TalentLocation) {
                            return TalentLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('talent-location.new', {
            parent: 'talent-location',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/talent-location/talent-location-dialog.html',
                    controller: 'TalentLocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                city: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('talent-location', null, { reload: 'talent-location' });
                }, function() {
                    $state.go('talent-location');
                });
            }]
        })
        .state('talent-location.edit', {
            parent: 'talent-location',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/talent-location/talent-location-dialog.html',
                    controller: 'TalentLocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TalentLocation', function(TalentLocation) {
                            return TalentLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('talent-location', null, { reload: 'talent-location' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('talent-location.delete', {
            parent: 'talent-location',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/talent-location/talent-location-delete-dialog.html',
                    controller: 'TalentLocationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TalentLocation', function(TalentLocation) {
                            return TalentLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('talent-location', null, { reload: 'talent-location' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
