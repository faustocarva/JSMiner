// Exemplos de uso de parâmetros padrão que devem ser capturados
function greet(name = getDefaultName()) {
  console.log(`Hello, ${name}!`);
}

const calculateArea = (width = 5, height = 5) => {
  return width * height;
};

const greetMultiple = (name = 'World', greeting = 'Hello') => {
  console.log(`${greeting}, ${name}!`);
};

const obj = {
  greet(name = 'World') {
    console.log(`Hello, ${name}!`);
  }
};

function greetRest(greeting = 'Hello', ...names) {
  console.log(`${greeting}, ${names.join(' and ')}!`);
}

// Exemplos que não devem ser capturados
const str = "This is a string with default parameters"; // Não deve ser capturado
const templateStr = `Function with default parameters: greet(name = 'World')`; // Não deve ser capturado

// Comentários que contêm a sintaxe de parâmetros padrão, que não devem ser capturados
// Function with default parameters: greet(name = 'World')
/* Another comment with calculateArea(width = 5, height = 5) */

// Código adicional que não deve ser capturado
let result1 = greet(); // Chamada de função sem argumentos, usará o valor padrão
let result2 = calculateArea(10); // Chamada de função com um argumento
let result3 = calculateArea(10, 20); // Chamada de função com dois argumentos específicos
let result4 = greetMultiple(); // Chamada de função com parâmetros padrão
obj.greet(); // Chamada de método de objeto sem argumentos
greetRest('Hola', 'Alice', 'Bob'); // Chamada de função com parâmetros rest e um argumento padrão
