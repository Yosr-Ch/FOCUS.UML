(function () {
    'use strict';

    angular
        .module('demoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('project', {
                parent: 'entity',
                url: '/project',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Projects'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/project/projects.html',
                        controller: 'ProjectController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {}
            })
            .state('project-diagram', {
                parent: 'entity',
                url: '/project/{id}/diagrams',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Diagram'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/diagram/diagrams.html',
                        controller: 'ProjectDiagramsController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    diagrams: ['$stateParams', 'Project', function ($stateParams, Project) {
                        return Project.diagrams({id: $stateParams.id});
                    }]
                }
            })
            .state('project-detail', {
                parent: 'entity',
                url: '/project/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Project'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/project/project-detail.html',
                        controller: 'ProjectDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Project', function ($stateParams, Project) {
                        return Project.get({id: $stateParams.id});
                    }]
                }
            })
            .state('project.new', {
                parent: 'project',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/project/project-dialog.html',
                        controller: 'ProjectDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    creation_date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('project', null, {reload: true});
                    }, function () {
                        $state.go('project');
                    });
                }]
            })
            .state('project.edit', {
                parent: 'project',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/project/project-dialog.html',
                        controller: 'ProjectDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Project', function (Project) {
                                return Project.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('project', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('project.delete', {
                parent: 'project',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/project/project-delete-dialog.html',
                        controller: 'ProjectDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Project', function (Project) {
                                return Project.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function () {
                        $state.go('project', null, {reload: true});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
