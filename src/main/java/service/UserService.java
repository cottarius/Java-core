package service;

import dao.UserDao;
import model.User;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(String username) {
        userDao.create(new User(username));
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }

    public User getUser(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }
}
