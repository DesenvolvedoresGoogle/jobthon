'use strict';

angular.module('appApp')
    .controller('VagaCtrl', ['$scope', 'VagaService',
        function($scope, VagaService) {

            VagaService.getList().then(function(data) {
                $scope.Vagas = data;
            });

            $scope.editVagas = function(vaga) {
                $scope.editVaga = vaga;
            }

            $scope.addVaga = function() {
                $scope.Vagas.push($scope.newVaga);
                VagaService.save($scope.newVaga);
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