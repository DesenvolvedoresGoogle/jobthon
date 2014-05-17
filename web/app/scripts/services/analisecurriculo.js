'use strict';

angular.module('appApp')
    .factory('AnaliseCurriculo', ['Restangular',
        function AnaliseCurriculo(Restangular) {
            // AngularJS will instantiate a singleton by calling "new" on this function
            return {
                getList: function(email) {
                    return Restangular.all('analises').one('curriculo', email).getList();
                }
            }

        }
    ]);