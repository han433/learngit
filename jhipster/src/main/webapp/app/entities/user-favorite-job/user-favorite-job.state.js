(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-favorite-job', {
            parent: 'entity',
            url: '/user-favorite-job',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.userFavoriteJob.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-favorite-job/user-favorite-jobs.html',
                    controller: 'UserFavoriteJobController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userFavoriteJob');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-favorite-job-detail', {
            parent: 'user-favorite-job',
            url: '/user-favorite-job/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.userFavoriteJob.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-favorite-job/user-favorite-job-detail.html',
                    controller: 'UserFavoriteJobDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userFavoriteJob');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserFavoriteJob', function($stateParams, UserFavoriteJob) {
                    return UserFavoriteJob.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-favorite-job',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-favorite-job-detail.edit', {
            parent: 'user-favorite-job-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-favorite-job/user-favorite-job-dialog.html',
                    controller: 'UserFavoriteJobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserFavoriteJob', function(UserFavoriteJob) {
                            return UserFavoriteJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-favorite-job.new', {
            parent: 'user-favorite-job',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-favorite-job/user-favorite-job-dialog.html',
                    controller: 'UserFavoriteJobDialogController',
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
                    $state.go('user-favorite-job', null, { reload: 'user-favorite-job' });
                }, function() {
                    $state.go('user-favorite-job');
                });
            }]
        })
        .state('user-favorite-job.edit', {
            parent: 'user-favorite-job',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-favorite-job/user-favorite-job-dialog.html',
                    controller: 'UserFavoriteJobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserFavoriteJob', function(UserFavoriteJob) {
                            return UserFavoriteJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-favorite-job', null, { reload: 'user-favorite-job' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-favorite-job.delete', {
            parent: 'user-favorite-job',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-favorite-job/user-favorite-job-delete-dialog.html',
                    controller: 'UserFavoriteJobDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserFavoriteJob', function(UserFavoriteJob) {
                            return UserFavoriteJob.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-favorite-job', null, { reload: 'user-favorite-job' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
