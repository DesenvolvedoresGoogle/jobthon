'use strict';

describe('Service: Empresa', function () {

  // load the service's module
  beforeEach(module('appApp'));

  // instantiate service
  var Empresa;
  beforeEach(inject(function (_Empresa_) {
    Empresa = _Empresa_;
  }));

  it('should do something', function () {
    expect(!!Empresa).toBe(true);
  });

});
