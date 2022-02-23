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

}
