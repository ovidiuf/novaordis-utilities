package io.novaordis.utilities.ac;


import java.util.ArrayList;
import java.util.List;

/**
 * Not thread safe on account of better performance. Normally it should not be accessed concurrently, all the
 * initialization is done before events are pushed to handlers. To review this.
 */
public class Handlers implements Distributor {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<Handler> handlers;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Handlers() {

        this.handlers = new ArrayList<>();
    }

    // Distributor implementation --------------------------------------------------------------------------------------

    /**
     * @see Distributor#process(Collected)
     */
    @Override
    public void process(Collected c) {

        for(Handler h: handlers) {

            // precaution to allow the other handlers to run in case one misbehaves

            try {

                Object payload = c.getPayload();

                if (h.canHandle(payload)) {

                    h.handle(c.getTimeStamp(), c.getThreadName(), payload);
                }
            }
            catch(Throwable t) {

                System.err.println("[warning] handler " + h + " failed to process event: " + t);
            }
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public boolean add(Handler h) {

        return handlers.add(h);
    }

    public boolean remove(Handler h) {

        return handlers.remove(h);
    }

    public void close() {

        for(Handler h: handlers) {

            // precaution to allow the other handlers to notified of shutdown in case one misbehaves

            try {

                h.close();
            }
            catch(Throwable t) {

                System.err.println("[warning] handler " + h + " failed to close: " + t);
            }
        }
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
