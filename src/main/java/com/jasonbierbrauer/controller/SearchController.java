package com.jasonbierbrauer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jasonbierbrauer.model.InventoryItem;
import com.jasonbierbrauer.model.JumbotronContent;
import com.jasonbierbrauer.repository.InventoryRepository;

@Controller
public class SearchController extends PermissionController {


	// instance of Repositories
	protected InventoryRepository inventory;
	
	// autowire the repository to the controller
	@Autowired
	public SearchController(InventoryRepository inventory) {
		this.inventory = inventory;
	}


	@RequestMapping("/search")
	public String searchItem(@RequestParam String item, HttpSession session, Model model) {
		List<InventoryItem> items = findByTypeItem(item);	
		items.addAll(findByHeadlineItem(items, item));
		if (items != null) {
			model.addAttribute("items", items);
		}
		// adds last jumbo 
		JumbotronContent jumboMain = findLastJumbo();		
		if (jumboMain != null) {
			session.setAttribute("jumboMain", jumboMain);
		}
		// admin user
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}
		return "inventory/inventory";
	}
	
	// use this method second
	
	/* working for now with multiple search words but 
	 * catches other items not containing main keywords
	 * 
	 */
	public List<InventoryItem> findByHeadlineItem(List<InventoryItem> itemsMain, String type) {
		List<InventoryItem> items = inventory.findAll();
		List<InventoryItem> realItems = new ArrayList<InventoryItem>();
		type = type.toLowerCase();
		if (type.contains(" ")) {
			String[] words = type.split(" ");
			boolean containsKey = true;
			for (int j = 0; j <= items.size()-1; j++) {
				for (int i = 1; i < words.length; i++) {
					if (items.get(j).getHeadline().toLowerCase().contains(words[i]) && 
							items.get(j).getHeadline().toLowerCase().contains(words[0]) && 
							!itemsMain.contains(items.get(i))) {
						containsKey = true;					
					}
					else if (items.get(j).getHeadline().equalsIgnoreCase(words[i]) && 
							items.get(j).getHeadline().toLowerCase().contains(words[0]) && 
							!itemsMain.contains(items.get(i))) {
						containsKey = true;
					}
					else {
						containsKey = false;
					}
				}
				if (containsKey) {
					realItems.add(items.get(j));
				}
			}
		}
		// go through list and add if type matches
		else {
			for (int i = 0; i <= items.size()-1; i++) {				
				if (items.get(i).getHeadline().toLowerCase().contains(type) && 
						!itemsMain.contains(items.get(i))) {
					realItems.add(items.get(i));					
				}
				else if (items.get(i).getHeadline().equalsIgnoreCase(type) && 
						!itemsMain.contains(items.get(i))) {
					realItems.add(items.get(i));
				}
			}
		}		
		return realItems;
	}

	// use this method first
	public List<InventoryItem> findByTypeItem(String type) {
		List<InventoryItem> items = inventory.findAll();
		List<InventoryItem> realItems = new ArrayList<InventoryItem>();
		// go through list and add if type matches
		if (items != null) {
			for (int i = 0; i <= items.size()-1; i++) {
				if (items.get(i).getType().equalsIgnoreCase(type)) {
					realItems.add(items.get(i));
				}
			}
		}		
		return realItems;
	}

}
