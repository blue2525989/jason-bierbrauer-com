package com.jasonbierbrauer.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import com.jasonbierbrauer.model.Fragment;
import com.jasonbierbrauer.model.Image;
import com.jasonbierbrauer.model.ImageTypes;
import com.jasonbierbrauer.model.JumbotronContent;
import com.jasonbierbrauer.repository.FragmentRepository;
import com.jasonbierbrauer.repository.ImageRepository;
import com.jasonbierbrauer.repository.ImageTypeRepository;
import com.jasonbierbrauer.repository.JumboTronRepository;

public class PermissionController {
	
	// instance of Repositories
	@Autowired
	private JumboTronRepository jumbo;
	@Autowired
	private ImageRepository imgRepo;
	@Autowired
	private ImageTypeRepository imgTypeRepo;
	@Autowired
	private FragmentRepository frags;
	// autowire the repository to the controller
		
	/**
	 * This method checks the users authentication level
	 * @return true or false
	 */
	
	public boolean hasUserRole() {
		// this checks to see if a user has a user role
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasUserRole = authentication.getAuthorities().stream()
		         .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
		return hasUserRole;
	}
	
	/**
	 * This method returns the users authentication level
	 * @return true or false
	 * 
	 */
	
	public boolean hasAdminRole() {
		// this checks to see if a user has a admin role.
		Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
		boolean hasAdminRole = authentication2.getAuthorities().stream()
		          .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		return hasAdminRole;
	}

	
	public JumbotronContent findLastJumbo() {
		long size = jumbo.count();
		if (size == 0) {
			JumbotronContent jumbo = new JumbotronContent();
			jumbo.setHeadline("Blue's website and software design");
			String content = "Welcome to Blue's website and software design. This site is brand new "
					+ "and is the model for the site I hope to market. Everything is self contained "
					+ "and can be edited from the administration account. If your interesterd, send "
					+ "me an email on the contact page.";
			jumbo.setContent(content);
			jumbo.setUrl("https://s3-us-west-2.amazonaws.com/wandering-wonderland-images/system/banner01.jpg");
			return jumbo;
		} else {
			List<JumbotronContent> jumboMain = jumbo.findAll();
			return jumboMain.get(jumboMain.size()-1);
		}
	}
	
	// find type and adds to a list
	protected List<Image> findListofImages(String type) {
		List<Image> imageList = imgRepo.findAll();
		List<Image> realList = new ArrayList<Image>();
		for (int i = imageList.size()-1; i >= 0; i--) {
			if (imageList.get(i).getType().equalsIgnoreCase(type)) {
				realList.add(imageList.get(i));
			}
		}
		return realList;
	}
	
	protected void addTypesForMenu(Model model) {
		List<ImageTypes> imgTypes = imgTypeRepo.findAll();
		List<ImageTypes> realList = new ArrayList<ImageTypes>();
		for (int i = 0; i < imgTypes.size(); i++) {
			if (!imgTypes.get(i).getType().equals("system")) {
				realList.add(imgTypes.get(i));
			}
		}
		if (realList != null) {
			model.addAttribute("imgTypes", realList);
		}
	}
	
	protected void addFragments(Model model) {
		List<Fragment> allFrags = frags.findAll();
		if (allFrags.size() > 0) {
			Fragment frag = allFrags.get(allFrags.size()-1);
			model.addAttribute("frag", frag);
		}
	}
}
