package io.novaordis.utilities.ac;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CollectorImpl implements Collector {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final long PUMP_SLEEP_AFTER_INTERRUPTED_EXCEPTION_MS = 100L;

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String name;

    private final BlockingQueue<Collected> collectionQueue;

    private Pump pump;
    private Thread drainingThread;

    private Handlers handlers;

    private Timer maintenanceTimer;

    private volatile boolean outOfBusiness;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Constructs a collector configured with the default draining thread priority.
     */
    public CollectorImpl(String name) {

        this(name, -1);
    }

    /**
     * @param drainingThreadPriority - between Thread.MIN_PRIORITY and Thread.MAX_PRIORITY. If -1, use default priority.
     */
    public CollectorImpl(final String name, int drainingThreadPriority) {

        this.outOfBusiness = false;
        this.name = name;
        collectionQueue = new LinkedBlockingQueue<Collected>();

        this.handlers = new Handlers();

        this.pump = new Pump();
        drainingThread = new Thread(pump, "' Collector '" + name + "' Draining Thread");
        drainingThread.setDaemon(true);

        if (drainingThreadPriority != -1) {

            drainingThread.setPriority(drainingThreadPriority);
        }

        drainingThread.start();

        this.maintenanceTimer = new Timer("Collector '" + name + "' Maintenance Timer", true);

        maintenanceTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                int size = collectionQueue.size();

                if (size > 1000) {

                    System.err.println("[warning] collector " + name + " queue over " + size + " entries");
                }
            }
        }, 20000L, 20000L);
    }

    // Collector interface ---------------------------------------------------------------------------------------------

    @Override
    public String getName() {

        return name;
    }

    @Override
    public String getThreadName() {

        return drainingThread == null ? null : drainingThread.getName();
    }

    @Override
    public boolean handOver(Object o) {

        if (outOfBusiness) {

            return false;
        }

        // assign timestamp at the collection time, as object may spend time in the queue waiting to be processed

        long timestamp = System.currentTimeMillis();
        collectionQueue.add(new Collected(timestamp, o));
        return true;
    }

    @Override
    public boolean registerHandler(Handler h) {

        return !outOfBusiness && handlers.add(h);
    }

    /**
     * @see Collector#unregisterHandler(Handler)
     */
    @Override
    public boolean unregisterHandler(Handler h) {

        return handlers.remove(h);
    }

    @Override
    public void dispose() {

        outOfBusiness = true;
        pump.stop();
        maintenanceTimer.cancel();
        handlers.close();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return "Collector[" + name + "], thread=" + getThreadName();
    }

    // Package Protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

    private class Pump implements Runnable {

        private volatile boolean active;

        Pump() {

            this.active = true;
        }

        @Override
        public void run() {

            // attempt to read the queue forever and dump everything that comes from it

            //noinspection InfiniteLoopStatement
            while(active) {

                Collected c;

                try {

                    c = collectionQueue.take();
                }
                catch(InterruptedException e) {

                    // nothing, sleep a bit and go back to the queue

                    try {

                        Thread.sleep(PUMP_SLEEP_AFTER_INTERRUPTED_EXCEPTION_MS);
                    }
                    catch(InterruptedException e2) {

                        // nothing
                    }

                    continue;
                }

                try {

                    // even if the underlying implementation throws unchecked exception, the pump must handle them
                    // without stopping
                    handlers.process(c);
                }
                catch(Throwable t) {

                    // never stop the pump as the collectionQueue may grow out of control; keep consuming, even if
                    // we discard everything we consume
                    System.err.println("[warning] collector " + name + " is failing to process events: " + t);
                }
            }
        }

        void stop() {

            active = false;
        }
    }
}
