package io.novaordis.utilities.ac;

public class Collected {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private long timeStamp;
    private String threadName;
    private Object payload;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Collected(long timestamp, Object o) {

        this(timestamp, Thread.currentThread().getName(), o);
    }

    public Collected(long timestamp, String threadName, Object o) {

        this.timeStamp = timestamp;
        this.threadName = threadName;
        this.payload = o;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public Object getPayload() {
        return payload;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getThreadName() {
        return threadName;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
