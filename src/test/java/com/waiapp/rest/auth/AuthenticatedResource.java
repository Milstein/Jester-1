package com.waiapp.rest.auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/auth")
@BasicAuthentication
public class AuthenticatedResource {

    @GET
    public String get() {
        return "passed";
    }

    @GET
    @Path("noauth")
    @BasicAuthentication(false)
    public String getNoAuth() {
        return "passed";
    }
}
