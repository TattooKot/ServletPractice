package controller;

import com.google.gson.Gson;
import model.User;
import repository.hibernate.UserRepositoryImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

public class UserRESTController extends HttpServlet {
    private final UserRepositoryImpl repository = new UserRepositoryImpl();
    private final Gson gson = new Gson();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String idParam = request.getParameter("id");

        if (idParam.equals("all")) {
            getAll(response);
        } else if (idParam.matches("\\d+")) {
            getById(request, response);
        } else {
            response.setStatus(400);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = request.getParameter("ip");

        if(Objects.nonNull(ip) && ip.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")){
            create(ip, response);
        } else {
            response.setStatus(400);
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = request.getParameter("ip");
        String id = request.getParameter("id");

        if(Objects.nonNull(ip) && Objects.nonNull(id)
                && id.matches("\\d+") && ip.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")){
            update(request, response);
        } else {
            response.setStatus(400);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response){
        String idParam = request.getParameter("id");

        if(Objects.nonNull(idParam) && idParam.matches("\\d+")){
            delete(Integer.parseInt(idParam), response);
        } else {
            response.setStatus(400);
        }
    }

    private void getAll(HttpServletResponse response) throws IOException{
        List<User> users = repository.getAll();
        createResponse(users, response);
    }

    private void getById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = repository.getById(id);
        if(Objects.isNull(user)){
            response.setStatus(400);
            return;
        }
        createResponse(user, response);
    }

    private void create(String ip, HttpServletResponse response) throws IOException {
        List<User> users = repository.getAll();
        User user = users.stream()
                .filter(u -> ip.equals(u.getIpaddress()))
                .findAny()
                .orElse(null);

        if(Objects.isNull(user)){
            User created = repository.create(new User(ip));
            createResponse(created, response);
        } else {
            response.setStatus(500);
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String ip = request.getParameter("ip");
        User user = repository.getById(id);
        if(Objects.isNull(user)){
            response.setStatus(400);
            return;
        }

        user.setIpaddress(ip);
        repository.update(user);
        createResponse(user, response);
    }

    private void delete(int id, HttpServletResponse response){
        User user = repository.getById(id);
        if(Objects.nonNull(user)){
            repository.delete(id);
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
