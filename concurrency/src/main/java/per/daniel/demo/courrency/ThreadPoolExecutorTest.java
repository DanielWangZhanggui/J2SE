package per.daniel.demo.courrency;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: daniel
 * Date: 7/18/14
 * Time: 8:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadPoolExecutorTest
{
   public static void main(String args [])
   {
       CallableTask a = new CallableTask("Annie" ,1000);
       CallableTask b = new CallableTask("Bob" ,2000);
       CallableTask c = new CallableTask("Charls" ,3000);

       FutureTask<String> futureTaskA = new FutureTask<String>(a);
       FutureTask<String> futureTaskB = new FutureTask<String>(b);
       FutureTask<String> futureTaskC = new FutureTask<String>(c);

       RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
       //Get the ThreadFactory implementation to use
       ThreadFactory threadFactory = Executors.defaultThreadFactory();

       ExecutorService executor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1), threadFactory, rejectionHandler);
       executor.execute(futureTaskA);
       executor.execute(futureTaskB);
       executor.execute(futureTaskC);


       while (true) {
           try {
               if(futureTaskA.isDone() && futureTaskB.isDone() && futureTaskC.isDone()){
                   System.out.println("Complete");
                   //shut down executor service
                   executor.shutdown();
                   return;
               }

               if(!futureTaskA.isDone()){
                   //wait indefinitely for future task to complete
                   System.out.println(futureTaskA.get() + "\t survived!!");
               }

               System.out.println("Waiting for other guys to be survived");
               if(!futureTaskB.isDone()){
                   String name = futureTaskB.get(200L, TimeUnit.MILLISECONDS);
                   if(name != null){
                       System.out.println(name + "\t survived!!");
                   }
               }

               // due to futureTaskC has been rejected, so it will be loop.
               if(!futureTaskC.isDone()){
                   String name = futureTaskC.get(200L, TimeUnit.MILLISECONDS);
                   if(name != null){
                       System.out.println(name + "\t survived!!");
                   }
               }


           }  catch (InterruptedException e) {
               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
           } catch (ExecutionException e) {
               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
           } catch (TimeoutException e) {
                // do nothing.
           }
       }

   }
}

class CallableTask implements Callable<String>
{
    private String name;
    private int sleepingTime;

    CallableTask(String name, int sleepingTime) {
        this.name = name;
        this.sleepingTime = sleepingTime;
    }

    @Override
    public String call() throws Exception {
        Thread.sleep(sleepingTime);

        return name;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println(r.toString() + " is rejected");

    }

}
