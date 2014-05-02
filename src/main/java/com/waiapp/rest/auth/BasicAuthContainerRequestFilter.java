package com.waiapp.rest.auth;

import org.glassfish.jersey.internal.util.Base64;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * An immutable container request filter that requires basic authentication using static credentials. This
 * class is for handling the simplest cases of basic authentication for a REST resource. The case where
 * Basic Auth is used and there is a static API key and secret for that resource. The key and secret
 * (represented as userName and password here) can be any combination of ascii characters.
 * <p>
 * The client should concatenate the user name and password (separated by a ":") and then base64 encoded.
 * <p>
 * For more information <a href=http://en.wikipedia.org/wiki/Basic_access_authentication#Client_side>Basic Auth</a>
 * <p>
 * @author Robert DiFalco (robert.difalco@waiapp.com)
 * Copyright (c) 2014
 */
public class BasicAuthContainerRequestFilter implements ContainerRequestFilter {

    /** Defines the length of the "Basic " string preceding the actual Base64 encoded credential */
    private static final int BASIC_AUTH_PREFIX_LENGTH = 6;

    /** Base64 encoded version of the credentials ("user:password") */
    private final String credentials;

    /**
     * This sets up a basic auth filter that uses a static key and secret for performing authentication.
     * It is appropriate for when you have a global key and secret for a set of resources.
     */
    public BasicAuthContainerRequestFilter(String userName, String password) {
        this.credentials = encodeBase64(userName, password);
    }

    private String encodeBase64(String userName, String password) {
        // here we concatenate the username and password and base64 encode as a client would send it
        return Base64.encodeAsString(userName + ':' + password);
    }

    @Override
    public void filter(ContainerRequestContext requestCtx) throws WebApplicationException {
        String authValue = getHeaderValue(requestCtx.getHeaders(), "authorization");
        if (!isValid(authValue))
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    private String getHeaderValue(MultivaluedMap<String, String> headers, String header) {
        List<String> values = headers.get(header);
        if (values == null || values.isEmpty())
            return null;

        return values.get(0);
    }

    private boolean isValid(String authValue) {
        return authValue != null
            && authValue.length() > BASIC_AUTH_PREFIX_LENGTH
            && credentials.equals(authValue.substring(BASIC_AUTH_PREFIX_LENGTH));
    }
}
