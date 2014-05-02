package com.waiapp.rest.auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/noauth")
public class UnauthenticatedResource {

    @GET
    public String get() {
        return "passed";
    }

    @Path("auth")
    @GET
    @BasicAuthentication
    public String getAuth() {
        return "passed";
    }
}
