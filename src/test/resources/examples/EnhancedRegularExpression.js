"use strict";
//------------------------------------------------------------------------------
// Regular Expression Sticky Matching
// Keep the matching position sticky between matches and this way support
//   efficient parsing of arbitrary long input strings, even with an arbitrary
//   number of distinct regular expressions.
// http://es6-features.org/#RegularExpressionStickyMatching
//------------------------------------------------------------------------------

let parser = (input, match) => {
    for (let pos = 0, lastPos = input.length; pos < lastPos; ) {
        for (let i = 0; i < match.length; i++) {
            match[i].pattern.lastIndex = pos
            let found
            if ((found = match[i].pattern.exec(input)) !== null) {
                match[i].action(found)
                pos = match[i].pattern.lastIndex
                break
            }
        }
    }
}

let report = (match) => {
    console.log(JSON.stringify(match))
}
parser("Foo 1 Bar 7 Baz 42", [
    { pattern: /^Foo\s+(\d+)/y, action: (match) => report(match) },
    { pattern: /^Bar\s+(\d+)/y, action: (match) => report(match) },
    { pattern: /^Baz\s+(\d+)/y, action: (match) => report(match) },
    { pattern: /^\s*/y,         action: (match) => {}            }
])

const regex1 = /(?<=@)\w+/;
const match1 = regex1.exec('user@example.com');

const regex = /(?<year>\d{4})-(?<month>\d{2})-(?<day>\d{2})/;
const match = regex.exec('2022-01-01');

console.log(match.groups.year); // Output: 2022
console.log(match.groups.month); // Output: 01
console.log(match.groups.day); // Output: 01


//Old method before ECMAScript 2015
var regexOld = /\(\d{3}\)\s\d{4}-\d{4}/;
var telefone = "(123) 4567-8901";

if (regexOld.test(telefone)) {
    console.log("Número de telefone válido.");
} else {
    console.log("Número de telefone inválido.");
}