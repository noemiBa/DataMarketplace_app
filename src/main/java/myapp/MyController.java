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

        return "index.html";
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
