(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-favorite-talent', {
            parent: 'entity',
            url: '/user-favorite-talent',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.userFavoriteTalent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-favorite-talent/user-favorite-talents.html',
                    controller: 'UserFavoriteTalentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userFavoriteTalent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-favorite-talent-detail', {
            parent: 'user-favorite-talent',
            url: '/user-favorite-talent/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.userFavoriteTalent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-favorite-talent/user-favorite-talent-detail.html',
                    controller: 'UserFavoriteTalentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userFavoriteTalent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserFavoriteTalent', function($stateParams, UserFavoriteTalent) {
                    return UserFavoriteTalent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-favorite-talent',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-favorite-talent-detail.edit', {
            parent: 'user-favorite-talent-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-favorite-talent/user-favorite-talent-dialog.html',
                    controller: 'UserFavoriteTalentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserFavoriteTalent', function(UserFavoriteTalent) {
                            return UserFavoriteTalent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-favorite-talent.new', {
            parent: 'user-favorite-talent',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-favorite-talent/user-favorite-talent-dialog.html',
                    controller: 'UserFavoriteTalentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-favorite-talent', null, { reload: 'user-favorite-talent' });
                }, function() {
                    $state.go('user-favorite-talent');
                });
            }]
        })
        .state('user-favorite-talent.edit', {
            parent: 'user-favorite-talent',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-favorite-talent/user-favorite-talent-dialog.html',
                    controller: 'UserFavoriteTalentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserFavoriteTalent', function(UserFavoriteTalent) {
                            return UserFavoriteTalent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-favorite-talent', null, { reload: 'user-favorite-talent' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-favorite-talent.delete', {
            parent: 'user-favorite-talent',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-favorite-talent/user-favorite-talent-delete-dialog.html',
                    controller: 'UserFavoriteTalentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserFavoriteTalent', function(UserFavoriteTalent) {
                            return UserFavoriteTalent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-favorite-talent', null, { reload: 'user-favorite-talent' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
