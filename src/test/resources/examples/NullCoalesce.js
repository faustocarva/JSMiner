const foo = null ?? 'default string';
console.log(foo);
// Expected output: "default string"

const baz = 0 ?? 42;
console.log(baz);
// Expected output: 0

const nullValue = null;
const emptyText = ""; // falsy
const someNumber = 42;

const valA = nullValue ?? "default for A";
const valB = emptyText ?? "default for B";
const valC = someNumber ?? 0;

console.log(valA); // "default for A"
console.log(valB); // "" (as the empty string is not null or undefined)
console.log(valC); // 42


// Exemplos de uso em strings e comentários que não devem ser capturados
const str = "This is a string with ?? inside"; // Não deve ser capturado
const templateStr = `Another string with ?? and ${a}`; // Não deve ser capturado

// Comentários que contêm o operador de coalescência nula, que não devem ser capturados
// This is a comment with ?? operator
/* Another comment with ?? inside */

// Código adicional que não deve ser capturado
let x = 10;
let y = null;
let z = x + y; // Operação regular sem operador de coalescência nula