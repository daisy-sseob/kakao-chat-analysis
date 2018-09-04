package com.java.web.dao;

import java.util.HashMap;

import javax.annotation.Resource;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class KaKaoUserDao {

	@Resource(name="sqlSession")
	SqlSession session;
	
	public int userInsert(HashMap<String, Object> param) {
		
		return session.insert("user.userInsert",param);
	}
	
	public HashMap<String, Object> OverlapCheck(HashMap<String, Object> param){
		
		return session.selectOne("user.userIdCheck", param);
	}
	
	public HashMap<String, Object> userLogin(HashMap<String, Object> param){
		
		return session.selectOne("user.userLogin",param);
	}
	
	public int removeUser(HashMap<String, Object> param){
		
		return session.update("user.userDelete",param);
	}
	
	public int passModify(HashMap<String, Object> param) {
		
		return session.update("user.passwordUpdate",param);
	}
}
