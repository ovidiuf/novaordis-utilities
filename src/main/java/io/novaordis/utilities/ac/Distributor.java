package io.novaordis.utilities.ac;

public interface Distributor {

    /**
     * Pushes the collected instance to *all* registered handlers that would accept it.
     *
     * @see Handler#canHandle(Object)
     *
     * Never this method should throw checked or unchecked exceptions. The upper layer will swallow them as it is not
     * allowed for the draining pump to stop.
     *
     * @see Handler#handle(long timestamp, String originatorThreadName, Object o)
     */
    void process(Collected c);
}
