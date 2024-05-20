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

// comentário que não deve ser capturado ?.?.?. com optional chain

function greet(name) {
  // Verifica se o objeto 'person' existe antes de acessar sua propriedade 'name'
  return "Hello, ${name?.toUpperCase()}!";
}

function printData(data) {
  console.log("Data: ${data?.toString() ?? 'N/A'}");
}