'use strict';

angular.module('appApp')
    .controller('VagaCtrl', ['$scope', 'VagaService',
        function($scope, VagaService) {

            VagaService.getList().then(function(data) {
                $scope.Vagas = data;
            });

            $scope.viewVagas = function(vaga) {
                $scope.viewVaga = angular.copy(vaga);
                $scope.viewVaga.contratacao = {
                    'Estágio': null,
                    'PJ': null,
                    'CLT': null
                }

                angular.forEach(vaga.contratacao, function(value) {
                    $scope.viewVaga.contratacao[value] = true
                });

            };

            $scope.initNewVaga = function(vaga) {
                $scope.newVaga = {
                    "id": null,
                    "email": null,
                    "titulo": null,
                    "sobre": null,
                    "habilidades": [],
                    "area": null,
                    "cidade": null,
                    "estado": null,
                    "ativa": null,
                    "contratacao": {
                        'Estágio': null,
                        'PJ': null,
                        'CLT': null
                    }
                };
            }

            $scope.addVaga = function() {
                var contratacao = [];
                var i = 0;
                angular.forEach($scope.newVaga.contratacao, function(value, key) {
                    if (value) {
                        contratacao.push(key);
                    }
                });
                $scope.newVaga.contratacao = contratacao;
                $scope.newVaga.area = $scope.newVaga.area.descricao;
                $scope.Vagas.push($scope.newVaga);
                VagaService.add($scope.newVaga);
            };

            $scope.addHabilidade = function(vaga, habilidade) {
                vaga.habilidades.push(habilidade);
                $scope.adicionando = false;
            };

            $scope.initNewHavilidade = function() {
                $scope.habilidade = undefined;
                $scope.adicionando = true;
            }

            $scope.AreasVagas = [{
                descricao: 'Programador'
            }, {
                descricao: 'Vendas'
            }, {
                descricao: 'Design'
            }, {
                descricao: 'Administrativo'
            }];
        }
    ]);