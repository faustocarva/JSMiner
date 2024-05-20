"use strict";
//------------------------------------------------------------------------------
// Array Matching
// Intuitive and flexible destructuring of Arrays into individual variables during assignment.
// http://es6-features.org/#ArrayMatching
//------------------------------------------------------------------------------

var list = [ 1, 2, 3 ]
var [ a, , b ] = list//ok
[ b, a ] = [ a, b ]

//------------------------------------------------------------------------------
// Object Matching, Shorthand Notation
// Intuitive and flexible destructuring of Objects into individual variables during assignment.
// http://es6-features.org/#ObjectMatchingShorthandNotation
//------------------------------------------------------------------------------

var { op, lhs, rhs } = getASTNode()//ok

//------------------------------------------------------------------------------
// Object Matching, Deep Matching
// Intuitive and flexible destructuring of Objects into individual variables during assignment.
// http://es6-features.org/#ObjectMatchingDeepMatching
//------------------------------------------------------------------------------

var { op: a, lhs: { op: b }, rhs: c } = getASTNode()//ok

//------------------------------------------------------------------------------
// Object And Array Matching, Default Values
// Simple and intuitive default values for destructuring of Objects and Arrays.
// http://es6-features.org/#ObjectAndArrayMatchingDefaultValues
//------------------------------------------------------------------------------

var obj = { a: 1 }
var list = [ 1 ]
var { a, b = 2 } = obj//ok
var [ x, y = 2 ] = list//ok

//------------------------------------------------------------------------------
// Parameter Context Matching
// Intuitive and flexible destructuring of Arrays and Objects into individual
//   parameters during function calls.
// http://es6-features.org/#ParameterContextMatching
//------------------------------------------------------------------------------

function f ([ name, val ]) {
    console.log(name, val)
}
function g ({ name: n, val: v }) {
    console.log(n, v)
}
function h ({ name, val }) {
    console.log(name, val)
}
f([ "bar", 42 ])
g({ name: "foo", val:  7 })
h({ name: "bar", val: 42 })

//------------------------------------------------------------------------------
// Fail-Soft Destructuring
// Fail-soft destructuring, optionally with defaults.
// http://es6-features.org/#FailSoftDestructuring
//------------------------------------------------------------------------------

var list = [ 7, 42 ]
var [ a = 1, b = 2, c = 3, d ] = list//ok
a === 7
b === 42
c === 3
d === undefined

// Exemplos de destruturação de array que devem ser capturados
let [x, y] = [1, 2];
const [a, b, c] = [3, 4, 5];
let [first, , third] = ['apple', 'banana', 'cherry'];
const [head, ...tail] = [1, 2, 3, 4];

// Exemplos de destruturação de objeto que devem ser capturados
let {name, age} = {name: 'Alice', age: 25};
const {width, height} = {width: 100, height: 200};
let {foo: f, bar: b} = {foo: 'baz', bar: 'qux'};
const {a: alpha, b: beta, ...rest} = {a: 1, b: 2, c: 3, d: 4};


// Exemplos de uso em strings e comentários que não devem ser capturados
const str = "This is a string with [x, y] destructuring"; // Não deve ser capturado
const templateStr = `Object destructuring example: {name, age}`; // Não deve ser capturado

// Comentários que contêm a sintaxe de destruturação, que não devem ser capturados
// This is a comment with [a, b] destructuring
/* Another comment with {width, height} destructuring */

// Código adicional que não deve ser capturado
let arr = [10, 20, 30];
let obj = {foo: 'bar', baz: 'qux'};
let notDestructuring1 = arr[0]; // Acesso direto ao array
let notDestructuring2 = obj.foo; // Acesso direto ao objeto