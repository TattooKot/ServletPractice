package repository.hibernate;

import model.User;
import org.hibernate.Session;
import repository.UserRepository;
import service.Utils;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public List<User> getAll() {
        Session session = Utils.getNewSession();
        List<User> users = session.createQuery("select a from User a", User.class).list();
        session.close();
        return users;
    }

    @Override
    public User getById(Integer id) {
        Session session = Utils.getNewSession();
        User user = session.get(User.class, id);
        session.close();
        return user;
    }

    @Override
    public User create(User user) {
        Session session = Utils.getNewSession();
        session.beginTransaction();
        user.setId((int) session.save(user));
        session.getTransaction().commit();
        session.close();
        return user;
    }

    @Override
    public User update(User user) {
        Session session = Utils.getNewSession();
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
        session.close();
        return user;
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
