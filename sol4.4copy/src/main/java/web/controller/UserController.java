package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import javax.swing.text.AttributeSet;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserController {


    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(@Lazy UserService userService, @Lazy RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @PostMapping(value = "/admin/allUsers")
    @ResponseBody
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.add(user);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping(value = "/admin/showList", produces = "application/json")
    public User updateUser(@RequestBody User user) {
        userService.updateById(user, user.getId());
        System.out.println("завершено");
        return user;
    }


    @ResponseBody
    @DeleteMapping(value = "/admin/showList", produces = "application/json")
    public ResponseEntity<User> deleteUser(@RequestBody User user) {
        userService.deleteById(user.getId());
        return new ResponseEntity(user, HttpStatus.CREATED);
    }


    @ResponseBody
    @GetMapping(value = "/admin/allUsers", produces = "application/json")
    public List<User> allUsers() {
       List <User> users = userService.getAllUsers();
        List <User> newUsers = new ArrayList<>();
       for(User user : users) {
           User newUser = userService.getUser(user);
            newUsers.add(newUser);
       }
        return newUsers;
    }


    @ResponseBody
    @GetMapping(value ="/user/getOne/{id}", produces = "application/json")
    User one(@PathVariable Long id) {
        User user= userService.getUser(userService.findById(id));
        return user;
    }

//___________________________________________________________________


    @GetMapping(value = "/admin/showList")
    public String showList(ModelMap model, Authentication authentication) {
        User authenUser = userService.findByUsername(authentication.getName());
        model.addAttribute("user", new User());

        model.addAttribute("authUser", authenUser);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "result";
    }




    @GetMapping(value = "/user")
    public String user(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "user";
    }

}
