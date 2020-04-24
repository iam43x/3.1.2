package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping(value = "/")
	public String session(Authentication authentication){
		if (authentication!= null) {
			User user=(User)authentication.getPrincipal();
			return user.getAuthorities().contains(Role.ADMIN)?"redirect:/admin":"redirect:/user";
		} else {
			return "login";
		}
	}

    @GetMapping(value = "/reg")
    public String regPage() {
        return "reg";
    }

    @PostMapping(value = "/reg")
    public String addUser(WebRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Set<Role> user = new HashSet<>();
        user.add(Role.USER);
        userService.addUser(
                User.builder().
                        authorities(user).
                        accountNonExpired(true).
                        username(username).
                        password(password).
                        enabled(true).
                        accountNonLocked(true).
                        credentialsNonExpired(true).
                        build());
        return "redirect:/login";
    }

    @GetMapping(value = "login")
    public String loginPage() {
    	return "login";

    }

    @GetMapping(value = "/user")
    public String User(ModelMap modelMap, Authentication authentication) {
        User user=(User)authentication.getPrincipal();
        modelMap.addAttribute("username",user.getUsername());
        modelMap.addAttribute("role",user.getAuthorities());
        return "userPage";
    }


}