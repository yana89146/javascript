package web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import web.model.Role;
import web.model.User;
import web.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImp implements UserDetailsService, UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;


    @Autowired
    public UserServiceImp(@Lazy PasswordEncoder passwordEncoder, @Lazy UserService userService, RoleService roleService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }


    @Override
    public Set<Role> checkRoles(User user) {
        Set<Role> newRoles = new HashSet<>();

        if(!user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                Role dbRole = Optional.of(roleService.findByName(role.getName())).orElse(null);
                System.out.println(dbRole.getName());
                if (dbRole == null) {
                    System.out.println("роль не найдена");
                    dbRole = roleService.save(role.getName());
                    newRoles.add(dbRole);
                } else {
                    newRoles.add(dbRole);
                }
            }

        }else{
            newRoles.addAll(user.getRoles());
        }
        return newRoles;
    }

    public void add(User user) {
        User newUser = new User();
        userRepository.save(newUser);
       userService.updateById(user, newUser.getId());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional <User> user = userRepository.findById(id);
        user.get().getRoles().clear();
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void updateById(User user, Long id) {

        Set<Role> newRoles = userService.checkRoles(user);

        User updatedUser = userRepository.findById(id).get();
        updatedUser.getRoles().clear();
        updatedUser.setRoles(newRoles);
        updatedUser.setUsername(user.getUsername());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updatedUser.setEmail(user.getEmail());
        updatedUser.setAge(user.getAge());
        userRepository.save(updatedUser);

    }

    @Override
    public User getUser(User user) {
        User newUser =new User();
        newUser.setId(Long.parseLong(user.getId().toString()));
        newUser.setUsername(user.getUsername().toString());
        newUser.setLastName(user.getLastName().toString());
        newUser.setPassword(user.getPassword().toString());
        newUser.setEmail(user.getEmail().toString());
        newUser.setAge(Integer.parseInt(user.getAge().toString()));

        for(Role role : user.getRoles()){
            newUser.setRole(new Role(role.getName()));
        }

        return newUser;
    }

    @Override
    public  User findByUsername(String username){
        return userRepository.findByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<User> optionalUser = Optional.ofNullable(userService.findByUsername(username));
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        User user = optionalUser.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities(user.getRoles()));

    }

    private Set<GrantedAuthority> authorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
    }
}
