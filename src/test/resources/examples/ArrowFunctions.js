"use strict";
//------------------------------------------------------------------------------
// Expression Bodies
// More expressive closure syntax.
// http://es6-features.org/#ExpressionBodies
//------------------------------------------------------------------------------

odds  = evens.map(v => v + 1)
pairs = evens.map(v => ({ even: v, odd: v + 1 }))
nums  = evens.map((v, i) => v + i)

//------------------------------------------------------------------------------
// Statement Bodies
// More expressive closure syntax.
// http://es6-features.org/#StatementBodies
//------------------------------------------------------------------------------

nums.forEach(v => {
   if (v % 5 === 0)
       fives.push(v)
})

//------------------------------------------------------------------------------
// Lexical this
// More intuitive handling of current object context.
// http://es6-features.org/#Lexicalthis
//------------------------------------------------------------------------------

this.nums.forEach((v) => {
    if (v % 5 === 0)
        this.fives.push(v)
})

//------------------------------------------------------------------------------
// Additional Examples of Anonymous Functions
//------------------------------------------------------------------------------

const anonymousFunction1 = function() {
    console.log("Anonymous function 1");
};

const anonymousFunction2 = function(a, b) {
    return a + b;
};

setTimeout(function() {
    console.log("This runs after 1 second");
}, 1000);

document.addEventListener('click', function(event) {
    console.log("Document was clicked", event);
});

const array = [1, 2, 3, 4, 5];
const doubled = array.map(function(num) {
    return num * 2;
});
console.log(doubled);

const filtered = array.filter(function(num) {
    return num > 2;
});
console.log(filtered);

const sum = array.reduce(function(acc, num) {
    return acc + num;
}, 0);
console.log(sum);

function outerFunction() {
    return function() {
        console.log("Inner anonymous function");
    };
}
const innerFunction = outerFunction();
innerFunction();

const objectWithMethod = {
    method: function() {
        console.log("Method in an object using an anonymous function");
    }
};
objectWithMethod.method();

const constructorFunction = function(name) {
    this.name = name;
    this.sayName = function() {
        console.log("My name is " + this.name);
    };
};
const instance = new constructorFunction("Anonymous");
instance.sayName();