/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author <a href="http://community.jboss.org/people/LightGuard">Jason Porter</a>
 */
package org.jboss.arquillian.container.glassfish.remote;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jakarta.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifies arquillian tests can run in container mode with this REST based container.
 *
 * @author <a href="http://community.jboss.org/people/aslak">Aslak Knutsen</a>
 * @author <a href="http://community.jboss.org/people/LightGuard">Jason Porter</a>
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@ExtendWith(ArquillianExtension.class)
public class GlassFishRestDeployWarTest {
    private static final Logger log = Logger.getLogger(GlassFishRestDeployWarTest.class.getName());

    @Deployment
    public static WebArchive getTestArchive() {
        final WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(GreeterServlet.class, Greeter.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        log.info(war.toString(true));
        return war;
    }

    @Test
    public void assertWarDeployed() throws Exception {
        final String servletPath = GreeterServlet.class.getAnnotation(WebServlet.class).urlPatterns()[0];

        final URLConnection response = new URL("http://localhost:8080/test" + servletPath).openConnection();

        BufferedReader in = new BufferedReader(new InputStreamReader(response.getInputStream()));
        final String result = in.readLine();

        assertEquals("Hello", result);
    }
}
