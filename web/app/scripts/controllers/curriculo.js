'use strict';

angular.module('appApp')
    .controller('CurriculoCtrl', ['$scope', 'Curriculo',
        function($scope, Curriculo) {
            function init() {
                Curriculo.getList().then(function(data) {
                    $scope.Curriculos = data;
                });
            }

            init();

            $scope.editCurriculos = function(curriculo) {
                $scope.editCurriculo = angular.copy(curriculo);
            }

            $scope.addCurriculo = function() {
                $scope.newCurriculo.idade *= 1;
                $scope.Curriculos.push($scope.newCurriculo);
                Curriculo.add($scope.newCurriculo);
            };

            $scope.initNewCurriculo = function() {
                $scope.newCurriculo = {
                    "email": null,
                    "nome": null,
                    "idade": null,
                    "habilidades": [],
                    "cidade": null,
                    "estado": null,
                    "telefone": null
                };
            }

            $scope.addHabilidade = function(curriculo, habilidade) {
                curriculo.habilidades.push(habilidade);
                $scope.adicionando = false;
            };

            $scope.initNewHabilidade = function() {
                $scope.habilidade = undefined;
                $scope.adicionando = true;
            };
            $scope.saveEditCurriculo = function() {
                Curriculo.add($scope.editCurriculo);
                init();
            };
        }
    ]);