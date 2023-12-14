let a = 3;

console.log((a **= 2));
// Expected output: 9

console.log((a **= 0));
// Expected output: 1

console.log((a **= 'hello'));
// Expected output: NaN

let bar = 5;

bar **= 2; // 25
bar **= "foo"; // NaN

let foo = 3n;
foo **= 2n; // 9n