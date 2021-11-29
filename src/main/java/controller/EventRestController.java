package controller;

import com.google.gson.Gson;
import model.Event;
import model.User;
import service.EventService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@WebServlet(value = "/events")
@MultipartConfig
public class EventRestController extends HttpServlet {
    private final EventService service = new EventService();
    private final UserService userService = new UserService();
    private final Gson gson = new Gson();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getHeader("id");

        if(Objects.isNull(id)){
            getAll(response);
        } else if(id.matches("\\d+")){
            getById(request, response);
        } else {
            response.setStatus(400);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getHeader("userId");
        String filePart = request.getHeader("Content-type");
        if(Objects.nonNull(userId) && userId.matches("\\d+") && Objects.nonNull(filePart)){
            createEvent(request, response);
        } else {
            response.setStatus(400);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getHeader("id");

        if(Objects.nonNull(id) && id.matches("\\d+")){
            delete(request, response);
        } else {
            response.setStatus(400);
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getHeader("id");
        String filePart = request.getHeader("Content-type");
        if(Objects.nonNull(userId) && userId.matches("\\d+") && Objects.nonNull(filePart)){
            update(request, response);
        } else {
            response.setStatus(400);
        }
    }

    private void getAll( HttpServletResponse response) throws IOException {
        List<Event> eventList = service.getAll();
        createResponse(eventList, response);

    }

    private void createEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getHeader("userId"));
        User user = userService.getById(id);
        if(Objects.isNull(user)){
            response.setStatus(400);
            return;
        }

        Part part = request.getPart("file");
        String fileName = id + "_" + getFileName(part);

        List<Event> userEvents = user.getEvents();
        if(userEvents.contains(new Event(fileName))){
            response.setStatus(400);
            return;
        }

        Event newEvent = service.create(new Event(fileName, new Date()));
        saveFile(part, fileName);

        userEvents.add(newEvent);
        user.setEvents(userEvents);
        userService.update(user);

        createResponse(fileName, response);
    }

    public void getById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getHeader("id"));
        Event event = service.getById(id);
        if(Objects.isNull(event)){
            response.setStatus(400);
            return;
        }
        createResponse(event, response);
    }

    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getHeader("id"));
        Event event = service.getById(id);
        if(Objects.isNull(event)){
            response.setStatus(400);
            return;
        }

        Part part = request.getPart("file");
        String fileName = id + "_" + getFileName(part);
        if(!fileName.equals(event.getFilename())){
            response.setStatus(400);
            return;
        }

        saveFile(part, event.getFilename());
        event.setUploaddate(new Date());
        Event updated = service.update(event);

        createResponse(updated, response);
    }

    public void delete(HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getHeader("id"));
        Event event = service.getById(id);
        if(Objects.isNull(event)){
            response.setStatus(400);
            return;
        }

        service.delete(id);
        new File("uploadedFiles/" + event.getFilename()).delete();

        event = service.getById(id);
        if(Objects.nonNull(event)){
            response.setStatus(400);
        }
    }

    private void createResponse(Object o, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println(gson.toJson(o));
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private void saveFile(Part part, String fileName) throws IOException {
        File file = new File("uploadedFiles/" + fileName);

        try(BufferedInputStream inputStream =
                    new BufferedInputStream(part.getInputStream());
            BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(file));
            ByteArrayOutputStream buffer = new ByteArrayOutputStream())
        {
            byte[] data = new byte[1024];
            int current;
            while((current = inputStream.read(data,0, data.length)) != -1){
                buffer.write(data,0,current);
            }
            outputStream.write(buffer.toByteArray());
        }
    }
}