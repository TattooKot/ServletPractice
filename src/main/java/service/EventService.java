package service;

import model.Event;
import repository.hibernate.EventRepositoryImpl;

import java.util.List;

public class EventService {
    private final EventRepositoryImpl repository = new EventRepositoryImpl();

    public List<Event> getAll(){
        return repository.getAll();
    }

    public Event getById(Integer id){
        return repository.getById(id);
    }

    public Event create(Event event){
        return repository.create(event);
    }

    public Event update(Event event){
        return repository.update(event);
    }

    public void delete(Integer id){
        repository.delete(id);
    }
}
