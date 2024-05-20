function sum(x, y, z) {
  return x + y + z;
}

const numbers = [1, 2, 3];

console.log(sum(...numbers));//1 spread
// Expected output: 6

console.log(sum.apply(null, numbers));
// Expected output: 6


// Exemplos de uso de spread arguments que devem ser capturados
function sum(...numbers) {
  return numbers.reduce((acc, curr) => acc + curr, 0);
}

const person = {
  name: 'John',
  age: 30
};

const updatedPerson = {
  ...person,//1 spread
  age: 35
};

console.log(updatedPerson); // Output: { name: 'John', age: 35 }

const arr1 = [1, 2, 3];
const arr2 = [4, 5, 6];
const mergedArray = [...arr1, ...arr2];//2 spreads

function greetMany(greeting, ...names) {
  names.forEach(name => {
    console.log(`${greeting}, ${name}!`);
  });
}

const obj1 = { x: 1, y: 2 };
const obj2 = { z: 3 };
const mergedObject = { ...obj1, ...obj2 };//2 spreads

// Exemplos que não devem ser capturados
const str = "This is a string with spread arguments"; // Não deve ser capturado
const templateStr = `Function with spread arguments: sum(...numbers)`; // Não deve ser capturado

// Comentários que contêm a sintaxe de spread arguments, que não devem ser capturados
// Function with spread arguments: sum(...numbers)
/* Another comment with mergedArray = [...arr1, ...arr2] */

// Código adicional que não deve ser capturado
let result1 = sum(1, 2, 3); // Chamada de função com spread arguments
let result2 = mergedArray.length; // Acesso ao comprimento do array resultante da junção
greetMany('Hello', 'Alice', 'Bob'); // Chamada de função com spread arguments
let result3 = mergedObject.z; // Acesso à propriedade do objeto resultante da fusão
