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
import com.jasonbierbrauer.model.AboutContent;
import com.jasonbierbrauer.model.Image;
import com.jasonbierbrauer.repository.AboutContentRepository;
import com.jasonbierbrauer.repository.ImageRepository;

@Controller
public class EditAboutUsController extends PermissionController {
	
	// instance of Repositories
	private AboutContentRepository about;
	private ImageRepository imgRepo;
	// autowire the repository to the controller
	@Autowired
	public EditAboutUsController(AboutContentRepository about, ImageRepository imgRepo) {
		this.about = about;
		this.imgRepo = imgRepo;
	}

	@RequestMapping("/edit-about")
	public String admin(HttpSession session, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		
		// adds full list from gallery
		// need to work on slimming down list.
		List<Image> imageList = imgRepo.findAll();
		if (imageList != null) {
			session.setAttribute("imageList", imageList);
		}
		// admin user
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}		
		return "admin/edit-about-us";
	}
	
/* for editing feature content */
	
	//@PostMapping(path="/edit-about/edit-about-content")
	// request params to save
	public String addNewFeature(Model model, @RequestParam String headline
			, @RequestParam String content, @RequestParam String url) {

		AboutContent aboutCon = new AboutContent();
		aboutCon.setHeadline(headline);
		aboutCon.setContent(content);
		aboutCon.setUrl(url);
		about.save(aboutCon);
		return "redirect:/edit-about";
	}
	
	// delete element
	//@GetMapping(path="/delete-about-content")
	public String deleteFeature(Long ID, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		about.delete(ID);
		String saved = "The About Content with ID " + ID + " has been deleted.";
		model.addAttribute("saved", saved);
		return "admin/saved";
	}
		
	// list all element
	@RequestMapping("/list-about-content")
	public String listAllFeatures(Model model) {
		addFragments(model);
		addTypesForMenu(model);
		List<AboutContent> aboutList = about.findAll();
		if (aboutList != null) {
			model.addAttribute("listMain", aboutList);
		}	
		return "admin/list-all";
	}
}
