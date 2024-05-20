"use strict";
//------------------------------------------------------------------------------
// Value Export/Import
// Support for exporting/importing values from/to modules without global namespace pollution.
// http://es6-features.org/#ValueExportImport
//------------------------------------------------------------------------------

//  lib/math.js
export function sum (x, y) { return x + y }
export var pi = 3.141593

//  someApp.js
import * as math from "lib/math"
console.log("2π = " + math.sum(math.pi, math.pi))

//  otherApp.js
import { sum, pi } from "lib/math"
console.log("2π = " + sum(pi, pi))

//------------------------------------------------------------------------------
// Default & Wildcard
// Marking a value as the default exported value and mass-mixin of values.
// http://es6-features.org/#DefaultWildcard
//------------------------------------------------------------------------------

//  lib/mathplusplus.js
export * from "lib/math"
export var e = 2.71828182846
export default (x) => Math.exp(x)

//  someApp.js
import exp, { pi, e } from "lib/mathplusplus"
console.log("e^{π} = " + exp(pi))

export {};
import a,{aaa as bbb} from 'ccc';
import('runtime_import');

import React from 'react';

import { name as squareName, draw } from "./shapes/square.js";
import { name as circleName } from "https://example.com/shapes/circle.js";


// main.js
import * as mod from "/my-module.js";

import("/my-module.js").then((mod2) => {
  // Logs "then() called"
  console.log(mod === mod2); // false
});

// my-module.js
export function then(resolve) {
  console.log("then() called");
  resolve(1);
}

export default function add(a, b) {
  return a + b;
}

import { func1, func2 } from './module1';
import * as module2 from './module2';
import defaultFunc from './module3';