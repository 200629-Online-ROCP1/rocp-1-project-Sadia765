package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.LoginDTO;
import com.revature.models.User;
import com.revature.models.UserDTO;
import com.revature.services.LoginService;

public class LoginController {

	private static final LoginService ls = new LoginService();
	private static final ObjectMapper om = new ObjectMapper();
	
	public String login(HttpServletRequest req, HttpServletResponse res) throws IOException{//used to be void...
		if(req.getMethod().equals("POST")) {
			System.out.println("In POST for login");
			BufferedReader reader = req.getReader();
			
			StringBuilder  s = new StringBuilder();
			
			String line = reader.readLine();
			
			while(line != null) {
				s.append(line);
				line = reader.readLine();
			}
			
			String body = new String(s);
			
			System.out.println(body);
			
			LoginDTO l = om.readValue(body, LoginDTO.class);
			//System.out.println("ls(login(l) returns " + ls.login(l));
			User currentUser = ls.login(l);
			
			//UserDTO userdto = new UserDTO();
			//userdto.userId = currentUser.getUserId();
			
			
			if(currentUser != null) {
				
				HttpSession ses = req.getSession();
				ses.setAttribute("user", l);
				ses.setAttribute("userId", currentUser.getUserId());
				ses.setAttribute("userRole", currentUser.getRole().getRole());

				ses.setAttribute("loggedin", true);
				res.setStatus(200);
				String currentUserom = om.writeValueAsString(currentUser);
				//User currentUser = om.readValue(ls.login(l), UserDTO.class);
				//res.getWriter().println(currentUser);//ls.login(l));
				res.getWriter().println(currentUserom);
				
			}

			else {
				//will only return a session if one is already associated with the request, will not create a new one. 				
				HttpSession ses = req.getSession(false);
				
				if(ses != null) {
					ses.invalidate();
				}
				res.setStatus(400);
				res.getWriter().println("Invalid Credentials");
			}
	
		}
		else if(req.getMethod().equals("GET") && (req.getParameterMap().containsKey("username") && (req.getParameterMap().containsKey("password")))) {
			LoginDTO l = new LoginDTO();
			l.username = req.getParameter("username");
			l.password = req.getParameter("password");
			User currentUser = ls.login(l);
			if(currentUser != null) {
				
				HttpSession ses = req.getSession();
				ses.setAttribute("user", l);
				ses.setAttribute("userId", currentUser.getUserId());
				ses.setAttribute("userRole", currentUser.getRole().getRole());

				ses.setAttribute("loggedin", true);
				res.setStatus(200);
				String currentUserom = om.writeValueAsString(currentUser);
				res.getWriter().println(currentUserom);//ls.login(l));
				
			}
			else {
				
				HttpSession ses = req.getSession(false);
				
				if(ses!=null) {
					ses.invalidate();
				}
				res.setStatus(400);
				res.getWriter().println("Invalid Credentials");
			}
			
		}
		return null;
	}
	
	public void logout(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		HttpSession ses = req.getSession(false);
		
		if(ses != null) {
			LoginDTO l = (LoginDTO) ses.getAttribute("user");
			ses.invalidate();
			res.setStatus(200);
			
			res.getWriter().println("You have successfully logged out " + l.username);
		}
		else {
			res.setStatus(400);
			res.getWriter().println("There was no user logged into the session");
		}
		
	}
	
	
	
}
