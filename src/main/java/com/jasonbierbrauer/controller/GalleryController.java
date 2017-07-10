package com.jasonbierbrauer.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jasonbierbrauer.model.GalleryCardContent;
import com.jasonbierbrauer.model.JumbotronContent;
import com.jasonbierbrauer.repository.GalleryCardContentRepository;

@Controller
public class GalleryController extends PermissionController {

	private GalleryCardContentRepository card;
	
	public GalleryController(GalleryCardContentRepository card) {
		this.card = card;
	}
	
	@RequestMapping("/gallery")
	public String gallery(HttpSession session, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		// adds the three most current cards
		findLastNineCards(model);
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
		return "gallery/gallery";
	}
	
	private void findLastNineCards(Model model) {
		List<GalleryCardContent> realList = card.findAll();
			if (realList != null) {
				model.addAttribute("cardList", realList);
			}
	}
}
