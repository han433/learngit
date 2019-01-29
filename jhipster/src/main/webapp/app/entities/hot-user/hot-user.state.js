(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hot-user', {
            parent: 'entity',
            url: '/hot-user',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.hotUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hot-user/hot-users.html',
                    controller: 'HotUserController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hotUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hot-user-detail', {
            parent: 'hot-user',
            url: '/hot-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.hotUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hot-user/hot-user-detail.html',
                    controller: 'HotUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hotUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HotUser', function($stateParams, HotUser) {
                    return HotUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hot-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hot-user-detail.edit', {
            parent: 'hot-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-user/hot-user-dialog.html',
                    controller: 'HotUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HotUser', function(HotUser) {
                            return HotUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hot-user.new', {
            parent: 'hot-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-user/hot-user-dialog.html',
                    controller: 'HotUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                hotid: null,
                                userid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hot-user', null, { reload: 'hot-user' });
                }, function() {
                    $state.go('hot-user');
                });
            }]
        })
        .state('hot-user.edit', {
            parent: 'hot-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-user/hot-user-dialog.html',
                    controller: 'HotUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HotUser', function(HotUser) {
                            return HotUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hot-user', null, { reload: 'hot-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hot-user.delete', {
            parent: 'hot-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-user/hot-user-delete-dialog.html',
                    controller: 'HotUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HotUser', function(HotUser) {
                            return HotUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hot-user', null, { reload: 'hot-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
