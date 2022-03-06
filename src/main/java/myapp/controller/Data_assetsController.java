package myapp.controller;

import myapp.model.activeUser;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import myapp.model.Asset_types;
import myapp.repository.Asset_typesRepository;
import myapp.model.Data_assets;
import myapp.repository.Data_assetsRepository;

import javax.servlet.http.HttpServletResponse;

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

	@GetMapping("/adminportal")
	public String adminportal(Model model) {
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
		return "adminportal.html";
	}

	private void updateTheDataAssetTable(Integer dataId,
										 String assetname,
										 Float assetcost,
										 Integer assetsize,
										 String assetdesc,
										 Boolean featured){
		Data_assets asset = data_assetsRepo.getOne(dataId);
		asset.setAssetname(assetname);
		asset.setAssetcost(assetcost);
		asset.setAssetsize(assetsize);
		asset.setAssetdesc(assetdesc);
		asset.setFeatured(featured);
		data_assetsRepo.save(asset);
	}

	@PostMapping("/update_data_asset_db")
	public @ResponseBody void updateDataAssetDB(@RequestParam String dataId,
												@RequestParam String assetname,
												@RequestParam String assetcost,
												@RequestParam String assetsize,
												@RequestParam String assetdesc,
												@RequestParam String featured,
												Model model,
												HttpServletResponse response) throws Exception {
		int idNum = Integer.parseInt(dataId);
		float cost = Float.parseFloat(assetcost);
		int size = Integer.parseInt(assetsize);
		boolean isFeatured = Boolean.parseBoolean(featured);
		updateTheDataAssetTable(idNum,assetname,cost,size,assetdesc,isFeatured);
		try {
			response.sendRedirect("/adminportal");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/view_data/{id}")
	public String viewData(@PathVariable("id") String dataId, Model model) {
		// Setup Links for login/ out
		if(activeUser.getInstance().isActiveUserLoggedIn()){
			model.addAttribute("loginRouting","/login");
			model.addAttribute("loginstate","Login");
			model.addAttribute("loggedIn", false);
		} else {
			model.addAttribute("loginRouting","/logout");
			model.addAttribute("loginstate","Log Out");
			model.addAttribute("loggedIn", true);
		}
		int idNum;
		// Convert id to number. If not a number return non existant view
		try {
			idNum = Integer.parseInt(dataId);
		} catch(NumberFormatException nfe) {
			model.addAttribute("exists", false);
			return "view_data.html";
		}

		Optional<Data_assets> assetOption = data_assetsRepo.findById(idNum);
		Data_assets asset;
		try {
			asset = assetOption.get();
		} catch(NoSuchElementException nsee) {
			model.addAttribute("exists", false);
			return "view_data.html";
		}
		model.addAttribute("exists", true);
		model.addAttribute("asset", asset);
		return "view_data.html";


	}

	@GetMapping("/update_data/{id}")
	public String updateDataView(@PathVariable("id") String dataId, Model model) {
		// Setup Links for login/ out
		if(activeUser.getInstance().isActiveUserLoggedIn()){
			model.addAttribute("loginRouting","/login");
			model.addAttribute("loginstate","Login");
			model.addAttribute("loggedIn", false);
		} else {
			model.addAttribute("loginRouting","/logout");
			model.addAttribute("loginstate","Log Out");
			model.addAttribute("loggedIn", true);
		}
		int idNum;
		// Convert id to number. If not a number return non existant view
		try {
			idNum = Integer.parseInt(dataId);
		} catch(NumberFormatException nfe) {
			model.addAttribute("exists", false);
			return "update_data.html";
		}

		Optional<Data_assets> assetOption = data_assetsRepo.findById(idNum);
		Data_assets asset;
		try {
			asset = assetOption.get();
		} catch(NoSuchElementException nsee) {
			model.addAttribute("exists", false);
			return "update_data.html";
		}
		model.addAttribute("exists", true);
		model.addAttribute("asset", asset);
		return "update_data.html";


	}
}
