(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('team-user', {
            parent: 'entity',
            url: '/team-user',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.teamUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/team-user/team-users.html',
                    controller: 'TeamUserController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('teamUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('team-user-detail', {
            parent: 'team-user',
            url: '/team-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.teamUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/team-user/team-user-detail.html',
                    controller: 'TeamUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('teamUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TeamUser', function($stateParams, TeamUser) {
                    return TeamUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'team-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('team-user-detail.edit', {
            parent: 'team-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-user/team-user-dialog.html',
                    controller: 'TeamUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TeamUser', function(TeamUser) {
                            return TeamUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('team-user.new', {
            parent: 'team-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-user/team-user-dialog.html',
                    controller: 'TeamUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                teamId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('team-user', null, { reload: 'team-user' });
                }, function() {
                    $state.go('team-user');
                });
            }]
        })
        .state('team-user.edit', {
            parent: 'team-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-user/team-user-dialog.html',
                    controller: 'TeamUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TeamUser', function(TeamUser) {
                            return TeamUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('team-user', null, { reload: 'team-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('team-user.delete', {
            parent: 'team-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-user/team-user-delete-dialog.html',
                    controller: 'TeamUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TeamUser', function(TeamUser) {
                            return TeamUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('team-user', null, { reload: 'team-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
