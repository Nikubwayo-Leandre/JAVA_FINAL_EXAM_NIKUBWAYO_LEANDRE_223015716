package service;

import model.User;
import dao.UserDAO;
import java.util.List;

public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        userDAO = new UserDAO();
    }
    
    public User authenticate(String username, String password) {
        return userDAO.authenticate(username, password);
    }
    
    public User createUser(String username, String password, String userType, String email, int referenceId) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setUserType(userType);
        user.setEmail(email);
        user.setReferenceId(referenceId);
        
        int userId = userDAO.create(user);
        if (userId != -1) {
            user.setUserId(userId);
            return user;
        }
        return null;
    }
    
    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }
    
    public User getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }
    
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
    
    public List<User> getUsersByType(String userType) {
        return userDAO.findByUserType(userType);
    }
    
    public boolean updateUser(User user) {
        return userDAO.update(user);
    }
    
    public boolean deleteUser(int userId) {
        return userDAO.delete(userId);
    }
    
    public boolean usernameExists(String username) {
        return userDAO.usernameExists(username);
    }
    
    public boolean emailExists(String email) {
        return userDAO.emailExists(email);
    }
    
    public List<User> searchUsers(String keyword) {
        return userDAO.searchUsers(keyword);
    }
    
    public int countUsersByType(String userType) {
        return userDAO.countByUserType(userType);
    }
    
    public int getTotalUsersCount() {
        return userDAO.getTotalUsers();
    }
}