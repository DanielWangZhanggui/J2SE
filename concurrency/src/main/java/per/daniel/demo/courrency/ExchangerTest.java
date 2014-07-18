package per.daniel.demo.courrency;

import java.util.concurrent.Exchanger;

/**
 * Created with IntelliJ IDEA.
 * User: daniel
 * Date: 7/17/14
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExchangerTest
{
    public static void main(String []args)
    {
        Exchanger<String> exchanger  = new Exchanger<String>();
        new Thread(new Client(exchanger)).start();
        new Thread(new Waiter(exchanger)).start();
    }

}

class Client implements Runnable
{
    private Exchanger<String> clientExchanger;

    public Client(Exchanger<String> clientExchanger) {
        this.clientExchanger = clientExchanger;
    }
    @Override
    public void run() {
        String reply = null;

        try {
            reply = clientExchanger.exchange("Hello, may I help you?");
            Thread.sleep(10);
            System.out.println("Waiter: " + reply);
            reply = clientExchanger.exchange("Sure, what kind of drinks do you want?");
            Thread.sleep(10);
            System.out.println("Waiter: " + reply);
            reply = clientExchanger.exchange("Here you are");
            Thread.sleep(10);
            System.out.println("Waiter: " + reply);
            reply = clientExchanger.exchange("You're welcome");
            Thread.sleep(10);
            System.out.println("Waiter: " + reply);

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

class Waiter implements Runnable
{
    private Exchanger<String> waiterExchanger;

    public Waiter(Exchanger<String> waiterExchanger) {
        this.waiterExchanger = waiterExchanger;
    }

    @Override
    public void run() {
        String reply = null;

        try {
            reply = waiterExchanger.exchange("Hello, I'd like to something to drink.");

            System.out.println("Client: " + reply);
            reply = waiterExchanger.exchange("Cappuccino, please.");

            System.out.println("Client: " + reply);
            reply = waiterExchanger.exchange("Thank you!");

            System.out.println("Client: " + reply);

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}