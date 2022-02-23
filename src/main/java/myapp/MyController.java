package myapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Controller
public class MyController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/datasets_all")
    public String datasets() {
        return "datasets_all.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/newuser")
    public String newuser() { return "newuser.html"; }

    @GetMapping("/shoppingcart")
    public String shoppingcart() {
        return "shoppingcart.html";
    }

}
