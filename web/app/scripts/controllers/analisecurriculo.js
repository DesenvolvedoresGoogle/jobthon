'use strict';

angular.module('appApp')
    .controller('AnalisecurriculoCtrl', ['$scope', '$stateParams', 'AnaliseCurriculo',
        function($scope, $stateParams, AnaliseCurriculo) {
            function init() {
                AnaliseCurriculo.getList($stateParams.email).then(function(data) {
                    $scope.AnaliseCurriculo = data;
                })
            }

            init();
        }
    ]);