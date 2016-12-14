package io.novaordis.utilities.ac;


public class CollectorFactory {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Returns a Collector instance with the given name. The name will be used as a component of the name of the pump
     * thread.
     */
    public static Collector getInstance(String name) {

        // currently we're creating a new instance ever time we're called. Review this
        return new CollectorImpl(name);
    }

    /**
     * Returns a Collector instance with the given name. The name will be used as a component of the name of the pump
     * thread.
     */
    public static Collector getInstance(String name, int drainThreadPriority) {

        // currently we're creating a new instance ever time we're called. Review this
        return new CollectorImpl(name, drainThreadPriority);
    }


    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Collector interface ---------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package Protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
