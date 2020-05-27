package com.markvdhorst.infrastructure.driving;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("")
public class HelloController {

    @GET
    @Path("")
    public String hello() {
        return "Hello World";
    }
}
