'use strict';

describe('Service: Analisecurriculo', function () {

  // load the service's module
  beforeEach(module('appApp'));

  // instantiate service
  var Analisecurriculo;
  beforeEach(inject(function (_Analisecurriculo_) {
    Analisecurriculo = _Analisecurriculo_;
  }));

  it('should do something', function () {
    expect(!!Analisecurriculo).toBe(true);
  });

});
