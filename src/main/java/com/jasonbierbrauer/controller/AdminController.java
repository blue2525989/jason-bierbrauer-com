package com.jasonbierbrauer.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jasonbierbrauer.model.JumbotronContent;

@Controller
public class AdminController extends PermissionController {

	@RequestMapping("/admin")
	public String admin(HttpSession session, Model model) {
		addFragments(model);

		addTypesForMenu(model);
		// admin user
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}		
		return "admin/admin";
	}
	
	@RequestMapping("/saved")
	public String saved(HttpSession session, Model model) {
		addFragments(model);
		// adds last jumbo 
		addTypesForMenu(model);
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
		return "admin/saved";
	}
	
	@RequestMapping("/list-all")
	public String listAll(HttpSession session, Model model) {
		addFragments(model);
		addTypesForMenu(model);
		// admin user
		if (hasAdminRole()) {
			session.setAttribute("adminrole", hasAdminRole());
		}
		// regular user
		else if (hasUserRole()) {
			session.setAttribute("userrole", hasUserRole());
		}		
		return "admin/list-all";
	}
	
	@RequestMapping("/list-all-type")
	public String listAllType(HttpSession session, Model model) {
		addFragments(model);
		addTypesForMenu(model);
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
