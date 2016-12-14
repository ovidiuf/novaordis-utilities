package io.novaordis.utilities.ac.handler;

import io.novaordis.utilities.ac.Handler;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * An example of CSV file handler.
 */
public class ToCsv implements Handler {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final Format TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // we don't want it to be buffered, we want to go to disk as soon as possible when a line is ready, we won't
    // block anything because the work is done on a dedicated thread
    private PrintWriter pw;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Sends CSV to stdout.
     */
    public ToCsv() throws Exception {

        this(null);
    }

    public ToCsv(File file) throws Exception {

        if (file == null) {

            // we'll send output to stdout
            pw = new PrintWriter(System.out);
        }
        else {

            pw = new PrintWriter(new FileWriter(file));
        }
    }

    // Handler implementation ------------------------------------------------------------------------------------------

    @Override
    public boolean canHandle(Object o) {
        return true;
    }

    @Override
    public void handle(long timestamp, String originatorThreadName, Object o) {
        String line = null;
        pw.println(TIMESTAMP_FORMAT.format(timestamp) + ", " + originatorThreadName + ", " + o);
        pw.flush();
    }

    @Override
    public void close() {

        if (pw != null) {

            pw.flush();
            pw.close();
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
