package per.daniel.demo.courrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: daniel
 * Date: 7/16/14
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class DelayQueueTest {
    public static void main(String[] args) throws Exception {
        Cache<Integer, String> cache = new Cache<Integer, String>();

        for(int i=0; i<10; i++)
        {
           System.out.println(cache);
           Thread.sleep(500);
           cache.put(i, "value", 1, TimeUnit.SECONDS);
        }
    }
}

class DelayedItem<T> implements Delayed {

    private static final long NANO_ORIGIN = System.nanoTime();


    final static long now() {
        return System.nanoTime() - NANO_ORIGIN;
    }


    private static final AtomicLong sequencer = new AtomicLong(0);


    private final long sequenceNumber;


    private final long time;

    private final T item;

    public DelayedItem(T submit, long timeout) {
        this.time = now() + timeout;
        this.item = submit;
        this.sequenceNumber = sequencer.getAndIncrement();
    }

    public T getItem() {
        return this.item;
    }

    public long getDelay(TimeUnit unit) {
        long d = unit.convert(time - now(), TimeUnit.NANOSECONDS);
        return d;
    }
    //Use this method to sort the elements in collections.
    public int compareTo(Delayed other) {
        if (other == this)
            return 0;
        if (other instanceof DelayedItem) {
            DelayedItem x = (DelayedItem) other;
            long diff = time - x.time;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else if (sequenceNumber < x.sequenceNumber)
                return -1;
            else
                return 1;
        }
        long d = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }
}

class Pair<K, V> {
    public K key;

    public V value;

    public Pair() {}

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

class Cache<K, V> {
    private static final Logger LOG = Logger.getLogger(Cache.class.getName());

    private ConcurrentMap<K, V> cacheObjMap = new ConcurrentHashMap<K, V>();

    private DelayQueue<DelayedItem<Pair<K, V>>> q = new DelayQueue<DelayedItem<Pair<K, V>>>();

    private Thread daemonThread;

    public Cache() {

        Runnable daemonTask = new Runnable() {
            public void run() {
                daemonCheck();
            }
        };

        daemonThread = new Thread(daemonTask);
        daemonThread.setDaemon(true);
        daemonThread.setName("Cache Daemon");
        daemonThread.start();
    }

    private void daemonCheck() {

        if (LOG.isLoggable(Level.INFO))
            LOG.info("cache service started.");

        for (;;) {
            try {
                //Retrieve and remove the head of this queue, waiting if necessary until an element with an expired delay is available on this queue.
                DelayedItem<Pair<K, V>> delayItem = q.take();
                if (delayItem != null) {
                    Pair<K, V> pair = delayItem.getItem();
                    //Remove the item from cache.
                    cacheObjMap.remove(pair.key, pair.value);
                }
            } catch (InterruptedException e) {
                if (LOG.isLoggable(Level.SEVERE))
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                break;
            }
        }

        if (LOG.isLoggable(Level.INFO))
            LOG.info("cache service stopped.");
    }


    public void put(K key, V value, long time, TimeUnit unit) {
        V oldValue = cacheObjMap.put(key, value);
        if (oldValue != null)
            q.remove(key);

        long nanoTime = TimeUnit.NANOSECONDS.convert(time, unit);
        q.put(new DelayedItem<Pair<K, V>>(new Pair<K, V>(key, value), nanoTime));
    }

    public V get(K key) {
        return cacheObjMap.get(key);
    }

    public String toString()
    {
       return cacheObjMap.toString();
    }
}