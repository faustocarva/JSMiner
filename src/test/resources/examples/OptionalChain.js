"use strict";

// Optional Chain
//
// https://262.ecma-international.org/11.0/#prod-OptionalChain
var e = { f: { g: 'valor' } };
var i = {
    j: function() {
      return 'valor_j';
    }
  };
  var l = {
    m: {
      n: {
        o: 'valor_o'
      }
    }
  };

var d = e?.f?.g;
var h = i?.j();
var k = l?.['m']?.n?.['o'];
