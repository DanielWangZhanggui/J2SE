package per.daniel.demo.courrency;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: daniel
 * Date: 7/17/14
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class CyclicBarrierTest
{
     public static void main(String []args)
     {
         Phase one = new Phase("Phase One");
         CyclicBarrier phase1 = new CyclicBarrier(2, one);
         Phase two = new Phase("Phase Two");
         CyclicBarrier phase2 = new CyclicBarrier(2, two);

         TeamGoal goalPhase1 = new  TeamGoal(phase1, phase2);
         TeamGoal goalPhase2 = new  TeamGoal(phase1, phase2);

         new Thread(goalPhase1).start();
         new Thread(goalPhase2).start();
     }
}

class Phase implements Runnable
{

    private String name;

    public Phase(String name) {
        this.name = name;
    }

    @Override
    public void run() {
         System.out.println(name + " Completed");
    }
}

class TeamGoal implements Runnable
{
    private CyclicBarrier phase1;
    private CyclicBarrier phase2;

    public TeamGoal(CyclicBarrier phase1, CyclicBarrier phase2) {
        this.phase1 = phase1;
        this.phase2 = phase2;
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " is working at phase 1");
            Thread.sleep(1000);
            this.phase1.await();

            System.out.println(Thread.currentThread().getName() +
                    " is working at phase 2");
            Thread.sleep(1000);
            this.phase2.await();

            System.out.println(Thread.currentThread().getName() +
                    " Done! Yay!!");
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BrokenBarrierException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}