/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;
    private final static notificator notification = new notificator();

    private final int NDATA = MAXVALUE / NTHREADS;
    private PrimeFinderThread pft[];
    
    private Control( ) {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, notification);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, notification);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < NTHREADS; i++){
                pft[i].start();
            }

            boolean anyAlive = true;
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            while (anyAlive) {
                Thread.sleep(TMILISECONDS);
                // pause all worker threads
                notification.pause();

                // wait a short moment to let threads reach the check
                Thread.sleep(50);

                // count primes found so far
                int total = 0;
                for (int i = 0; i < NTHREADS; i++) {
                    total += pft[i].getPrimes().size();
                }
                System.out.println("Pausing. Primes found so far: " + total + ". Press ENTER to continue...");

                // wait for ENTER
                br.readLine();

                // resume all
                notification.resume();

                // check if any thread is still alive
                anyAlive = false;
                for (int i = 0; i < NTHREADS; i++) {
                    if (pft[i].isAlive()) {
                        anyAlive = true;
                        break;
                    }
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
}
