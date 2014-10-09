package com.gillessed.euler.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	public static boolean isPrime(long n) {
		return MillerRabin.miller_rabin(n);
	}
	public static List<Long> primeFactors(long i) {
		long testNum = i;
		long n = 2;
		List<Long> factors = new ArrayList<Long>();
		while(testNum > 1) {
			if(isPrime(n) && testNum % n == 0){
				factors.add(n);
				testNum /= n;
			} else {
				n++;
			}
		}
		return factors;
	}
	public static List<Long> primeDivisors(long i) {
		long testNum = i;
		long n = 2;
		List<Long> factors = new ArrayList<Long>();
		while(testNum > 1) {
			if(isPrime(n) && testNum % n == 0){
				if(!factors.contains(n)) factors.add(n);
				testNum /= n;
			} else {
				n++;
			}
		}
		return factors;
	}
	public static long nthPrime(long i) {
		long n = 1;
		long co = 0;
		while(co < i) {
			n++;
			if(isPrime(n)) {
				co++;
			}
		}
		return n;
	}
	public static List<Long> nPrimes(long i) {
		long n = 1;
		List<Long> primes = new ArrayList<Long>();
		while(primes.size() < i) {
			n++;
			if(isPrime(n)) {
				primes.add(n);
			}
		}
		return primes;
	}
	public static long nextPrime(long i) {
		while(!isPrime(i)) {
			i++;
		}
		return i;
	}
	public static int digitProduct(String s) {
		int prod = 1;
		for(int i = 0; i < s.length(); i++) {
			String t = s.substring(i,i+1);
			prod *= Integer.parseInt(t);
		}
		return prod;
	}
	public static int digitSum(String s) {
		int sum = 0;
		for(int i = 0; i < s.length(); i++) {
			String t = s.substring(i,i+1);
			sum += Integer.parseInt(t);
		}
		return sum;
	}
	public static List<Long> divisors(long i) {
		List<Long> divisors = new ArrayList<Long>();
		for(long n = 1; n <= i; n++) {
			if(i % n == 0) {
				divisors.add(n);
			}
		}
		return divisors;
	}
	public static List<Long> properDivisors(long i) {
		List<Long> divisors = new ArrayList<Long>();
		for(long n = 1; n < i; n++) {
			if(i % n == 0) {
				divisors.add(n);
			}
		}
		return divisors;
	}
	public static long divisorCount(long i) {
		List<Long> factors = primeFactors(i);
		long totalProduct = 1;
		long sum = 2;
		long previousValue = factors.get(0);
		for(int n = 1; n < factors.size(); n++) {
			long currentValue = factors.get(n);
			if(currentValue == previousValue) {
				sum++;
			} else {
				totalProduct *= sum;
				sum = 2;
			}
			previousValue = currentValue;
		}
		totalProduct *= sum;
		return totalProduct;
	}
	public static long triangleNumber(long i) {
		return (i + 1) * i / 2;
	}
	public static long gcd(long a, long b) {
		if(a == 0 || b == 0) return 0;
		if(a == 1 || b == 1) return 1;

		long larger = a, smaller = b;

		if(b > a) {
			larger = b; smaller = a;
		}
		while(smaller >= 1) {
			long tempSmaller = smaller;
			smaller = larger % smaller;
			larger = tempSmaller;
		}
		return larger;
	}
	public static long fib(long i){
		double phi=(1+Math.sqrt(5))/2;
		return (long)(Math.pow(phi,(double)(i-1))/Math.sqrt(5)+0.5d);
	}
	public static long listMax(List<Long> list){
		long max=list.get(0);
		for(Long l:list){if(l>max)max=l;}
		return max;
	}
	public static long listSum(List<Long> list){
		long sum = 0;
		for(Long l:list){sum += l;}
		return sum;
	}
	public static boolean isPalindrome(long i){
		String s=Long.toString(i);
		return s.equals(Utils.reverseString(s));
	}
	public static String reverseString(String s){
		char[] c=s.toCharArray();
		for(int i=0;i<s.length()/2;i++){
			c[i]=s.charAt(s.length()-(i+1));
			c[s.length()-(i+1)]=s.charAt(i);
		}
		return new String(c);
	}
	private static int phi[];
	private static boolean sieveInit = false;
	public static void initTotientSieve(int size) {
		phi = new int[(int)size+1];
		for(int i=2; i <= size; i++) phi[i] = i; //phi[1] is 0
		for(int i=2; i <= size; i++) {
			if(phi[i] == i) {
				for(int j=i; j <= size; j += i) {
					phi[j] = (phi[j]/i)*(i-1);
				}
			}
		}
		sieveInit = true;
	}
	public static long totient(long n) {
		if(sieveInit) return phi[(int)n];
		long sum = 0;
		for(long i = 2; i <= n; i++) {
			if(gcd(i, n) == 1) {
				sum++;
			}
		}
		return sum;
	}
	public static BigInteger factorial(long n) {
		BigInteger val = BigInteger.ONE;
		for(long i = 2; i <= n; i++) {
			val = val.multiply(new BigInteger(Long.toString(i)));
		}
		return val;
	}
	private static String numberMap1[] =
		{"zero", "one", "two", "three", "four",
		"five", "six", "seven", "eight", "nine"};
	private static String numberMap2[] =
		{"ten", "eleven", "twelve", "thirteen", "fourteen",
		"fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
	private static String numberMap3[] =
		{"ten", "twenty", "thirty", "fourty", "fifty",
		"sixty", "seventy", "eighty", "ninety"};
	public static String toWritten(long number) {
		String ret = "";
		if(number >= 1000) {
			long multiplier = number / 1000;
			if(multiplier > 0) {
				ret += toWritten(multiplier) + " thousand ";
			}
			ret += toWritten(number % 1000);
		} else if(number >= 100) {
			long multiplier = number / 100;
			if(multiplier > 0) {
				ret += toWritten(multiplier) + " hundred ";
			}
			if(number % 100 != 0) {
				ret += "and ";
			}
			ret += toWritten(number % 100);
		} else if(number >= 10) {
			long multiplier = number / 10;
			if(multiplier == 1) {
				ret += numberMap2[(int)(number - 10)];
			} else {
				if(multiplier > 1) {
					ret += numberMap3[(int)(multiplier - 1)];
				}
				if(number % 10 != 0) {
					ret += "-";
				}
				ret += toWritten(number % 10);
			}
		} else {
			if(number > 0) {
				return numberMap1[(int)number];
			} else {
				return "";
			}
		}
		return ret;
	}
}
