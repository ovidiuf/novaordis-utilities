package io.novaordis.utilities.ac;

public interface Handler {
    /**
     * Never this method should throw checked or unchecked exceptions. The upper layer will swallow them as it is not
     * allowed for the draining pump to stop.
     *
     * @param o the instance submitted to the collector earlier, on a different thread. Be prepared for the possibility
     *        that o is null.
     */
    boolean canHandle(Object o);

    /**
     * Never this method should throw checked or unchecked exceptions. The upper layer will swallow them as it is not
     * allowed for the draining pump to stop.
     *
     * It is guaranteed that Object instance provided is the same one that was provided before to canHandle().
     */
    void handle(long timestamp, String originatorThreadName, Object o);

    /**
     * Notifies the handler that it is not needed anymore and it can dispose of whatever resources it is holding.
     */
    void close();

}
