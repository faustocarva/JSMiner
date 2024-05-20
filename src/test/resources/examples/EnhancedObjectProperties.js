"use strict";
//------------------------------------------------------------------------------
// Property Shorthand
// Shorter syntax for common object property definition idiom.
// http://es6-features.org/#PropertyShorthand
//------------------------------------------------------------------------------

obj = { x, y }

obj = {
  foo (a, b) {
  },
  *quux (x, y) {
  }
}

let uname = 'Anil',  
    udivision = 'First';  
   
let user = {  
   uname,  
   udivision  
};  
console.log(user.uname);  
console.log(user.udivision);

var department = 'dep_name';  
var emp = {  
    id : 102,  
    name : 'Anil',  
    [department]:'Production'  
}  
console.log(emp);


let obj = {
    foo: "bar",
    [ "baz" + quux() ]: 42
}


//------------------------------------------------------------------------------
// Method Properties
// Support for method notation in object property definitions, for both regular
//   functions and generator functions.
// http://es6-features.org/#MethodProperties
//------------------------------------------------------------------------------


// Exemplo de definição de propriedades de objetos no ECMAScript 3
var obj = {
    prop1: "foo",
    prop2: "bar",
    method: function() {
      console.log("Método");
    }
  };
  
  console.log(obj.prop1); // Output: foo
  console.log(obj.prop2); // Output: bar
  obj.method(); // Output: Método

// Exemplo de definição de propriedades de objetos no ES5
var prop1 = "foo";
var prop2 = "bar";

var obj = {
  prop1: prop1,
  prop2: prop2,
  method: function() {
    console.log("Método");
  }
};

console.log(obj.prop1); // Output: foo
console.log(obj.prop2); // Output: bar
obj.method(); // Output: Método

var obj = {
  foo: "bar"
};
obj[ "baz" + quux() ] = 42;