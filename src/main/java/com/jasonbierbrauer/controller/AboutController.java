package com.jasonbierbrauer.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jasonbierbrauer.model.AboutContent;
import com.jasonbierbrauer.model.JumbotronContent;
import com.jasonbierbrauer.repository.AboutContentRepository;

@Controller
public class AboutController extends PermissionController {
	
	// instance of Repositories
	private AboutContentRepository about;
	// autowire the repository to the controller
	@Autowired
	public AboutController(AboutContentRepository about) {
		this.about = about;
	}

	
	@RequestMapping("/about")
	public String about(HttpSession session, Model model) {
		addFragments(model);
		// adds last jumbo 
		JumbotronContent jumboMain = findLastJumbo();		
		if (jumboMain != null) {
			session.setAttribute("jumboMain", jumboMain);
		}
		addTypesForMenu(model);
		// try just counting instead of returning full list to speed up times
		long size = about.count();
		if (size > 0) {
			List<AboutContent> aboutCon = about.findAll();
			AboutContent last = aboutCon.get(aboutCon.size()-1);
			if (last != null) {
				session.setAttribute("aboutMain", last);
			}
		} else {
			AboutContent aboutCon = new AboutContent();
			aboutCon.setHeadline("Blue's website and software design");
			String content = "Welcome to Blue's website and software design. This site is brand new "
					+ "and is the model for the site I hope to market. Everything is self contained "
					+ "and can be edited from the administration account. If your interesterd, send "
					+ "me an email on the contact page.";
			aboutCon.setContent(content);
			aboutCon.setUrl("https://s3-us-west-2.amazonaws.com/blue-company-images/system/banner01.jpg");
			session.setAttribute("aboutMain", aboutCon);
			
		}
		
		// admin user
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}
		return "about/about";
	}
}
