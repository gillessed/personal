function isPrime(n) {
    if(n <= 1) {
        return false;
    }
    if(n == 2) {
        return true;
    }
    division = false;
    for(i = 2; i <= (n + 1) / 2; i++) {
        if(n % i == 0) {
            division = true;
            i = n;
        }
    }
    return !division;
}

return isPrime(args[0]);
