package com.java.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.java.util.Utils;
import com.java.web.service.KaKaoUserService;

@Controller
public class KakaoUserController {
	
	@Autowired 
	KaKaoUserService kus;
	
	@RequestMapping("/userInsert")
	public void userInsert(HttpServletRequest req, HttpServletResponse res) {
		
		Utils.JsonWriter(res, kus.userInsert(req, res));
	}
	@RequestMapping("/OverlapCheck")
	public void OverlapCheck(HttpServletRequest req, HttpServletResponse res) {
	
		Utils.JsonWriter(res, kus.OverlapCheck(req, res));
	}
	@RequestMapping("/userLogin")
	public void userLogin(HttpServletRequest req, HttpServletResponse res, HttpSession sessionCheck) {
		
		Utils.JsonWriter(res, kus.userLogin(req, res, sessionCheck));
	}
	@RequestMapping("/sessionCheck")
	public void sessionCheck( HttpServletResponse res,HttpSession sessionCheck) {
		
		Utils.JsonWriter(res, kus.sessionCheck(res, sessionCheck));
	}
	@RequestMapping("/logout")
	public void logout(HttpSession sessionlog, HttpServletResponse res) {
		
		Utils.JsonWriter(res, kus.logout(sessionlog, res));
	}
	@RequestMapping("/removeUser")
	public void removeUser(HttpServletRequest req, HttpServletResponse res,HttpSession sessionCheck) {
		
		Utils.JsonWriter(res, kus.removeUser(req, res, sessionCheck));
	}
	@RequestMapping("/passModify")
	public void passModify(HttpServletRequest req, HttpServletResponse res) {
		
		Utils.JsonWriter(res, kus.passModify(req, res));
	}
}








