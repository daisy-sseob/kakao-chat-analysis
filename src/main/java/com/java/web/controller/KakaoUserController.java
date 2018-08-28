package com.java.web.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.util.Utils;
import com.sun.tools.internal.ws.processor.model.Model;

@Controller
public class KakaoUserController {
	
	@Resource(name="sqlSession")
	SqlSession session;
	
	@RequestMapping("/userInsert")
	public void userInsert(HttpServletRequest req, HttpServletResponse res) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		
		String id = req.getParameter("id");
		String name = req.getParameter("name");
		String passwd = req.getParameter("passwd");
		
		if(id == null || name == null || passwd == null) {
			resultmap.put("status",0);
		}else {
			map.put("id", id);
			map.put("name", name);
			map.put("passwd", passwd);
			
			int status = session.insert("user.userInsert",map);
			resultmap.put("status", status);
			
			Utils.JsonWriter(res, resultmap);
		}
	}
	@RequestMapping("/OverlapCheck")
	public void OverlapCheck(HttpServletRequest req, HttpServletResponse res) {
		
		String id = req.getParameter("id");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		HashMap<String, Object> map = session.selectOne("user.userIdCheck", param);
		
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		
		if(map == null) {
			resultmap.put("status", 1);
		}else {
			resultmap.put("status",0);
		}
		Utils.JsonWriter(res, resultmap);
		
	}
	@RequestMapping("/userLogin")
	public void userLogin(HttpServletRequest req, HttpServletResponse res, HttpSession sessionCheck) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		
		String id = req.getParameter("id");
		String passwd = req.getParameter("passwd");
		
		param.put("id", id);
		param.put("passwd", passwd);
		
		HashMap<String, Object> map = session.selectOne("user.userLogin",param);
		
		if(map == null) {
			resultmap.put("status", 0);
		}else {
			resultmap.put("status", 1);
		}
		System.out.println(map);
		sessionCheck.setAttribute("user", map);
		Utils.JsonWriter(res, resultmap);
	}
	
	
	@RequestMapping("/sessionCheck")
	public void sessionCheck( HttpServletResponse res,HttpSession sessionCheck) {

		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		HashMap<String, Object> user = (HashMap<String, Object>) sessionCheck.getAttribute("user");

		if(user == null) {
			resultmap.put("status", 0);
		}else {
			resultmap.put("status", 1);
		}
		resultmap.put("user", user);
		
		Utils.JsonWriter(res, resultmap);
	}
	@RequestMapping("/logout")
	public void logout(HttpSession sessionlog, HttpServletResponse res) {
		
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		sessionlog.invalidate();
		resultmap.put("status", 1);
		
		Utils.JsonWriter(res, resultmap);
	}
	@RequestMapping("/removeUser")
	public void removeUser(HttpServletRequest req, HttpServletResponse res,HttpSession sessionCheck) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		String userNo = req.getParameter("userNo");
		param.put("userNo", userNo);
		
		int status = session.update("user.userDelete",param);
		
		if(status == 1) {
			resultmap.put("status", status);
		}else {
			resultmap.put("status",0);
		}
		sessionCheck.invalidate();
		Utils.JsonWriter(res, resultmap);
	}
	
	@RequestMapping("/passModify")
	public void passModify(HttpServletRequest req, HttpServletResponse res) {
		
		String userNo = req.getParameter("userNo");
		String passwd = req.getParameter("passwd");
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		
		param.put("userNo", userNo);
		param.put("passwd", passwd);
		
		int status = session.update("user.passwordUpdate",param);
		
		if(status == 1) {
			resultmap.put("status", 1);
		}else {
			resultmap.put("status",0);
		}
		Utils.JsonWriter(res, resultmap);
	}
}








