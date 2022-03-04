package myapp.controller;

import myapp.model.Data_assets;
import myapp.model.Users;
import myapp.model.activeUser;
import myapp.repository.Data_assetsRepository;
import myapp.repository.UsersRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class MyController {

    @Autowired
    private Data_assetsRepository data_assetsRepo;
    private boolean invalidPassword = false;
    private boolean goToCartAfterLogin = false;

    @GetMapping("/")
    public String index(Model model) {
        // Grab featured datasets (Max 4)
        List<Data_assets> featuredAssets = data_assetsRepo.findByFeaturedTrue();
        int count = Math.max(4, featuredAssets.size());
        // Send count and list to thymeleaf in index
        model.addAttribute("featuredAssets", featuredAssets);
        model.addAttribute("featuredCount", count);
        System.out.println("Active User is Null: " + (activeUser.getInstance().isActiveUserLoggedIn()));
        if(activeUser.getInstance().isActiveUserLoggedIn()){
            System.out.println(activeUser.getInstance().toString());
            model.addAttribute("showJoinUs",true);
            model.addAttribute("loginRouting","/login");
            model.addAttribute("loginstate","Login");
        } else {
            System.out.println(activeUser.getInstance().toString());
            model.addAttribute("showJoinUs",false);
            model.addAttribute("loginRouting","/logout");
            model.addAttribute("loginstate","Log Out");
        }

        return "index.html";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("invalidpassword",invalidPassword);
        return "login.html";
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws Exception{
        activeUser.getInstance().logoutUser();
        try {
            response.sendRedirect("/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/newuser")
    public String newuser() { return "newuser.html"; }

    @GetMapping("/adminlogin")
    public String adminlogin() { return "adminlogin.html"; }



    @Autowired
    private UsersRepository usersRepository;

    @PostMapping(path = "/addnewuser")
    public @ResponseBody void addNewUser (@RequestParam String username,
                                          @RequestParam String password,
                                          @RequestParam String name,
                                          @RequestParam String email,
                                          HttpServletResponse response) throws IOException {
        Users u = new Users();
        u.setUsername(username);
        u.setPassword(password);
        u.setName(name);
        u.setEmail(email);
        u.setAdmin(false);
        u.setActive(true);

        usersRepository.save(u);
        activeUser.getInstance().loginUser(u);
        try{
            if(goToCartAfterLogin){
                response.sendRedirect("/shoppingcart");
            } else {
                response.sendRedirect("/");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/viewallusers")
    public @ResponseBody Iterable<Users> viewAllUsers() {
        return usersRepository.findAll();
    }

    @PostMapping(path = "/userlogin")
    public @ResponseBody void userlogin (@RequestParam String username,
                                         @RequestParam String password,
                                         HttpServletResponse response) throws IOException {
        // logout current user
        activeUser.getInstance().logoutUser();
        // if the user cannot be validated then reset the login page and alert user
        boolean validPassword = false;
        // first try catch checks for a valid username, if so verifies password
        try {
            Users u = usersRepository.findByUsername(username);
            if (u == null){
                validPassword = false;
                invalidPassword = true;
            } else if (u.getPassword().compareTo(password) == 0) {
                validPassword = true;
                invalidPassword = false;
            } else {
                invalidPassword = true;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }

        // valid password will be false if password is wrong or username is not valid
        if(!validPassword) {
            // add code to deal with an invalid username or password
            try{
                response.sendRedirect("/login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // add any necessary bits to have the user logged into the site
            Users u = usersRepository.findByUsername(username);
            activeUser.getInstance().loginUser(u);
            try {
                if(goToCartAfterLogin){
                    goToCartAfterLogin = false;
                    response.sendRedirect("/shoppingcart");
                } else {
                    response.sendRedirect("/");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getIdFromUsername(String username){
        Users u = usersRepository.findByUsername(username);
        return u.getId();
    }

}
