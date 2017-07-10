package com.jasonbierbrauer.controller.edit;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jasonbierbrauer.controller.PermissionController;
import com.jasonbierbrauer.model.Image;
import com.jasonbierbrauer.model.ImageTypes;
import com.jasonbierbrauer.model.InventoryItem;
import com.jasonbierbrauer.model.PayPalButton;
import com.jasonbierbrauer.repository.ImageRepository;
import com.jasonbierbrauer.repository.ImageTypeRepository;
import com.jasonbierbrauer.repository.InventoryRepository;
import com.jasonbierbrauer.repository.PayPalButtonRepository;

@Controller
public class EditInventoryController extends PermissionController {


	// instance of Repositories
	private InventoryRepository inventory;
	private ImageRepository imgRepo;
	private PayPalButtonRepository buttons;
	private ImageTypeRepository imgTypeRepo;
	
	// autowire the repository to the controller
	@Autowired
	public EditInventoryController(InventoryRepository inventory, ImageRepository imgRepo,
			PayPalButtonRepository buttons, ImageTypeRepository imgTypeRepo) {
		this.inventory = inventory;
		this.imgRepo = imgRepo;
		this.buttons = buttons;
		this.imgTypeRepo = imgTypeRepo;
	}
	
	@RequestMapping("/edit-inventory")
	public String adminInventory(HttpSession session, Model model) {
		addTypesForMenu(model);		
		addFragments(model);
		// adds full list from gallery
		// need to work on slimming down list.
		List<Image> imageList = imgRepo.findAll();
		if (imageList != null) {
			session.setAttribute("imageList", imageList);
		}
		List<PayPalButton> buttonList = buttons.findAll();
		if (buttonList != null) {
			session.setAttribute("buttonList", buttonList);
		}
		List<ImageTypes> fullList = imgTypeRepo.findAll();
		if (fullList != null) {
			model.addAttribute("fullList", fullList);
		}
		// admin user
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}		
		return "admin/edit-inventory";
	}
	
	/* for editing services content */
	
	//@PostMapping(path="/edit-inventory/edit-item")
	// request params to save
	public String addNewItem(Model model, @RequestParam String headline
			, @RequestParam String content, @RequestParam String url, @RequestParam String type, 
			@RequestParam String price, @RequestParam String button) {
		InventoryItem item = new InventoryItem();;
		item.setHeadline(headline); 
		item.setUrl(url);
		item.setContent(content);
		item.setType(type);
		item.setPrice(price);
		item.setButton(button);
		inventory.save(item);
		return "redirect:/edit-inventory";
	}
	
	// delete element
	//@GetMapping(path="/delete-item")
	public String deleteItem(Long ID, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		String name = inventory.getOne(ID).getHeadline();
		inventory.delete(ID);
		String saved = "The Item " + name + " has been deleted.";
		model.addAttribute("saved", saved);
		return "admin/saved";
	}
		
	// list all element
	@RequestMapping("/list-items")
	public String listAllitems(Model model, HttpSession session) {
		addFragments(model);
		addTypesForMenu(model);
		List<InventoryItem> items = inventory.findAll();
		// Iterator iter = jumboList.iterator();
		if (items != null) {
			model.addAttribute("listMain", items); /* all named list for uniformity */
		}	
		// admin user
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}
		return "admin/list-all-type";
	}
	
}
