'use strict';

angular.module('appApp')
    .controller('VagaCtrl', ['$scope', 'VagaService',
        function($scope, VagaService) {
            // $scope.Vagas = [{
            //     id: 0,
            //     titulo: 'test',
            //     sobre: 'testestsete',
            //     habilidades: [{
            //         descricao: 'habilidade1'
            //     }, {
            //         descricao: 'habilidade1'
            //     }]
            // }, {
            //     id: 1,
            //     titulo: 'test',
            //     sobre: 'testestsete'
            // }, {
            //     id: 2,
            //     titulo: 'test',
            //     sobre: 'testestsete'
            // }];

            VagaService.getList().then(function(data) {
                $scope.Vagas = data;
            });

            $scope.editVagas = function(vaga) {
                $scope.editVaga = vaga;
            }

            $scope.AreasVagas = [{
                descricao: 'Industrial'
            }, {
                descricao: 'Vendas'
            }, {
                descricao: 'Outros'
            }];

            $scope.TipoContratacao = [{
                descricao: 'Estagiàrio'
            }, {
                descricao: 'Definitivo'
            }, {
                descricao: 'Outros'
            }];

            $scope.VagaCurriculos = [{
                nome: 'test'
            }, {
                nome: 'test'
            }];

        }
    ]);