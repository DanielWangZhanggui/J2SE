package per.daniel.demo.courrency.blockingQueue;

import java.util.concurrent.CountDownLatch;

/**
 * Created with IntelliJ IDEA.
 * User: daniel
 * Date: 7/17/14
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class CountDownLacthTest
{
    public static void main(String []args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        new Thread(new Guest(latch, "Annie")).start();
        Thread.sleep(1000);
        new Thread(new Guest(latch, "Bob")).start();
        Thread.sleep(1000);
        new Thread(new Guest(latch, "Charls")).start();
        Thread.sleep(1000);

        new Thread(new Host(latch)).start();
    }
}

class Guest implements Runnable
{
   private CountDownLatch latch = null;

   private String name;

   public Guest(CountDownLatch latch, String name) {
        this.latch = latch;
        this.name = name;
    }

    @Override
    public void run() {
        latch.countDown();
        System.out.println(name + " is ready");
    }
}

class Host implements Runnable
{
    Host(CountDownLatch latch) {
        this.latch = latch;
    }

    private CountDownLatch latch = null;


    @Override
    public void run() {
        try {
            latch.await();
            System.out.println("Every body is ready, let's shoot!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}