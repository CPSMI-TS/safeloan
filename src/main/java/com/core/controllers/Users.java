package com.core.controllers;

import com.core.dbService.entities.Loan;
import com.core.dbService.entities.Group;
import com.core.dbService.entities.User;
import com.core.dbService.services.GroupService;
import com.core.dbService.services.UserService;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by t.konst on 31.01.2017.
 */
@Path("/users")
public class Users {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers(@QueryParam("start") int start, @QueryParam("size") int size) {
        UserService userService = new UserService();
        User user;
        String response = "{\"users\": [";
        int i = 0;
        int id = start;
        while (i != size) {
            user = userService.getUserById(id);
            if (user != null) {
                i++;
                response = (i != size) ? response.concat(user.toString()) : response.concat(user.toString() + ", ");
            }
            id++;
        }
        response = response.concat("]}");
        userService.stop();
        return response;
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") int id) {
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        userService.stop();
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(user.toString(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("login") String login) {
        UserService userService = new UserService();
        User user = userService.getUserByLogin(login);
        userService.stop();
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(user.toString(), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/signup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(String jsonUser) {
        Gson gson = new Gson();
        User newUser = gson.fromJson(jsonUser, User.class);
        int id;
        User user = null;
        UserService userService = new UserService();
        try {
            id = userService.addUser(newUser.getUserLogin(), newUser.getPassword(), newUser.getDebt());
            if (id != -1)
                user = userService.getUserById(id);
        } catch (Exception e) {
            throw new WebApplicationException(e);
        } finally {
            userService.stop();
        }
        return (id == -1 ? Response.notModified().build() : Response.created(URI.create("/" + id))
                .entity(user.toString()).type(MediaType.APPLICATION_JSON).build());
    }

    @GET
    @Path("{id: \\d+}/groups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGroupUsers(@PathParam("id") int id) {
        checkUserExists(id);
        UserService userService = new UserService();
        List<Group> groups = userService.getUserGroups(id);
        userService.stop();
        if (groups == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(groups.toString(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("{id: \\d+}/loans")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserLoans(@PathParam("id") int id) {
        checkUserExists(id);
        UserService userService = new UserService();
        List<Loan> loans = userService.getUserLoans(id);
        userService.stop();
        if (loans == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(loans, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("{id: \\d+}/payments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPayments(@PathParam("id") int id) {
        checkUserExists(id);
        UserService userService = new UserService();
        List<Loan> loans = userService.getUserPayments(id);
        userService.stop();
        if (loans == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.ok(loans, MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("{id: \\d+}/quit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response quit(@PathParam("id") int id) {
        checkUserExists(id);
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        boolean deleted = userService.deleteUser(user);
        userService.stop();
        if (!deleted)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else return Response.ok().build();
    }

    static boolean isGroupMember(Integer userId, Integer groupId) {

        GroupService groupService = new GroupService();
        ArrayList<User> users = groupService.getGroupUsers(groupId);
        groupService.stop();
        List<Integer> ids = new ArrayList<>(users.size());
        for (User user : users) {
            ids.add(user.getUserId());
        }
        return ids.contains(userId);
    }

    static void checkUserExists(int id) throws WebApplicationException {
        UserService userService = new UserService();
        User user = userService.getUserById(id);
        userService.stop();
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

/*
    @POST
    @Path("/signup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(@QueryParam("login") String login, @QueryParam("pass") String pass) {
        UserService userService = new UserService();
        int id = userService.addUser(login, pass, 0.0);
        userService.stop();
        return (id == -1 ? Response.notModified().build() : Response.created(URI.create("../" + id)).build());
    }
*/
}
