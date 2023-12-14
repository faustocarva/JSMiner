async function f(){}

class C {
    async method(){}
    static async method1(){}
    static async #method2(){}
    async *gen(){}
}
async ()=>{};

async ()=>{await f();}
async ()=>{await promise;}
async ()=>{await 1;}

async function* gen(){}

const asyncIterator = (async function* () {
  yield 1;
  yield 2;
  yield 3;
})();

(async () => {
  for await (const value of asyncIterator) {
    console.log(value);
  }
})();
// Logs: 1, 2, 3