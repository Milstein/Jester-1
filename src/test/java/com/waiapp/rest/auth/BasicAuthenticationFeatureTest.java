package com.waiapp.rest.auth;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class BasicAuthenticationFeatureTest extends JerseyTest {

    private static final String API_KEY = generateRandomKey();
    private static final String API_SECRET = generateRandomKey();

    @Override
    protected Application configure() {
        return
            new ResourceConfig(UnauthenticatedResource.class, AuthenticatedResource.class)
                .register(new BasicAuthenticationFeature(API_KEY, API_SECRET));
    }

    @Test
    public void testUnauthenticatedClass() {
        testPassesWithoutAuthentication("noauth");

        // server should just ignore the credentials
        testWithAuthentication("noauth");
    }

    @Test
    public void testAuthenticatedClassWithNoAuthFails() {
        testFailsWithoutAuthentication("auth");
    }

    @Test
    public void testAuthenticatedClassWithWrongAuthFails() {
        testWithWrongAuthentication("auth");
    }

    @Test
    public void testAuthenticatedClassWithAuthSucceeds() {
        testWithAuthentication("auth");
    }

    @Test
    public void testAuthenticatedClassWithUnauthenticatedMethod() {
        testPassesWithoutAuthentication("auth/noauth");
    }

    @Test
    public void testUnauthenticatedClassWithAuthenticatedMethod() {
        testWithAuthentication("noauth/auth");
        testFailsWithoutAuthentication("noauth/auth");
    }

    public void testPassesWithoutAuthentication(String path) {
        final Response rsp = target(path).request().get(Response.class);

        assertEquals(Response.Status.OK, rsp.getStatusInfo());
        assertEquals("passed", rsp.readEntity(String.class));
    }

    public void testFailsWithoutAuthentication(String path) {
        final Response rsp = target(path).request().get(Response.class);
        assertEquals(Response.Status.UNAUTHORIZED, rsp.getStatusInfo());
    }

    public void testWithAuthentication(String path) {
        final Response rsp =
                targetUsingBasicAuth(path, API_KEY, API_SECRET)
                        .request()
                        .get(Response.class);

        assertEquals(Response.Status.OK, rsp.getStatusInfo());
        assertEquals("passed", rsp.readEntity(String.class));
    }

    public void testWithWrongAuthentication(String path) {
        final Response rsp =
                targetUsingBasicAuth(path, "Foo", API_SECRET)
                        .request()
                        .get(Response.class);

        assertEquals(Response.Status.UNAUTHORIZED, rsp.getStatusInfo());
    }

    private static String generateRandomKey() {
        return UUID.randomUUID().toString();
    }

    public WebTarget targetUsingBasicAuth(String resource, String key, String secret) {
        return target(resource).register(HttpAuthenticationFeature.basic(key, secret));
    }
}
