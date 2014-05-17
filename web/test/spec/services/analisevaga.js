'use strict';

describe('Service: Analisevaga', function () {

  // load the service's module
  beforeEach(module('appApp'));

  // instantiate service
  var Analisevaga;
  beforeEach(inject(function (_Analisevaga_) {
    Analisevaga = _Analisevaga_;
  }));

  it('should do something', function () {
    expect(!!Analisevaga).toBe(true);
  });

});
