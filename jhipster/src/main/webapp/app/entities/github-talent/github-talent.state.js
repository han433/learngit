(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('github-talent', {
            parent: 'entity',
            url: '/github-talent',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.github_talent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/github-talent/github-talents.html',
                    controller: 'Github_talentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('github_talent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('github-talent-detail', {
            parent: 'github-talent',
            url: '/github-talent/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.github_talent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/github-talent/github-talent-detail.html',
                    controller: 'Github_talentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('github_talent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Github_talent', function($stateParams, Github_talent) {
                    return Github_talent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'github-talent',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('github-talent-detail.edit', {
            parent: 'github-talent-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/github-talent/github-talent-dialog.html',
                    controller: 'Github_talentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Github_talent', function(Github_talent) {
                            return Github_talent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('github-talent.new', {
            parent: 'github-talent',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/github-talent/github-talent-dialog.html',
                    controller: 'Github_talentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                username: null,
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('github-talent', null, { reload: 'github-talent' });
                }, function() {
                    $state.go('github-talent');
                });
            }]
        })
        .state('github-talent.edit', {
            parent: 'github-talent',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/github-talent/github-talent-dialog.html',
                    controller: 'Github_talentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Github_talent', function(Github_talent) {
                            return Github_talent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('github-talent', null, { reload: 'github-talent' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('github-talent.delete', {
            parent: 'github-talent',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/github-talent/github-talent-delete-dialog.html',
                    controller: 'Github_talentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Github_talent', function(Github_talent) {
                            return Github_talent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('github-talent', null, { reload: 'github-talent' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
