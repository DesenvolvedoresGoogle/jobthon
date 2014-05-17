'use strict';

angular.module('appApp')
    .factory('Curriculo', ['Restangular',
        function Curriculo(Restangular) {
            // AngularJS will instantiate a singleton by calling "new" on this function
            return {
                get: function(id) {
                    return Restangular.one('curriculo', id).get();
                },
                getList: function() {
                    return Restangular.all('curriculos').getList();
                },
                add: function(curriculo) {
                    Restangular.all('curriculo').post(curriculo);
                }
            };

        }
    ]);