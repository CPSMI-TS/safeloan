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
 * Created by t.konst on 04.02.2017.
 */
@Path("/groups")
public class Groups {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers() {
        GroupService groupService = new GroupService();
        Group group;
        String response = "{\"groups\": [";
        for (int i = 1; i <= 3; i++) {
            group = groupService.getGroupById(i);
            if (group != null) {
                response = (i == 3 ? response.concat(group.toString()) : response.concat(group.toString() + ", "));
            }
        }
        if (response.endsWith(", "))
            response = response.substring(0, response.length() - 2);
        response = response.concat("]}");
        groupService.stop();
        return response;
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getGroupById(@PathParam("id") int id) {
        GroupService groupService = new GroupService();
        Group group = groupService.getGroupById(id);
        if (group == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        groupService.stop();
        return "{\"group\": " + new Gson().toJson(group) + "}";
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getGroupByName(@PathParam("name") String name) {
        GroupService groupService = new GroupService();
        Group group = groupService.getGroupByName(name);
        if (group == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        groupService.stop();
        return "{\"group\": " + new Gson().toJson(group) + "}";
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGroup(@QueryParam("name") String name, @QueryParam("type") String type) {
        GroupService groupService = new GroupService();
        int id = groupService.addGroup(name, type);
        groupService.stop();
        return (id == -1 ? Response.notModified().build() : Response.created(URI.create("/users/" + id)).build());
    }

    @GET
    @Path("{id: \\d+}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public String getGroupUsers(@PathParam("id") int id) {
        GroupService groupService = new GroupService();
        UserService userService = new UserService();
        if (!groupService.checkGroupExists(id))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        List<User> users = groupService.getGroupUsers(id);
        if (users == null || users.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        String response = "{\"users\": [";
        int i = 0;
        for (User user : users) {
            i++;
            response = (i == users.size() ? response.concat(user.toString()) : response.concat(user.toString() + ", "));
        }
        response = response.concat("]}");
        groupService.stop();
        userService.stop();
        return response;
    }

    @POST
    @Path("{name}/join")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGroupUser(@PathParam("name") String name, @QueryParam("user") String login) {
        GroupService groupService = new GroupService();
        int id = groupService.addGroupUser(login, name);
        groupService.stop();
        return (id == -1 ? Response.notModified().build() : Response.created(URI.create("/users/" + id)).build());
    }

    @DELETE
    @Path("{id: \\d+}/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        GroupService groupService = new GroupService();
        boolean deleted = groupService.deleteGroup(groupService.getGroupById(id));
        groupService.stop();
        if (!deleted)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return Response.ok().build();
    }

    /*
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public String getGroupUsers(@QueryParam("group") int id) {
        GroupService groupService = new GroupService();
        UserService userService = new UserService();
        List<User> users = groupService.getGroupUsers(id);
        if (users == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        User user;
        String response = "{\"users\": [";
        for (int i = 1; i <= users.size(); i++) {
            user = userService.getUserById(i);
            response = (i == users.size() ? response.concat(user.toString()) : response.concat(user.toString() + ", "));
        }
        response = response.concat("]}");
        return response;
    }

    //todo: убрать параметры, вставить json и добавить парс json'а в объект группы
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGroup(String jsonGroup) {
        Gson gson = new Gson();
        Group group = gson.fromJson(jsonGroup, Group.class);
        GroupService groupService = new GroupService();
        int id = groupService.addGroup(group.getGroupName(), group.getGroupType());
        Response response = (id==-1 ? Response.notModified().build() : Response.created(URI.create("/users/" + id)).build());
        return response;
    }*/
}
