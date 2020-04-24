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
        modelMap.addAttribute("username", user.getUsername());
        modelMap.addAttribute("role", user.getAuthorities());
        modelMap.addAttribute("users", userService.getAllUsers());
        return "admin/panel";
    }

    @GetMapping("add")
    public String addUser() {
        return "admin/create";
    }

    @GetMapping("updt")
    public String getUpdate(WebRequest request, ModelMap model) {
        try {
            User user = userService.getUserById(Long.parseLong(request.getParameter("id")));
            if (user != null) {
                model.addAttribute("user", user);
            }
        } catch (Exception e) {
            model.addAttribute("message", "User Not Found:(");
        }
        return "admin/update";
    }

    @PostMapping("updt")
    public String updateUser(WebRequest request, ModelMap modelMap,Authentication authentication) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String[] role = request.getParameterValues("role");
        Set<Role> roles = new HashSet<>();
        User user=(User)authentication.getPrincipal();
        modelMap.addAttribute("username", user.getUsername());
        modelMap.addAttribute("role", user.getAuthorities());
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
            modelMap.addAttribute("message", "User was Update!");
            modelMap.addAttribute("users",userService.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.addAttribute("message", "User not Update:(");
            modelMap.addAttribute("users",userService.getAllUsers());
        }
        return "admin/panel";
    }

    @PostMapping("add")
    public String addUser(WebRequest request, ModelMap modelMap, Authentication authentication) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String[] role = request.getParameterValues("role");
        Set<Role> roles = new HashSet<>();
        User user=(User)authentication.getPrincipal();
        modelMap.addAttribute("username", user.getUsername());
        modelMap.addAttribute("role", user.getAuthorities());
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
            modelMap.addAttribute("message", "User was Create!");
            modelMap.addAttribute("users",userService.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.addAttribute("message", "User not Create:(");
            modelMap.addAttribute("users",userService.getAllUsers());
        }
        return "admin/panel";
    }

    @PostMapping("del")
    public String deleteUser(WebRequest request, ModelMap modelMap,Authentication authentication) {
        User user=(User)authentication.getPrincipal();
        modelMap.addAttribute("username", user.getUsername());
        modelMap.addAttribute("role", user.getAuthorities());
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            userService.deleteUser(id);
            modelMap.addAttribute("message","User with ID:"+id+" delete!");
            modelMap.addAttribute("users",userService.getAllUsers());
        } catch (Exception e) {
            modelMap.addAttribute("message", "User Not Found:(");
            modelMap.addAttribute("users",userService.getAllUsers());
        }
        return "admin/panel";

    }
}
