# JSpark
A simple programming language for the JVM. JSpark was designed to be a better Java so there is a lot of syntactic and behavioral overlap.


## Hello World

A basic Hello World program written in JSpark

	static MainClass {

		def main(){
			println("Hello World!");
		}

	}

## Math Oriented

Here are two examples of a program which displays the prime numbers up to a given parameter n. One is written in JSpark, and the directly equivalent algorithm in shown in Java.


**JSpark**

	def primes(int n) {
		for n :: if([x : sqrt(n) if i %% x] > 2) :: println(n);
	}

**Java:**

	public void primes(int n){
		for(int i = 0; i < n; i++){
			test: {
				if (i % 2 == 0){
					continue;
				}
				for(int j = 3; j <= Math.sqrt(n); j += 3){
					if (i % j == 0){
						break test;
					}
				}
				System.out.println(i);
			}
		}
	}
	
	

# Coolest Features
________________________


### Trivial `for` loops


	for 10 {
		Console.println(i); //Prints 0-9; i is the default variable
	}
	
	for j : 10 { //It is also easy to choose a variable name, parentheses are optional
		Console.println(j); //0-9
	}
	
	
### Return-then statements

Ever had to store some result as a local variable before you return it to perform some additional cleanup/logic?

**Java code:**

	void test(){
		Object temp = obj;
		obj = null;
		return temp;
	}


**JSpark equivalent with return-then:**

	def test(){
		return obj then obj = null;
	}
	

### Explicit single line blocks


	for n :: Console.println(i); //Easily and explicitly define single line blocks as opposed to dropping parentheses in Java

