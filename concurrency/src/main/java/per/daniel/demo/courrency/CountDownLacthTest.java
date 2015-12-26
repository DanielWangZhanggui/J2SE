package per.daniel.demo.courrency;

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
	System.out.println("Hello World!");
        new Thread(new Boss(latch)).start();
        Thread.sleep(1000);

        new Thread(new Employee(latch, "Annie")).start();
        Thread.sleep(1000);
        new Thread(new Employee(latch, "Bob")).start();
        Thread.sleep(1000);
        new Thread(new Employee(latch, "Charls")).start();
    }
}

class Employee implements Runnable
{
   private CountDownLatch latch = null;

   private String name;

   public Employee(CountDownLatch latch, String name) {
        this.latch = latch;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + " is ready");
        latch.countDown();
        System.out.println(name + "is working now");
    }
}

class Boss implements Runnable
{
    Boss(CountDownLatch latch) {
        this.latch = latch;
    }

    private CountDownLatch latch = null;


    @Override
    public void run() {
        try {
            System.out.println("Boss is monitoring");
            latch.await();
            System.out.println("Boss: Every body is available, awesome!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
