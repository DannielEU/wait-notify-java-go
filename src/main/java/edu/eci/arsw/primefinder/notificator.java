package edu.eci.arsw.primefinder;

public class notificator {
    boolean pause = false;

    public synchronized void pause(){
         this.pause = true;
    }
    public synchronized  void resume(){
        this.pause = false;
        notifyAll();
    }
    public synchronized void checkpause() throws InterruptedException{
        while(pause){
            wait();
        }
    }
}
