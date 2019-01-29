(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('activity-details-report', {
            parent: 'entity',
            url: '/activity-details-report',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.activityDetailsReport.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity-details-report/activity-details-reports.html',
                    controller: 'ActivityDetailsReportController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activityDetailsReport');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('activity-details-report-detail', {
            parent: 'activity-details-report',
            url: '/activity-details-report/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.activityDetailsReport.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity-details-report/activity-details-report-detail.html',
                    controller: 'ActivityDetailsReportDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activityDetailsReport');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ActivityDetailsReport', function($stateParams, ActivityDetailsReport) {
                    return ActivityDetailsReport.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'activity-details-report',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('activity-details-report-detail.edit', {
            parent: 'activity-details-report-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-details-report/activity-details-report-dialog.html',
                    controller: 'ActivityDetailsReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActivityDetailsReport', function(ActivityDetailsReport) {
                            return ActivityDetailsReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activity-details-report.new', {
            parent: 'activity-details-report',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-details-report/activity-details-report-dialog.html',
                    controller: 'ActivityDetailsReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('activity-details-report', null, { reload: 'activity-details-report' });
                }, function() {
                    $state.go('activity-details-report');
                });
            }]
        })
        .state('activity-details-report.edit', {
            parent: 'activity-details-report',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-details-report/activity-details-report-dialog.html',
                    controller: 'ActivityDetailsReportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActivityDetailsReport', function(ActivityDetailsReport) {
                            return ActivityDetailsReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity-details-report', null, { reload: 'activity-details-report' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activity-details-report.delete', {
            parent: 'activity-details-report',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-details-report/activity-details-report-delete-dialog.html',
                    controller: 'ActivityDetailsReportDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ActivityDetailsReport', function(ActivityDetailsReport) {
                            return ActivityDetailsReport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity-details-report', null, { reload: 'activity-details-report' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
