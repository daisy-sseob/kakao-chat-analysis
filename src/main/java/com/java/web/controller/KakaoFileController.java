package com.java.web.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.java.util.Utils;
import com.java.web.service.KaKaoFileService;
import com.jcraft.jsch.Session;

@Controller
public class KakaoFileController {
	@Resource(name="hdConf")
	Configuration conf;
	
	@Autowired
	KaKaoFileService kfs;
	
	@RequestMapping("/fileOutHadoop")
	public void fileAnalysis(HttpServletResponse res,HttpServletRequest req) throws Exception {
		
		HashMap<String, Object> resultmap = new HashMap<String,Object>();
		
		String fileURL = kfs.fileOutHadoop(req);
		HashMap<String, Object> urlMap = kfs.fileAnalysis(fileURL);
		
		String resultTalkURL = (String) urlMap.get("resultTalkURL");
		String resultSlangURL = (String) urlMap.get("resultSlangURL");
		String resultTermURL = (String) urlMap.get("resultTermURL");
		String resultTimeSlotURL = (String) urlMap.get("resultTimeSlotURL");
		
		resultmap.put("dataTalk", kfs.makeProvider(kfs.chartData(resultTalkURL)));
		resultmap.put("dataSlang", kfs.makeProvider(kfs.chartData(resultSlangURL)));
		resultmap.put("dataTerm", kfs.makeProvider(kfs.chartData(resultTermURL)));
		resultmap.put("dataTimeSlot", kfs.makeProvider(kfs.chartData(resultTimeSlotURL)));
		
		Utils.JsonWriter(res, resultmap);
		
		
	}
	
	@RequestMapping("/fileUpload")
	public void fileUpload(@RequestParam("file") MultipartFile[] files, HttpServletRequest req, HttpServletResponse res) {
		
		Utils.JsonWriter(res, kfs.fileUpload(files, req, res));
	}
	
	@RequestMapping("/fileDelete")
	public void fileDelete(HttpServletRequest req, HttpServletResponse res) {
		
		Utils.JsonWriter(res, kfs.fileDelete(req, res));
	}
	
	@RequestMapping("/fileList")
	public void fileList(HttpServletRequest req,HttpServletResponse res) {
		
		Utils.JsonWriter(res, kfs.fileList(req, res));
	}
	
	@RequestMapping("/fileRowCount")
	public void fileRowCount( HttpServletRequest req, HttpServletResponse res) {
		
		Utils.JsonWriter(res, kfs.fileRowCount(req, res));
	}
}
