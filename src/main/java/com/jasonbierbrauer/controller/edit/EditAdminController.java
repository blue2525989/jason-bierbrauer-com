package com.jasonbierbrauer.controller.edit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jasonbierbrauer.controller.PermissionController;

@Controller
public class EditAdminController extends PermissionController {
	
	private final String URL = "";
	
	//@RequestMapping("/change-account-details")
	public String addNewUser (@RequestParam String username
			, @RequestParam String password, Model model, @RequestParam String oldUsername) {

        //Get connection
    	Connection con;
    	Connection con2;
        Statement stmt = null;
                try{
                    String MySQL = URL ;
                    String SQL = "UPDATE users SET username='" + username + "', password='" + password + "' WHERE username='" + oldUsername + "'";
                    String SQL2 = "UPDATE user_roles SET username='" + username + "' WHERE username='" + oldUsername + "'";
                    //Opens connection to the new selection
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection(MySQL);
                    con2 = DriverManager.getConnection(MySQL);
                    //Creates database
                    stmt = con.createStatement();
                    stmt.execute(SQL);
                    stmt = con.createStatement();
                    stmt.execute(SQL2);
                    con.close();
                    con2.close();
                } catch (SQLException e) {
                	System.out.println(e.getMessage());
                } catch (ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                String saved = "Your account information has been saved. Logout and back in for the changes "
                		+ "to take affect.";
                model.addAttribute("saved", saved);
        		addFragments(model);
        		addTypesForMenu(model);
                return "admin/saved";
	}
	
	//@RequestMapping("/change-password")
	public String changePassword(@RequestParam String password, Model model, @RequestParam String oldUsername) {

        //Get connection
    	Connection con;
        Statement stmt = null;
                try{
                    String MySQL = URL ;
                    // delete old account only from users table
                    String SQLupdate = "UPDATE users SET password='"+ password +"' where username='" + oldUsername + "'";

                    //Opens connection to the new selection
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection(MySQL);
                    // deletes old user info then adds new
                    stmt = con.createStatement();
                    stmt.execute(SQLupdate);
                    con.close();
                } catch (SQLException e) {
                	System.out.println(e.getMessage());
                } catch (ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                String saved = "Your new password has been saved. Logout and back in for the changes "
                		+ "to take affect.";
                model.addAttribute("saved", saved);
        		addFragments(model);
        		addTypesForMenu(model);
                return "admin/saved";
	}

}
