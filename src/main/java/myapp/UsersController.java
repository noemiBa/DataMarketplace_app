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
                                            @RequestParam String fullname,
                                            @RequestParam String email,
                                            HttpServletResponse response) throws IOException {
        Users u = new Users();
        u.setUsername(username);
        u.setPassword(password);
        u.setFullname(fullname);
        u.setEmail(email);
        usersRepository.save(u);
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
        // if the user cannot be validated then reset the login page and alert user
        if(!validateUserLogin(username,password)) {
            System.out.println("!!!!!!!!!!!!!!! Login Failed !!!!!!!!!!");
            try{
                response.sendRedirect("/login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // add any necessary bits to have the user logged into the site
            System.out.println("!!!!!!!!!!!!!!! Login Succeeded !!!!!!!!!!");
            try {
                response.sendRedirect("/"); // temporarily redirects to index.html
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Boolean validateUserLogin(String username, String password) {
        Iterable<Users> usersList = usersRepository.findByUsername(username);

        if(usersList.iterator().hasNext()){
            Users u = usersList.iterator().next();
            System.out.println("!!!!!!!!!!!!!!!! Username: " +
                    u.getUsername() + ", Password: " + u.getPassword());
            System.out.println("!!! The given password is: " + password);
            if(password == u.getPassword()){ // !!! this boolean is not working despite being equal
                System.out.println(" password matches ");
            }
            return true;
        }
        return false;
    }

}
