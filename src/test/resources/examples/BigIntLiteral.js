const previousMaxSafe = BigInt(Number.MAX_SAFE_INTEGER);
//  9007199254740991

const maxPlusOne = previousMaxSafe + 1n;
//  9007199254740992n

const theFuture = previousMaxSafe + 2n;
//  9007199254740993n, this is works!

const multi = previousMaxSafe * 2n;
//  18014398509481982n

const subtr = multi - 10n;
//  18014398509481972n

const mod = multi % 10n;
//  2n

const bigN = 2n ** 54n;
//  18014398509481984n

bigN * -1n
//  –18014398509481984n
