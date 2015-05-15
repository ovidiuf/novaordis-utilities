package com.novaordis.utilities.testing;

import org.apache.log4j.Logger;

import javax.management.MBeanServer;
import java.util.List;

/**
 * Integration testing based on embedded Tomcat. A test base.
 *
 * TODO: Improved implementation in PRDO - transplant it here.
 *
 * TODO: This class has added functionality that stops the MBeanServer it starts, make sure
 *       to merge that. There's also an associated test.
 *
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2010 Ovidiu Feodorov
 */
public class EmbeddedTomcatTestBase extends FileTestBase
{
    // Constants -----------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(EmbeddedTomcatTestBase.class);

    public static final int TEST_HTTP_PORT = 28943;
    public static final String APP_BASE = "webapps";
    public static final String DOC_BASE = "ROOT";
    public static final String DEFAULT_HOST = "localhost";

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

//    protected Embedded embeddedTomcat;
//
//    // it's connection will be released by tearDown()
//    protected HttpMethod method;

    private List<MBeanServer> before;

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    @Override
    protected void setUp() throws Exception
    {
//        super.setUp();
//        before = MBeanServerFactory.findMBeanServer(null);
//        embeddedTomcat = createAndStartEmbeddedTomcat();
    }

    @Override
    protected void tearDown() throws Exception
    {
//        try
//        {
//            if (method != null)
//            {
//                method.releaseConnection();
//            }
//
//            if (embeddedTomcat != null)
//            {
//                try
//                {
//                    embeddedTomcat.stop();
//                }
//                catch(LifecycleException e)
//                {
//                    // some tests stop the initial container, and the Tomcat API doesn't allow me to
//                    // check whether the container is started or not, short of subclassing, which I do
//                    // not want to do. Hence, I am looking for LifecycleException and I am simply
//                    // reporting it if it happens
//                    log.info("embedded tomcat seem to have been stopped already: " + e.getMessage());
//                }
//
//                embeddedTomcat = null;
//            }
//        }
//        finally
//        {
//            List<MBeanServer> after = MBeanServerFactory.findMBeanServer(null);
//
//            for(MBeanServer s: after)
//            {
//                if (before == null || !before.contains(s))
//                {
//                    log.debug("releasing " + s);
//                    MBeanServerFactory.releaseMBeanServer(s);
//                }
//            }
//        }
//
//        super.tearDown();
    }

//    protected Embedded createAndStartEmbeddedTomcat() throws Exception
//    {
//        Embedded result = null;
//
//        // embedded tomcat needs valid filesystem paths to look at
//        File tomcatHome = new File(getScratchDir(), getRandomDirectoryName("embedded-tomcat-home"));
//        File appBase = new File(tomcatHome, APP_BASE);
//        File docBase = new File(appBase, DOC_BASE);
//
//        assertFalse(tomcatHome.exists());
//        assertTrue(docBase.mkdirs());
//
//        assertTrue(Files.write(new File(docBase, "index.html"), "hello"));
//
//        System.setProperty("catalina.home", tomcatHome.getAbsolutePath());
//        result = new Embedded();
//
//        // because we send "http://localhost:..." requests, we need to create a 'localhost' host
//        Engine tomcatEngine = result.createEngine();
//        tomcatEngine.setDefaultHost(DEFAULT_HOST);
//
//        Host tomcatHost = result.createHost(DEFAULT_HOST, appBase.getAbsolutePath());
//        tomcatEngine.addChild(tomcatHost);
//
//        // the default context
//        Context context = result.createContext("", docBase.getAbsolutePath());
//        tomcatHost.addChild(context);
//
//        deployTestServlet(result, tomcatHost, appBase);
//
//        // for the default web.xml it will look up for 'web-embed.xml' in the test's classpath
//
//        result.addEngine(tomcatEngine);
//
//        Connector connector = result.createConnector((InetAddress)null, TEST_HTTP_PORT, false);
//        result.addConnector(connector);
//
//        result.start();
//
//        log.debug(result + " created and started");
//        return result;
//    }

    // Private -------------------------------------------------------------------------------------


//    private void deployTestServlet(Embedded embedded, Host host, File appBase) throws Exception
//    {
//        if (!appBase.isDirectory())
//        {
//            throw new IllegalArgumentException(appBase + " is not a valid directory");
//        }
//
//        File docBase = new File(appBase, "testContext");
//        File webInfDir = new File(docBase, "WEB-INF");
//        assertTrue(webInfDir.mkdirs());
//
//        String webXml =
//            "<web-app>\n" +
//            "    <servlet>\n" +
//            "        <servlet-name>TestServlet</servlet-name>\n" +
//            "        <servlet-class>org.novaordis.universus.collector.http.TestServlet</servlet-class>\n" +
//            "    </servlet>\n" +
//            "    <servlet-mapping>\n" +
//            "        <servlet-name>TestServlet</servlet-name>\n" +
//            "        <url-pattern>/*</url-pattern>\n" +
//            "    </servlet-mapping>\n" +
//            "</web-app>\n";
//
//        File webXmlFile = new File(webInfDir, "web.xml");
//        assertTrue(Files.write(webXmlFile, webXml));
//
//        // the context's path, unless is "", *must* start with a slash
//        Context context = embedded.createContext("/testContext", docBase.getAbsolutePath());
//        host.addChild(context);
//    }

    // Inner classes -------------------------------------------------------------------------------
}