package io.novaordis.utilities.ac;

public interface Collector {

    String getName();

    /**
     * May return null if the draining thread has been shut down, or the implementation is not multi-threaded.
     */
    String getThreadName();

    /**
     * Hand over the object to be processed on a different thread.
     *
     * @return true if the object was accepted for handling or false if the object was rejected (which may be the case
     *         if the method dispose() was called on this Collector instance. If the method returns false, then you
     *         should expect no further processing of the handed over object.
     */
    boolean handOver(Object o);

    /**
     * Registers an external handler. Same handler can be registered multiple times.
     *
     * @return false if dispose() was previously called on this instance - and the handler will not be registered.
     */
    boolean registerHandler(Handler h);

    /**
     * @return true if the handler was found and removed, false if the handler was not found. true may be returned even
     *         after dispose() method was called.
     */
    boolean unregisterHandler(Handler h);

    /**
     * Once called, the collector frees up the internal resources it was holding onto and makes itself available for
     * GC, if no any other references are being maintained to it. A call on most methods will return false and will
     * result in a noop. The implementation must notify the currently registered handlers that is about to be disposed
     * by calling close() on those instances.
     */
    void dispose();

}
