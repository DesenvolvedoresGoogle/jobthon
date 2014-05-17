'use strict';

angular.module('appApp')
    .controller('AnalisevagaCtrl', ['$scope', '$stateParams', 'AnaliseVaga',
        function($scope, $stateParams, AnaliseVaga) {
            function init() {
                AnaliseVaga.getList($stateParams.id).then(function(data) {
                    $scope.AnaliseVagas = data;
                })
            }

            init();
        }
    ]);