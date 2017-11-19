# JSpark
A simple programming language for the JVM


## Hello World

A basic Hello World program written in JSpark

	static MainClass {

		def main(){
			println("Hello World!");
		}

	}

## Math Oriented

Here are two examples of a program which displays the prime numbers up to a given parameter n. One is written in JSpark, and the directly equivalent algorithm in shown in Java.


### JSpark

	def primes(int n){
		i = [1, n) :: if i|*.length == 2 :: println(i);
	}

### Java:

	public static void primes(int n){
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