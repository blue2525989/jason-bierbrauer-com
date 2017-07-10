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
import com.jasonbierbrauer.model.ContactFeatureContent;
import com.jasonbierbrauer.model.Image;
import com.jasonbierbrauer.repository.ContactFeatureContentRepository;
import com.jasonbierbrauer.repository.ImageRepository;

@Controller
public class EditContactController extends PermissionController {
	

	// instance of Repositories	
	private ImageRepository imgRepo;
	private ContactFeatureContentRepository feature;
	
	// autowire the repository to the controller
	@Autowired
	public EditContactController(ContactFeatureContentRepository feature, ImageRepository imgRepo) {
		this.feature = feature;
		this.imgRepo = imgRepo;
	}


	@RequestMapping("/edit-contact")
	public String admin(HttpSession session, Model model) {
		addTypesForMenu(model);		
		addFragments(model);
		// adds full list from gallery
		// need to work on slimming down list.
		List<Image> imageList = imgRepo.findAll();
		if (imageList != null) {
			session.setAttribute("imageList", imageList);
		}
		
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}		
		return "admin/edit-contact";
	}
	
/* for editing feature content */
	
	//@PostMapping(path="/edit-contact/edit-contact-content")
	// request params to save
	public String addNewContactFeature(Model model, @RequestParam String headline
			, @RequestParam String content, @RequestParam String url) {

		ContactFeatureContent featureCon = new ContactFeatureContent();
		featureCon.setHeadline(headline);
		featureCon.setContent(content);
		featureCon.setUrl(url);
		feature.save(featureCon);
		return "redirect:/edit-contact";
	}
	
	// delete element
	//@GetMapping(path="/delete-contact")
	public String deleteContactFeature(Long ID, Model model) {
		addTypesForMenu(model);
		addFragments(model);
		feature.delete(ID);
		String saved = "The Contact Content with ID " + ID + " has been deleted.";
		model.addAttribute("saved", saved);
		return "admin/saved";
	}
		
	// list all element
	@RequestMapping("/list-contact")
	public String listAllContactFeatures(Model model) {
		addTypesForMenu(model);
		addFragments(model);
		List<ContactFeatureContent> featureList = feature.findAll();
		if (featureList != null) {
			model.addAttribute("listMain", featureList);
		}	
		return "admin/list-all";
	}
}
