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
import com.jasonbierbrauer.model.GalleryCardContent;
import com.jasonbierbrauer.model.Image;
import com.jasonbierbrauer.repository.GalleryCardContentRepository;
import com.jasonbierbrauer.repository.ImageRepository;

@Controller
public class EditGalleryController extends PermissionController {

	private GalleryCardContentRepository card;
	private ImageRepository imgRepo;
	
	@Autowired
	public EditGalleryController(GalleryCardContentRepository card, ImageRepository imgRepo) {
		this.card = card;
		this.imgRepo = imgRepo;
	}
	
	@RequestMapping("/edit-gallery")
	public String admin(HttpSession session, Model model) {
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
		return "admin/edit-gallery";
	}
	
	//@PostMapping(path="/edit-gallery/edit-card")
	// request params to save
	public String addNewCardGallery(Model model, @RequestParam String headline
			, @RequestParam String content, @RequestParam String url) {

		GalleryCardContent cardNew = new GalleryCardContent();
		cardNew.setHeadline(headline);
		cardNew.setContent(content);
		cardNew.setUrl(url);
		card.save(cardNew);
		return "redirect:/edit-gallery";
	}
	

	// delete element
	//@GetMapping(path="/delete-card-gallery")
	public String deleteCard(Long ID, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		card.delete(ID);
		String saved = "The card with ID " + ID + " has been deleted.";
		model.addAttribute("saved", saved);
		return "admin/saved";
	}
		
	// list all element
	@RequestMapping("/list-card-gallery")
	public String listAllcards(Model model) {
		addFragments(model);
		addTypesForMenu(model);
		List<GalleryCardContent> cardList = card.findAll();
		if (cardList != null) {
			model.addAttribute("listMain", cardList);
		}	
		return "admin/list-all-gallerycard";
	}
	
	
}
