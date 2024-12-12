package web.service;
import web.model.Role;
import web.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService  {

    Set<Role> checkRoles(User user);

    void add(User user);

    List<User> getAllUsers();

    void deleteById(Long id);

    User findById(Long id);

    void updateById(User user, Long id);

    User getUser(User user);

    User findByUsername(String username);
}
