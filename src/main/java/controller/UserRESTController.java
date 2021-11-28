package controller;

import com.google.gson.Gson;
import model.User;
import service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

@WebServlet(value = "/users")
public class UserRESTController extends HttpServlet {
    private final UserService service = new UserService();
    private final Gson gson = new Gson();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String idParam = request.getHeader("id");

        if (Objects.isNull(idParam)) {
            getAll(response);
        } else if (idParam.matches("\\d+")) {
            getById(request, response);
        } else {
            response.setStatus(400);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getHeader("name");

        if(Objects.nonNull(name)){
            create(name, response);
        } else {
            response.setStatus(400);
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getHeader("id");
        String name = request.getHeader("name");

        if(Objects.nonNull(name) && Objects.nonNull(id) && id.matches("\\d+")){
            update(request, response);
        } else {
            response.setStatus(400);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response){
        String idParam = request.getHeader("id");

        if(Objects.nonNull(idParam) && idParam.matches("\\d+")){
            delete(Integer.parseInt(idParam), response);
        } else {
            response.setStatus(400);
        }
    }

    private void getAll(HttpServletResponse response) throws IOException{
        List<User> users = service.getAll();
        createResponse(users, response);
    }

    private void getById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getHeader("id"));
        User user = service.getById(id);
        if(Objects.isNull(user)){
            response.setStatus(400);
            return;
        }
        createResponse(user, response);
    }

    private void create(String name, HttpServletResponse response) throws IOException {
        if(!service.checkByName(name)){
            User created = service.create(new User(name));
            createResponse(created, response);
        } else {
            response.setStatus(500);
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getHeader("id"));
        String name = request.getHeader("name");
        User user = service.getById(id);
        if(Objects.isNull(user)){
            response.setStatus(400);
            return;
        }

        user.setUsername(name);
        service.update(user);
        createResponse(user, response);
    }

    private void delete(int id, HttpServletResponse response){
        if(service.checkById(id)){
            service.delete(id);
        } else {
            response.setStatus(400);
        }
    }

    private void createResponse(Object o, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(o));
    }
}
