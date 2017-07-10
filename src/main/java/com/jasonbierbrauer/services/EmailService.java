package com.jasonbierbrauer.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.jasonbierbrauer.controller.PermissionController;

@Controller
public class EmailService extends PermissionController {

    // Replace with a "To" address. If your account is still in the
    // sandbox, this address must be verified.
    static final String TO = "";
    
    @Autowired
    public BasicAWSCredentials creds;
    
    @SuppressWarnings("deprecation")
	@RequestMapping("/sendEmail")
    public String sendMail(@RequestParam String name, @RequestParam String surname, @RequestParam String email, 
    	    @RequestParam String phone,  @RequestParam String message,
    	    HttpSession session, Model model) {

    	if (phone == null || phone == "") {
    		phone = "no phone #";
    	}
    	SendEmailRequest request = new SendEmailRequest()
 		       .withSource(TO);
 		
 		List<String> toAddresses = new ArrayList<String>();
 		toAddresses.add(TO);
 		Destination dest = new Destination().withToAddresses(toAddresses);
 		
 		// set fromEmail as BCC
 		List<String> toBccAddresses = new ArrayList<String>();
 		toBccAddresses.add(TO);
 		dest.setBccAddresses(toBccAddresses);
 		request.setDestination(dest);
 		
 		Content subjContent = new Content().withData(phone +" "+ name +" "+ surname +" "+ email);
 		Message msg = new Message().withSubject(subjContent);
 		
 		
 		// Include a body in HTML formats.
 		Content htmlContent = new Content().withData("Phone number: " + phone 
 				+"<br/>"+ "<h2>First name:</h2> " + name 
 				+"<br/>"+ "<h2>Last name:</h2> " + surname 
 				+"<br/>"+ "<h2>Email address:</h2> " + email
 				+"<br/><br/><h2>Message,</h2><br/>"+ message);
 		Body body = new Body().withHtml(htmlContent);
 		msg.setBody(body);
 		
 		request.setMessage(msg);
 		
 		// Set AWS access credentials.
 		AmazonSimpleEmailServiceClient client =
 		       new AmazonSimpleEmailServiceClient(creds).withRegion(Regions.US_WEST_2);
 		
 		// Call Amazon SES to send the message. 
 		try {
 		   client.sendEmail(request);
 		
 		} catch (AmazonClientException e) {
 		   e.printStackTrace();
 		} catch (Exception e) {
 		   e.printStackTrace();
 		}
 		String saved = "Your email has been sent!";
 		model.addAttribute("saved", saved);
		addFragments(model);
 		return "admin/saved";
    }
}