(function() {
    'use strict';

    angular
        .module('demoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('diagram', {
            parent: 'entity',
            url: '/diagram',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Diagrams'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diagram/diagrams.html',
                    controller: 'DiagramController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('diagram-detail', {
            parent: 'entity',
            url: '/diagram/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Diagram'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diagram/diagram-detail.html',
                    controller: 'DiagramDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Diagram', function($stateParams, Diagram) {
                    return Diagram.get({id : $stateParams.id});
                }]
            }
        })
            /*.state('diagram-editor', {
                parent: 'entity',
                url: '/project/{id}/diagrams/diagram-editor',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Diagram'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/diagram/diagram-editor.html',
                        controller: 'DiagramEditorController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            visibility: null,
                            content: null,
                            validation: null,
                            id: null
                        };
                    }
                }
            })*/
        .state('diagram.new', {
            parent: 'diagram',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagram/diagram-dialog.html',
                    controller: 'DiagramDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                visibility: null,
                                content: null,
                                validation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('diagram', null, { reload: true });
                }, function() {
                    $state.go('diagram');
                });
            }]
        })
        .state('diagram.edit', {
            parent: 'diagram',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagram/diagram-dialog.html',
                    controller: 'DiagramDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Diagram', function(Diagram) {
                            return Diagram.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('diagram', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diagram.delete', {
            parent: 'diagram',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagram/diagram-delete-dialog.html',
                    controller: 'DiagramDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Diagram', function(Diagram) {
                            return Diagram.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('diagram', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
