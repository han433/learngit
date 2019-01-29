(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hot-talent', {
            parent: 'entity',
            url: '/hot-talent',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.hotTalent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hot-talent/hot-talents.html',
                    controller: 'HotTalentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hotTalent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hot-talent-detail', {
            parent: 'hot-talent',
            url: '/hot-talent/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.hotTalent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hot-talent/hot-talent-detail.html',
                    controller: 'HotTalentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hotTalent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HotTalent', function($stateParams, HotTalent) {
                    return HotTalent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hot-talent',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hot-talent-detail.edit', {
            parent: 'hot-talent-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-talent/hot-talent-dialog.html',
                    controller: 'HotTalentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HotTalent', function(HotTalent) {
                            return HotTalent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hot-talent.new', {
            parent: 'hot-talent',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-talent/hot-talent-dialog.html',
                    controller: 'HotTalentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                hotid: null,
                                talentid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hot-talent', null, { reload: 'hot-talent' });
                }, function() {
                    $state.go('hot-talent');
                });
            }]
        })
        .state('hot-talent.edit', {
            parent: 'hot-talent',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-talent/hot-talent-dialog.html',
                    controller: 'HotTalentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HotTalent', function(HotTalent) {
                            return HotTalent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hot-talent', null, { reload: 'hot-talent' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hot-talent.delete', {
            parent: 'hot-talent',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-talent/hot-talent-delete-dialog.html',
                    controller: 'HotTalentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HotTalent', function(HotTalent) {
                            return HotTalent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hot-talent', null, { reload: 'hot-talent' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
