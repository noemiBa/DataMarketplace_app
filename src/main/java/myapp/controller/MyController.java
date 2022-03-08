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
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
public class MyController {

    @Autowired
    private Data_assetsRepository data_assetsRepo;
    private boolean goToCartAfterLogin = false;
    private boolean invalidPassword = false;
    private boolean usedUsername = false;

    @GetMapping("/")
    public String index(Model model) {
        // Grab featured datasets (Max 4)
        List<Data_assets> featuredAssets = data_assetsRepo.findByFeaturedTrue();
        //featuredAssets.removeIf(asset -> !asset.isActive());
        int count = Math.min(4, featuredAssets.size());
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
    public String login(@RequestParam(required=false) String after, Model model) {
        model.addAttribute("invalidpassword",invalidPassword);
        model.addAttribute("usedusername",usedUsername);
        model.addAttribute("redirectTo",redirectTo);
        System.out.println(after);
        if (after != null)
            model.addAttribute("goHereAfterLogin", after);
        if(activeUser.getInstance().isActiveUserLoggedIn()){
            model.addAttribute("loginRouting","/adminlogin");
            model.addAttribute("loginstate","Admin Login");
        } else {
            model.addAttribute("loginRouting","/logout");
            model.addAttribute("loginstate","Log Out");
        }
        return "login.html";
    }

    private String redirectTo = "/";

    @GetMapping("/adminlogin")
    public String adminlogin(Model model) {
        model.addAttribute("invalidpassword",invalidPassword);


        if(activeUser.getInstance().isActiveUserLoggedIn()){
            model.addAttribute("loginRouting","/login");
            model.addAttribute("loginstate","Login");
        } else {
            model.addAttribute("loginRouting","/logout");
            model.addAttribute("loginstate","Log Out");
        }
        return "adminlogin.html";
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws Exception{
        activeUser.getInstance().logoutUser();
        this.redirectTo = "/";
        try {
            response.sendRedirect("/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/newuser")
    public String newuser() { return "newuser.html"; }

    @GetMapping("/add_new_asset")
    public String addnewasset() { return "add_new_asset.html"; }

    @GetMapping("/generate_dataset")
    public String generate_dataset() {return "generate_dataset.html";}

    @Autowired
    private UsersRepository usersRepository;

    private void addUserToDatabase(String username, String password, String name, String email, boolean admin){
        Users u = new Users();
        u.setUsername(username);
        u.setPassword(password);
        u.setName(name);
        u.setEmail(email);
        u.setAdmin(admin);
        u.setActive(true);
        usersRepository.save(u);
    }

    @PostMapping(path = "/addnewuser")
    public @ResponseBody void addNewUser (@RequestParam String username,
                                          @RequestParam String password,
                                          @RequestParam String name,
                                          @RequestParam String email,
                                          @RequestParam String redirectTo,
                                          HttpServletResponse response) throws IOException {
        if(usersRepository.findByUsername(username) != null) {
            try {
                usedUsername = true;
                response.sendRedirect("/login");
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            addUserToDatabase(username,password,name,email,false);
            activeUser.getInstance().loginUser(usersRepository.findByUsername(username));
            try{
                response.sendRedirect(redirectTo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    @GetMapping("/viewallusers")
    public @ResponseBody Iterable<Users> viewAllUsers() {
        return usersRepository.findAll();
    }

    @PostMapping(path = "/userlogin")
    public @ResponseBody void userlogin (@RequestParam String username,
                                         @RequestParam String password,
                                         @RequestParam String redirectTo,
                                         Model model,
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
                this.redirectTo = redirectTo;
                response.sendRedirect("/login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // add any necessary bits to have the user logged into the site
            Users u = usersRepository.findByUsername(username);
            activeUser.getInstance().loginUser(u);
            try {
                response.sendRedirect(redirectTo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping(path = "/validateadminlogin")
    public @ResponseBody void validateadminlogin (@RequestParam String username,
                                         @RequestParam String password,
                                         HttpServletResponse response) throws IOException {
        // logout current user
        activeUser.getInstance().logoutUser();
        // if the user cannot be validated then reset the login page and alert user
        boolean validPassword = false;
        // first try catch checks for a valid username, if so verifies password
        Users u = usersRepository.findByUsername(username);
        try {
            if (u == null){
                validPassword = false;
                invalidPassword = true;
            } else if (u.getPassword().compareTo(password) == 0 && u.isAdmin()) {
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
            try{
                response.sendRedirect("/adminlogin");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            activeUser.getInstance().loginUser(u);
            try {
                response.sendRedirect("/adminportal");
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
