package com.java.web.service;

import java.util.HashMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.web.dao.KaKaoUserDao;

@Service
public class KaKaoUserService {
	
	@Resource(name="sqlSession")
	SqlSession session;
	
	@Autowired
	KaKaoUserDao kud;
	
	public HashMap<String, Object> userInsert(HttpServletRequest req, HttpServletResponse res){
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		
		String id = req.getParameter("id");
		String name = req.getParameter("name");
		String passwd = req.getParameter("passwd");
		
		if(id == null || name == null || passwd == null) {
			resultmap.put("status",0);
		}else {
			param.put("id", id);
			param.put("name", name);
			param.put("passwd", passwd);
			
			int status = kud.userInsert(param);
			
			resultmap.put("status", status);
		}
		return resultmap;
	}
	
	public HashMap<String, Object> OverlapCheck(HttpServletRequest req, HttpServletResponse res){
		String id = req.getParameter("id");
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		HashMap<String, Object> map = kud.OverlapCheck(param);
		
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		
		if(map == null) {
			resultmap.put("status", 1);
		}else {
			resultmap.put("status",0);
		}
		
		return resultmap;
	}
	
	public HashMap<String, Object> userLogin(HttpServletRequest req, HttpServletResponse res, HttpSession sessionCheck){
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> kakaomap = new HashMap<String, Object>();
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		
		String kakaoId = req.getParameter("kakaoId");
		String thumbnail_image = req.getParameter("thumbnail_image");
		String profile_image = req.getParameter("profile_image");
		
		String id = req.getParameter("id");
		String passwd = req.getParameter("passwd");
		
		param.put("id", id);
		param.put("passwd", passwd);
		
		if(kakaoId !=null) {
			kakaomap.put("kakaoId", kakaoId);
			kakaomap.put("image", thumbnail_image);
			kakaomap.put("profileImg", profile_image);
			sessionCheck.setAttribute("kakaoUser", kakaomap);
		}else {
			
			HashMap<String, Object> map = kud.userLogin(param);
			
			HashMap<String, Object> usermap = new HashMap<String,Object>();
			
			usermap.put("userNo", map.get("userNo"));
			usermap.put("toDate", map.get("toDate"));
			usermap.put("name", map.get("name"));
			usermap.put("id", map.get("id"));
			
			if(map == null) {
				resultmap.put("status", 0);
			}else {
				resultmap.put("status", 1);
			}
			System.out.println(map);
			sessionCheck.setAttribute("user", usermap);
		}
		System.out.println(kakaomap);
		
		return resultmap;
	}
	
	public HashMap<String, Object> sessionCheck(HttpServletResponse res,HttpSession sessionCheck){
		
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		HashMap<String, Object> user = (HashMap<String, Object>) sessionCheck.getAttribute("user");
		HashMap<String, Object> kakaoUser = (HashMap<String, Object>) sessionCheck.getAttribute("kakaoUser");
		
		System.out.println("user : " + user);
		System.out.println("kakaoUser : " + kakaoUser);
		
		if(user != null || kakaoUser != null) {
			resultmap.put("status", 1);			
		}else {
			resultmap.put("status", 0);
		}
		resultmap.put("kakaoUser", kakaoUser);
		resultmap.put("user", user);
		
		return resultmap;
	}
	
	public HashMap<String, Object> logout(HttpSession sessionlog, HttpServletResponse res){
		
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		sessionlog.invalidate();
		resultmap.put("status", 1);
		
		return resultmap;
	}
	
	public HashMap<String, Object> removeUser(HttpServletRequest req, HttpServletResponse res,HttpSession sessionCheck){
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		String userNo = req.getParameter("userNo");
		param.put("userNo", userNo);
		
		int status = kud.removeUser(param);
		
		if(status == 1) {
			resultmap.put("status", status);
		}else {
			resultmap.put("status",0);
		}
		sessionCheck.invalidate();
		
		return resultmap;
	}
	
	public HashMap<String, Object> passModify(HttpServletRequest req, HttpServletResponse res){
		
		String userNo = req.getParameter("userNo");
		String passwd = req.getParameter("passwd");
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultmap = new HashMap<String, Object>();
		
		param.put("userNo", userNo);
		param.put("passwd", passwd);
		
		int status = kud.passModify(param);
		
		if(status == 1) {
			resultmap.put("status", 1);
		}else {
			resultmap.put("status",0);
		}
		
		return resultmap;
	}
}

