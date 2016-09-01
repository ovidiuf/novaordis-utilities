/*
 * Copyright (c) 2016 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.utilities.jboss.cli;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.operation.impl.DefaultCallbackHandler;
import org.jboss.as.cli.parsing.ParserUtil;
import org.jboss.as.cli.parsing.operation.OperationFormat;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public class JBossControllerClientImpl implements JBossControllerClient {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(JBossControllerClientImpl.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String host;
    private int port;
    private String username;
    private char[] password;
    private boolean disableLocalAuthentication;
    private boolean initializeConsole;
    private int connectionTimeout;

    private CommandContext commandContext;

    private boolean connected;

    // Constructors ----------------------------------------------------------------------------------------------------

    public JBossControllerClientImpl() {

        this.username = null;
        this.password = null;
        this.disableLocalAuthentication = false;
        this.initializeConsole = false;
        this.connectionTimeout = -1;
        this.connected = false;
    }

    // JBossControllerClient implementation ----------------------------------------------------------------------------

    @Override
    public void connect() throws CliException {

        if (connected) {
            log.warn(this + " already connected");
            return;
        }

        try {

            CommandContextFactory factory = CommandContextFactory.getInstance();
            commandContext = factory.newCommandContext(
                    host, port, username, password, disableLocalAuthentication, initializeConsole, connectionTimeout);
            commandContext.connectController();
            connected = true;
        }
        catch (Exception e) {
            throw new CliException(e);
        }
    }

    @Override
    public void disconnect() {

        commandContext.disconnectController();
        connected = false;
    }

    @Override
    public boolean isConnected() {

        return connected;
    }

    @Override
    public String getAttributeValue(String path, String attributeName) throws CliException {

        if (!connected) {

            throw new CliException(this + " not connected");
        }

        String command = path + ":read-attribute(name=" + attributeName + ")";

        boolean validate = true;
        //noinspection ConstantConditions
        DefaultCallbackHandler parsedCommand = new DefaultCallbackHandler(validate);

        try {
            ParserUtil.parse(command, parsedCommand);
        }
        catch(Exception e) {
            throw new CliException(e);
        }

        if (parsedCommand.getFormat() != OperationFormat.INSTANCE ) {

            //
            // we got this from the CLI code, what is "INSTANCE"?
            //

            throw new RuntimeException("NOT YET IMPLEMENTED");
        }

        ModelNode request;

        try {

            request = parsedCommand.toOperationRequest(commandContext);
        }
        catch (Exception e) {
            throw new CliException(e);
        }

        ModelControllerClient client = commandContext.getModelControllerClient();

        ModelNode result;

        try {

            result = client.execute(request);
        }
        catch (Exception e) {
            throw new CliException(e);
        }

        if(Util.isSuccess(result)) {

            System.out.println("success: " + result);

        } else {

            String failureDescription = Util.getFailureDescription(result);
            System.out.println("failure: " + failureDescription);
        }

        return "?";
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
