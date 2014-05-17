'use strict';

describe('Controller: AnalisecurriculoCtrl', function () {

  // load the controller's module
  beforeEach(module('appApp'));

  var AnalisecurriculoCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AnalisecurriculoCtrl = $controller('AnalisecurriculoCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
