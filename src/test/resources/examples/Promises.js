// Uso de Promise: Resolvendo uma promessa após um intervalo de tempo
function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

// Uso de Promise: Encadeando múltiplas promessas
delay(1000)
    .then(() => {
        console.log('Passaram-se 1 segundo.');
        return delay(2000);
    })
    .then(() => {
        console.log('Passaram-se mais 2 segundos.');
    });

// Uso de Promise: Rejeitando uma promessa
function errorPromise() {
    return new Promise((resolve, reject) => {
        setTimeout(() => reject(new Error('Erro ocorrido!')), 3000);
    });
}

errorPromise()
    .then(() => console.log('Não deve ser executado'))
    .catch(error => console.error(error.message));

// Uso de Promise: Utilizando Promise.all para esperar múltiplas promessas
Promise.all([
    delay(500),
    delay(1000),
    delay(1500)
]).then(() => {
    console.log('Todas as promessas foram resolvidas.');
});

// Uso de Promise: Tratando erro em Promise.all
Promise.all([
    delay(200),
    delay(400).then(() => Promise.reject(new Error('Erro')))
]).then(() => {
    console.log('Todas as promessas foram resolvidas.');
}).catch(error => {
    console.error('Pelo menos uma promessa foi rejeitada:', error.message);
});

// Uso de Promise: Utilizando Promise.race para resolver com a primeira promessa resolvida
Promise.race([
    delay(3000).then(() => 'Promessa 1'),
    delay(2000).then(() => 'Promessa 2')
]).then(result => {
    console.log('A primeira promessa resolvida:', result);
});

// Não deve ser capturado: Uso de Promise sem encadeamento
delay(100).then(() => console.log('Não deve ser capturado'));

// Não deve ser capturado: Comentário que não contém uso de Promise
// Este é um comentário normal.

// Não deve ser capturado: Código adicional que não está diretamente relacionado a Promise
const x = 10;
console.log('Valor de x:', x);

someFunction(/* Promise is not used here */);