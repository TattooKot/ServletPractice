package service;

import model.User;
import repository.hibernate.UserRepositoryImpl;

import java.util.List;
import java.util.Objects;

public class UserService {
    private final UserRepositoryImpl repository = new UserRepositoryImpl();

    public List<User> getAll(){
        return repository.getAll();
    }

    public User getById(Integer id){
        return repository.getById(id);
    }

    public User create(User user){
        return repository.create(user);
    }

    public User update(User user){
        return repository.update(user);
    }

    public void delete(Integer id){
        repository.delete(id);
    }

    public boolean checkById(Integer id){
        return Objects.nonNull(getById(id));
    }

    public boolean checkByName(String name){
        User user = getAll().stream()
                .filter(u -> name.equals(u.getUsername()))
                .findAny()
                .orElse(null);
        return Objects.nonNull(user);
    }
}
