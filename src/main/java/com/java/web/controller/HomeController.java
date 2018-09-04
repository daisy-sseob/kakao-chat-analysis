package com.java.web.controller;

import java.util.HashMap;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "main";
	}
	
	@RequestMapping("/join")
	public String join() {
		return "join";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	@RequestMapping("/myinfo")
	public String myinfo(HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
		HashMap<String, Object>	kakao = (HashMap<String, Object>) session.getAttribute("kakaoUser");
		
		if(user == null && kakao == null) {
			return "main";
		}else {
			return "myinfo";
		}
	}
	
	@RequestMapping("/analysis")
	public String analysis(HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
		HashMap<String, Object>	kakao = (HashMap<String, Object>) session.getAttribute("kakaoUser");
		
		System.out.println("2222222222: " + user);
		System.out.println("2222222222: " + kakao);
		if(user == null && kakao == null) {
			return "main";
		}else {
			return "analysis";
		}
	}
	
}
