package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin/**")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String getPanel(ModelMap modelMap, Authentication authentication) {
        User user=(User)authentication.getPrincipal();
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("users", userService.getAllUsers());
        return "admin/panel";
    }

    @PostMapping("update")
    public String updateUser(WebRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String[] role = request.getParameterValues("role");
        Set<Role> roles = new HashSet<>();
        for(int i=0;i<role.length;i++){
           if(role[i].equals("ADMIN")){
               roles.add(Role.ADMIN);
            }else{roles.add(Role.USER);}
        }
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            User newUser = User.builder().
                    id(id).
                    username(username).
                    password(password).
                    authorities(roles).
                    accountNonLocked(true).
                    enabled(true).
                    credentialsNonExpired(true).
                    accountNonExpired(true).
                    build();
            userService.updateUser(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

    @PostMapping("add")
    public String addUser(WebRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String[] role = request.getParameterValues("role");
        Set<Role> roles = new HashSet<>();
        for(int i=0;i<role.length;i++){
            if(role[i].equals("ADMIN")){
                roles.add(Role.ADMIN);
            }else{roles.add(Role.USER);}
        }
        try {
             User newUser = User.builder().
                    username(username).
                    password(password).
                    authorities(roles).
                    accountNonExpired(true).
                    credentialsNonExpired(true).
                    accountNonLocked(true).
                    enabled(true).
                    build();
            userService.addUser(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

    @PostMapping("del")
    public String deleteUser(WebRequest request) {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            userService.deleteUser(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin";

    }
}
