package com.jasonbierbrauer.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jasonbierbrauer.model.CardContent;
import com.jasonbierbrauer.model.FeatureContent;
import com.jasonbierbrauer.model.JumbotronContent;
import com.jasonbierbrauer.repository.CardContentRepository;
import com.jasonbierbrauer.repository.FeatureContentRepository;

@Controller
public class MainController extends PermissionController {

	// instance of Repositories
	private CardContentRepository card;
	private FeatureContentRepository feature;
	
	// autowire the repository to the controller
	@Autowired
	public MainController(CardContentRepository card, FeatureContentRepository feature) {
		this.card = card;
		this.feature = feature;
	}
	
	@RequestMapping("/")
	public String index(HttpSession session, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		// adds last jumbo 
		JumbotronContent jumboMain = findLastJumbo();		
		if (jumboMain != null) {
			session.setAttribute("jumboMain", jumboMain);
		}	
		// adds the three most current cards
		List<CardContent> cards = card.findAll();
		CardContent card1 = new CardContent();
		CardContent card2 = new CardContent();
		CardContent card3 = new CardContent();
		// reverse list to get newest ones first
		if (cards.size() > 0) {
			for (int i = 0; i <= cards.size()-1; i++) {
				if (cards.get(i).getType().equals("card1")) {
					card1 = cards.get(i);
					session.setAttribute("card1", card1);
				} else if (cards.get(i).getType().equals("card2")) {
					card2 = cards.get(i);
					session.setAttribute("card2", card2);
				} else if (cards.get(i).getType().equals("card3")) {
					card3 = cards.get(i);
					session.setAttribute("card3", card3);
				}
			}
		} else {
			card1.setHeadline("Blue's website and software design");
			String content = "Welcome to Blue's website and software design. This site is brand new "
					+ "and is the model for the site I hope to market. Everything is self contained "
					+ "and can be edited from the administration account. If your interesterd, send "
					+ "me an email on the contact page.";
			card1.setContent(content);
			card1.setUrl("https://s3-us-west-2.amazonaws.com/blue-company-images/computer-02.jpg");
			card2.setHeadline("Blue's website and software design");
			card2.setContent(content);
			card2.setUrl("https://s3-us-west-2.amazonaws.com/blue-company-images/computer-02.jpg");
			card3.setHeadline("Blue's website and software design");
			card3.setContent(content);
			card3.setUrl("https://s3-us-west-2.amazonaws.com/blue-company-images/computer-02.jpg");
			session.setAttribute("card1", card1);
			session.setAttribute("card2", card2);
			session.setAttribute("card3", card3);
		}	
		
		// adds the feature at bottom
		FeatureContent featureMain = findLastFeature();
		if (featureMain != null) {
			session.setAttribute("featureMain", featureMain);
		}	
			
		// admin user
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}
		return "main/index";
	}
	
	
	public CardContent findByType(String type) {
		List<CardContent> cards = card.findAll();
		// goes backwards to get newest.
		if (cards != null) {
			for (int i = 0; i < cards.size(); i++) {
				if (cards.get(i).getType() == type) {
					return cards.get(i);
				}
			}
		}		
		return null;
	}
	
	
	public FeatureContent findLastFeature() {
		long size = feature.count();
		FeatureContent last = new FeatureContent();
		if (size > 0) {
			last = feature.getOne(size);
		} else {
			last.setHeadline("Blue's website and software design");
			String content = "Welcome to Blue's website and software design. This site is brand new "
					+ "and is the model for the site I hope to market. Everything is self contained "
					+ "and can be edited from the administration account. If your interesterd, send "
					+ "me an email on the contact page.";
			last.setContent(content);
			last.setUrl("https://s3-us-west-2.amazonaws.com/blue-company-images/computer-02.jpg");
		}
		return last;
	}


}
