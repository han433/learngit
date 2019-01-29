(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hot-list-user', {
            parent: 'entity',
            url: '/hot-list-user',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.hotListUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hot-list-user/hot-list-users.html',
                    controller: 'HotListUserController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hotListUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('hot-list-user-detail', {
            parent: 'hot-list-user',
            url: '/hot-list-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.hotListUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/hot-list-user/hot-list-user-detail.html',
                    controller: 'HotListUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('hotListUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HotListUser', function($stateParams, HotListUser) {
                    return HotListUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'hot-list-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('hot-list-user-detail.edit', {
            parent: 'hot-list-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-list-user/hot-list-user-dialog.html',
                    controller: 'HotListUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HotListUser', function(HotListUser) {
                            return HotListUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hot-list-user.new', {
            parent: 'hot-list-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-list-user/hot-list-user-dialog.html',
                    controller: 'HotListUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                hotListId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('hot-list-user', null, { reload: 'hot-list-user' });
                }, function() {
                    $state.go('hot-list-user');
                });
            }]
        })
        .state('hot-list-user.edit', {
            parent: 'hot-list-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-list-user/hot-list-user-dialog.html',
                    controller: 'HotListUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HotListUser', function(HotListUser) {
                            return HotListUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hot-list-user', null, { reload: 'hot-list-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('hot-list-user.delete', {
            parent: 'hot-list-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/hot-list-user/hot-list-user-delete-dialog.html',
                    controller: 'HotListUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HotListUser', function(HotListUser) {
                            return HotListUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('hot-list-user', null, { reload: 'hot-list-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
