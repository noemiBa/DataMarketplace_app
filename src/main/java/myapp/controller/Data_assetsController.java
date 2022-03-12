package myapp.controller;

import myapp.model.activeUser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import myapp.model.Dataset;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


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
		if(activeUser.getInstance().isActiveUserLoggedIn() || !(activeUser.getInstance().getActiveUser().isAdmin())){
			return "adminlogin.html";
		} else {
		model.addAttribute("assetTypes", asset_typesRepo.findAll());
		model.addAttribute("dataAssets", data_assetsRepo.findAll());
		if(activeUser.getInstance().isActiveUserLoggedIn()){
			model.addAttribute("loginRouting","/login");
			model.addAttribute("loginstate","Login");
		} else {
			model.addAttribute("loginRouting","/logout");
			model.addAttribute("loginstate","Log Out");
		}
		return "adminportal.html"; }
	}

	private void updateTheDataAssetTable(Integer dataId,
										 String assetname,
										 Float assetcost,
										 Integer assetsize,
										 String assetdesc,
										 Boolean featured,
										 Boolean active){
		Optional<Data_assets> assetOption = data_assetsRepo.findById(dataId);
		Data_assets asset = assetOption.get();
		asset.setAssetname(assetname);
		asset.setAssetcost(assetcost);
		asset.setAssetsize(assetsize);
		asset.setAssetdesc(assetdesc);
		asset.setFeatured(featured);
		asset.setActive(active);
		data_assetsRepo.save(asset);
	}

	@PostMapping("/add_new_dataset")
	public @ResponseBody void addNewDataAsset(@RequestParam String assetname,
											  @RequestParam String assetcost,
											  @RequestParam String assetsize,
											  @RequestParam String assetdesc,
											  @RequestParam String featured,
											  @RequestParam String active,
											  @RequestParam String typeid,
											  Model model,
											  HttpServletResponse response) throws Exception {
		float cost = Float.parseFloat(assetcost);
		int size = Integer.parseInt(assetsize);
		boolean isFeatured = Boolean.parseBoolean(featured);
		boolean isActive = Boolean.parseBoolean(active);
		Asset_types type = asset_typesRepo.getOne(Integer.parseInt(typeid));
		Data_assets asset = new Data_assets();
		asset.setAssetname(assetname);
		asset.setAssetcost(cost);
		asset.setAssetsize(size);
		asset.setAssetdesc(assetdesc);
		asset.setFeatured(isFeatured);
		asset.setActive(isActive);
		asset.setAsset_type(type);
		data_assetsRepo.save(asset);
		try {
			response.sendRedirect("/adminportal");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/update_data_asset_db")
	public @ResponseBody void updateDataAssetDB(@RequestParam String assetid,
												@RequestParam String assetname,
												@RequestParam String assetcost,
												@RequestParam String assetsize,
												@RequestParam String assetdesc,
												@RequestParam String featured,
												@RequestParam String active,
												Model model,
												HttpServletResponse response) throws Exception {
		int idNum = Integer.parseInt(assetid);
		float cost = Float.parseFloat(assetcost);
		int size = Integer.parseInt(assetsize);
		boolean isFeatured = Boolean.parseBoolean(featured);
		boolean isActive = Boolean.parseBoolean(active);
		updateTheDataAssetTable(idNum,assetname,cost,size,assetdesc,isFeatured,isActive);
		try {
			response.sendRedirect("/update_data/"+idNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/view_data/{id}")
	public String viewData(@PathVariable("id") String dataId, Model model) {
		// TEST AREA
		Dataset dataset = new Dataset();
		dataset = Dataset.json2Java("globex.json", Dataset.class);
		System.out.println(dataset.getName());
		ArrayList<Dataset> testOrder = new ArrayList<>();
		Dataset ds1 = new Dataset();
		Dataset ds2 = new Dataset();
		ds1 = Dataset.json2Java("finity.json", Dataset.class);
		ds2 = Dataset.json2Java("ninjacat.json", Dataset.class);
		Collections.shuffle(ds1.getValues());
		Collections.shuffle(ds2.getValues());
		ds1.setValues(new ArrayList<Double>(ds1.getValues().subList(0, 20)));
		ds2.setValues(new ArrayList<Double>(ds2.getValues().subList(0, 20)));
		testOrder.add(ds1);
		testOrder.add(ds2);
		Gson gson = new Gson();
		System.out.println("finitiy" + ds1.getName() + ds1.getValues().size());
		// try {
		// gson.toJson(ds1, new FileWriter("src/main/resources/static/testorder.json"));
		// } catch(IOException ie) {}
		try {
			FileWriter fw = new FileWriter("src/main/resources/static/testorder.json");
			gson.toJson(testOrder, fw);
			fw.close();
		} catch(IOException ie) {System.out.println("Error opening file");}
		

		// END TEST


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


