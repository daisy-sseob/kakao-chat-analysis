package com.java.web.dao;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class KaKaoFileDao {
	
	@Resource(name="sqlSession")
	SqlSession session;
	
	public int fileUpload(HashMap<String, Object> param) {
		
		return session.insert("file.fileInsert",param);
	}
	public int fileDelete(HashMap<String, Object> param) {
		
		return 	session.update("file.fileDelete",param);
	}
	
	public HashMap<String, Object> fileRowCount(HashMap<String, Object> param){
		
		return session.selectOne("file.fileRowCount",param);
	}
	
	public List<HashMap<String, Object>> fileList(HashMap<String, Object> param){
		
		return session.selectList("file.fileList",param);
	}
	
	public HashMap<String, Object> fileAnalysis(HashMap<String, Object> param) {
		
		return session.selectOne("file.getFileURL",param);
	}
	
}
