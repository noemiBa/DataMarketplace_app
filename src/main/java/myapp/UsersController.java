package myapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UsersController {
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
            response.sendRedirect("/"); // temporarily redirects to index.html
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
            if (u.getPassword().compareTo(password) == 0) {
                validPassword = true;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }

        // valid password will be false if password is wrong or username is not valid
        if(!validPassword) {
            // add code to deal with an invalid username or password

//            System.out.println("!!!!!!!!!!!!!!! Login Failed !!!!!!!!!!");
            try{
                response.sendRedirect("/login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // add any necessary bits to have the user logged into the site
            Users u = usersRepository.findByUsername(username);
            activeUser.getInstance().loginUser(u);
//            System.out.println("!!!!!!!!!!!!!!! Login Succeeded !!!!!!!!!!");
            try {
                response.sendRedirect("/"); // temporarily redirects to index.html
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
