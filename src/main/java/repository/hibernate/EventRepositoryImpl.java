package repository.hibernate;

import model.Event;
import org.hibernate.Session;
import repository.EventRepository;
import service.Utils;

import java.util.List;

public class EventRepositoryImpl implements EventRepository {
    @Override
    public List<Event> getAll() {
        Session session = Utils.getNewSession();
        List<Event> events = session.createQuery("select a from Event a", Event.class).list();
        session.close();
        return events;
    }

    @Override
    public Event getById(Integer id) {
        Session session = Utils.getNewSession();
        Event event = session.get(Event.class, id);
        session.close();
        return event;
    }

    @Override
    public Event create(Event event) {
        Session session = Utils.getNewSession();
        session.beginTransaction();
        event.setId((int) session.save(event));
        session.getTransaction().commit();
        session.close();
        return event;
    }

    @Override
    public Event update(Event event) {
        Session session = Utils.getNewSession();
        session.beginTransaction();
        session.update(event);
        session.getTransaction().commit();
        session.close();
        return event;
    }

    @Override
    public void delete(Integer id) {
        Session session = Utils.getNewSession();
        session.beginTransaction();
        session.delete(getById(id));
        session.getTransaction().commit();
        session.close();
    }
}
