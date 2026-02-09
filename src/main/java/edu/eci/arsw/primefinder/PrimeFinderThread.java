package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	
	private List<Integer> primes;
	private final notificator notification;
	
	public PrimeFinderThread(int a, int b) {
		this(a,b,null);
	}

	public PrimeFinderThread(int a, int b, notificator notification) {
		super();
		this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
		this.notification = notification;
	}

		@Override
		public void run(){
			for (int i= a;i < b;i++){
				try {
					if (notification != null) {
						notification.checkpause();
					}
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					break;
				}
				if (isPrime(i)){
					primes.add(i);
					System.out.println(i);
				}
			}
		}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
	
}
