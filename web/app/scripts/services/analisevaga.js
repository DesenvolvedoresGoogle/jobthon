'use strict';

angular.module('appApp')
    .factory('AnaliseVaga', ['Restangular',
        function AnaliseVaga(Restangular) {
            // AngularJS will instantiate a singleton by calling "new" on this function
            return {
                getList: function(id) {
                    return Restangular.all('analises').one('vaga', id).getList();
                }
            }

        }
    ]);