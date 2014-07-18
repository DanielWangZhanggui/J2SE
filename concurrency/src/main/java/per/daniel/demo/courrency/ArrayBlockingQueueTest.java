package per.daniel.demo.courrency;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by daniel on 7/15/14.
 */
public class ArrayBlockingQueueTest {
    public static void main(String args[]) throws InterruptedException {
        BlockingQueue queue = new ArrayBlockingQueue(5);

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        Thread proThread = new Thread(producer);
        Thread conThread = new Thread(consumer);

        proThread.start();
        conThread.start();

        proThread.join();
        conThread.join();

        System.out.println("Done..");


    }

}

//The producing thread will keep producing new objects and insert them into the queue,
// until the queue reaches some upper bound on what it can contain.
// If the blocking queue reaches its upper limit, the producing thread is blocked while trying to insert the new object.
// It remains blocked until a consuming thread takes an object out of the queue.
class Producer implements Runnable
{
    private BlockingQueue queue = null;

    public Producer(BlockingQueue queue)
    {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random randomGenerator = new Random();

        for(int i=0; i< 10; i++)
        {
            try {
                queue.put(i);
                System.out.println("Putting\t" + i);
                Thread.sleep(randomGenerator.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Producer exited");
    }
}

class Consumer implements Runnable
{
    private BlockingQueue queue = null;

    public Consumer(BlockingQueue queue)
    {
        this.queue = queue;
    }
    @Override
    public void run() {
        Random randomGenerator = new Random();

        for(int i=0; i<10; i++)
        {
            try {
                System.out.println(queue.take());
                Thread.sleep(randomGenerator.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Consumer exited");
    }
}