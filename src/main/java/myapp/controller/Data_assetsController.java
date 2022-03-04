package myapp.controller;

import myapp.model.activeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;


import myapp.model.Asset_types;
import myapp.repository.Asset_typesRepository;
import myapp.model.Data_assets;
import myapp.repository.Data_assetsRepository;

@Controller
public class Data_assetsController {
	@Autowired
	private Asset_typesRepository asset_typesRepo;
	@Autowired
	private Data_assetsRepository data_assetsRepo;

	@GetMapping("/viewalltypes")
	public @ResponseBody Iterable<Asset_types> viewAllTypes() {
		return asset_typesRepo.findAll();
	}

	@GetMapping("/viewalldata")
	public @ResponseBody Iterable<Data_assets> viewAllData() {
		return data_assetsRepo.findAll();
	}

	@GetMapping("/viewfeatureddata")
	public @ResponseBody Iterable<Data_assets> viewFeaturedData() {
		return data_assetsRepo.findByFeaturedTrue();
	}

	@GetMapping("/datasets_all")
    public String datasets(Model model) {
		// send down types for filtering and assets
		model.addAttribute("assetTypes", asset_typesRepo.findAll());
		model.addAttribute("dataAssets", data_assetsRepo.findAll());
		if(activeUser.getInstance().isActiveUserLoggedIn()){
			model.addAttribute("loginRouting","/login");
			model.addAttribute("loginstate","Login");
		} else {
			model.addAttribute("loginRouting","/logout");
			model.addAttribute("loginstate","Log Out");
		}
        return "datasets_all.html";
    }
}
