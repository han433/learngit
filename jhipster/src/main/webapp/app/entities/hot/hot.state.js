(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hot', {
            parent: 'entity',
            url: '/hot',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.hot.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hot/hots.html',
                    controller: 'HotController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hot');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hot-detail', {
            parent: 'hot',
            url: '/hot/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.hot.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hot/hot-detail.html',
                    controller: 'HotDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hot');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Hot', function($stateParams, Hot) {
                    return Hot.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hot',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hot-detail.edit', {
            parent: 'hot-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot/hot-dialog.html',
                    controller: 'HotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hot', function(Hot) {
                            return Hot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hot.new', {
            parent: 'hot',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot/hot-dialog.html',
                    controller: 'HotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                notes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hot', null, { reload: 'hot' });
                }, function() {
                    $state.go('hot');
                });
            }]
        })
        .state('hot.edit', {
            parent: 'hot',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot/hot-dialog.html',
                    controller: 'HotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Hot', function(Hot) {
                            return Hot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hot', null, { reload: 'hot' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hot.delete', {
            parent: 'hot',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot/hot-delete-dialog.html',
                    controller: 'HotDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Hot', function(Hot) {
                            return Hot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hot', null, { reload: 'hot' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
