package myapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
            model.addAttribute("loginRouting","/login");
            model.addAttribute("loginstate","Login");
        } else {
            System.out.println(activeUser.getInstance().toString());
            model.addAttribute("loginRouting","/logout");
            model.addAttribute("loginstate","Log Out");
        }

        return "index.html";
    }

    @GetMapping("/login")
    public String login() {
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

    @GetMapping("/shoppingcart")
    public String shoppingcart() {
        return "shoppingcart.html";
    }

}
