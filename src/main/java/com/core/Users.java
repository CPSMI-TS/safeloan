package com.core;


import com.google.gson.Gson;
import com.core.dbService.entities.Group;
import com.core.dbService.entities.User;
import com.core.dbService.services.GroupService;
import com.core.dbService.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

/**
 * Created by t.konst on 31.01.2017.
 */
@Path("/users")
public class Users {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers() {
        UserService userService = new UserService();
        User user;
        String response = "{\"users\": [";
        for (int i = 1; i <= 3; i++) {
            user = userService.getUserById(i);
            if (user != null)
                response = (i == 3 ? response.concat(user.toString()) : response.concat(user.toString() + ", "));
        }
        response = response.concat("]}");
        userService.stop();
        return response;
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser(@PathParam("id") int id) {
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        userService.stop();
        return "{\"user\": " + new Gson().toJson(user) + "}";
    }

    @GET
    @Path("{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser(@PathParam("login") String login) {
        UserService userService = new UserService();
        User user = userService.getUserByLogin(login);
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        userService.stop();
        return "{\"user\": " + new Gson().toJson(user) + "}";
    }

    @POST
    @Path("/signup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(@QueryParam("login") String login, @QueryParam("pass") String pass) {
        UserService userService = new UserService();
        int id = userService.addUser(login, pass, 0.0);
        userService.stop();
        return (id == -1 ? Response.notModified().build() : Response.created(URI.create("../" + id)).build());
    }

    @GET
    @Path("{id: \\d+}/groups")
    @Produces(MediaType.APPLICATION_JSON)
    public String getGroupUsers(@PathParam("id") int id) {
        GroupService groupService = new GroupService();
        UserService userService = new UserService();
        List<Group> groups = userService.getUserGroups(id);
        if (groups == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        String response = "{\"groups\": [";
        int i = 0;
        for (Group group : groups) {
            i++;
            response = (i == groups.size() ? response.concat(group.toString()) : response.concat(group.toString() + ", "));
        }
        response = response.concat("]}");
        groupService.stop();
        userService.stop();
        return response;
    }

    @DELETE
    @Path("{id: \\d+}/quit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response quit(@PathParam("id") int id) {
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        boolean deleted = userService.deleteUser(user);
        userService.stop();
        if (!deleted)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else return Response.ok().build();
    }
}
