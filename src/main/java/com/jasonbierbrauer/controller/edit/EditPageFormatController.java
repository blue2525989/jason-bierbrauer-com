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
import com.jasonbierbrauer.model.Fragment;
import com.jasonbierbrauer.model.Image;
import com.jasonbierbrauer.repository.AboutContentRepository;
import com.jasonbierbrauer.repository.FragmentRepository;
import com.jasonbierbrauer.repository.ImageRepository;

@Controller
public class EditPageFormatController extends PermissionController {
	
	// instance of Repositories
	private FragmentRepository frags;;
	private ImageRepository imgRepo;
	// autowire the repository to the controller
	@Autowired
	public EditPageFormatController(FragmentRepository frags, ImageRepository imgRepo) {
		this.frags = frags;
		this.imgRepo = imgRepo;
	}

	@RequestMapping("/edit-site-format")
	public String siteFormat(HttpSession session, Model model) {
		addTypesForMenu(model);
		addFragments(model);
		
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
		return "admin/edit-site-format";
	}
	
/* for editing feature content */
	
	//@PostMapping(path="/edit-website/edit-fragment")
	// request params to save
	public String addNewIcon(Model model, @RequestParam String iconUrl, @RequestParam String bgColor,
			@RequestParam String title, @RequestParam String metaDescription,
			@RequestParam String metaKeywords) {

		Fragment frag = new Fragment();
		frag.setIconUrl(iconUrl);
		frag.setTitle(title);
		frag.setBgColor(bgColor);
		frag.setMetaDescription(metaDescription);
		frag.setMetaKeywords(metaKeywords);
		frags.save(frag);
		return "redirect:/edit-site-format";
	}
	
	// delete element
	//@GetMapping(path="/delete-fragment")
	public String deleteIcon(Long ID, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		frags.delete(ID);
		String saved = "The fragment with ID " + ID + " has been deleted.";
		model.addAttribute("saved", saved);
		return "admin/saved";
	}
		
	// list all element
	@RequestMapping("/list-fragments")
	public String listAllIcons(Model model) {
		addFragments(model);
		addTypesForMenu(model);
		List<Fragment> fragList = frags.findAll();
		if (fragList != null) {
			model.addAttribute("listMain", fragList);
		}	
		return "admin/list-all-fragments";
	}

}
