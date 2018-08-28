package com.java.web.service;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.codehaus.jettison.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.java.util.Utils;
import com.java.web.dao.KaKaoFileDao;
import com.java.web.mapper.KaKaoMapper;
import com.java.web.mapper.KaKaoSlangMapper;
import com.java.web.mapper.KaKaoTermMapper;
import com.java.web.mapper.KaKaoTimeSlotMapper;
import com.java.web.reducer.KaKaoReducer;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

@Service
public class KaKaoFileService {
	
	@Resource(name="hdConf")
	Configuration conf;
	
	@Autowired
	KaKaoFileDao kfd;
	
	public String makeProvider(HashMap<String, Object> map) {
		
		String result = "";
		
		System.out.println("map: " + map);
		List<HashMap<String, Object>> mapList = (List<HashMap<String, Object>>) map.get("result");
		
		JSONObject obj = new JSONObject();
		
		
		net.sf.json.JSONArray jArray = new net.sf.json.JSONArray();
		for(int i=0; i<mapList.size(); i++) {
			JSONObject sObject = new JSONObject();
			
			sObject.put("category", mapList.get(i).get("0"));
			sObject.put("column-1", mapList.get(i).get("1"));
			
			jArray.add(sObject);
		}
		obj.put("dataProvider", jArray);
		
		result = obj.get("dataProvider").toString();
		System.out.println(result);
		
		return result;
	}
	
	public String fileOutHadoop(HttpServletRequest req) throws Exception{
		
		HashMap<String, Object> param = new HashMap<String,Object>();
		
		String fileNo = req.getParameter("fileNo");
		param.put("fileNo", fileNo);
		
		HashMap<String, Object> URLmap =  kfd.fileAnalysis(param);
		
		FileSystem fileConf = FileSystem.get(conf);
		
		System.out.println("=====================data========================");
		String fileURL = (String) URLmap.get("fileURL");
		
		System.out.println("fileNo : " + fileNo);
		System.out.println("URLmap : " + URLmap);
		
		System.out.println("=====================data========================//");
		
		//hadoop 영역에 저장될 이름
		String outputURL =  "/input" + fileURL; //output할 url 
		
		
		HashMap<String, Object> resultMap = new HashMap<String,Object>();
		try {
			FSDataInputStream fileRead = fileConf.open(new Path(fileURL));
			
			byte[]  buffer = new byte[50000];
			
			int byteRead = 0;
			String value = "";
			StringBuffer sb = new StringBuffer();
			
			while((byteRead = fileRead.read(buffer)) != -1) { 
			   sb.append(new String(buffer, 0, byteRead));
			}
			value = sb.toString();
			String[] result = value.split("\n");
			String date = "";
			List<HashMap<String, String>> list= new ArrayList<HashMap<String, String>>();
			for(int row=3; row< result.length; row++) {
				if(result[row].indexOf("-----") != -1) {
					
					date = result[row].replace("-", "").substring(0, 9).trim().replace(" ", "");
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("0", date);
				
				String[] bb = result[row].replace("[", "").replace(" ", "").split("]");
				for(int b = 0; b < bb.length; b++) {
					map.put((b + 1) +"", bb[b].trim());
				}
				list.add(map);
			}
			resultMap.put("resultMap", list);
			
			if(!fileConf.exists(new Path("/input"))) { //존재여부 확인 
				fileConf.mkdirs(new Path("/input"));  //존재하지 않으면 생성 처리 
			}
			
			FSDataOutputStream fsos = fileConf.create(new Path(outputURL));
			
			for(int y = 0; y < list.size(); y++) {
				
				if(list.get(y).get("0") == null || list.get(y).get("1") == null || list.get(y).get("2") == "" || list.get(y).get("3") == null) {
					
					list.get(y).put("0", " ");
					list.get(y).put("1", " ");
					list.get(y).put("2", " ");
					list.get(y).put("3", " ");
				}
				HashMap<String, String> result2Map = list.get(y); 
				Set set = result2Map.keySet();
				Iterator iterator = set.iterator();
				String listString = "";
				
				while(iterator.hasNext()){
					String key = (String)iterator.next();
					listString += list.get(y).get(key) + ",";
				}
				listString += listString + "\r\n";
				byte[] finalData = listString.getBytes();
				fsos.write(finalData);
			}
			fsos.close();								
			System.out.println(resultMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return outputURL;
	}
	
	public HashMap<String, Object> fileAnalysis(String path) throws Exception{
		
		HashMap<String, Object> urlMap = new HashMap<String,Object>();
		
		System.out.println("=======================analysis start=======================");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss/");  
	    LocalDateTime now = LocalDateTime.now();  
		
		String fileURL = path;
		Job jobTalk = Job.getInstance(conf, "talk");
		Job jobSlang = Job.getInstance(conf, "slang");
		Job jobTerm = Job.getInstance(conf, "term");
		Job jobTimeSlot = Job.getInstance(conf, "timeslot");
		
		URI inputUri = URI.create(fileURL); //Mapper에서 읽어들일 주소 
		
		System.out.println(inputUri);
		/**=====================RESULT 주소 ========================**/
		URI outputTalk = URI.create("/Talk/" + dtf.format(now));//Reducer에서 최종적으로 결과물 저장할 주소
		String resultTalkURL = outputTalk + "part-r-00000"; //result 파일 URL
		
		URI outputSlang = URI.create("/Slang/" + dtf.format(now));
		String resultSlangURL = outputSlang + "part-r-00000"; 
		
		URI outputTerm = URI.create("/Term/" + dtf.format(now));
		String resultTermURL = outputTerm + "part-r-00000"; 
		
		URI outputTimeSlot = URI.create("/TimeSlot/" + dtf.format(now));
		String resultTimeSlotURL = outputTimeSlot + "part-r-00000"; 
		
		urlMap.put("resultTalkURL", resultTalkURL);
		urlMap.put("resultSlangURL", resultSlangURL);
		urlMap.put("resultTermURL", resultTermURL);
		urlMap.put("resultTimeSlotURL", resultTimeSlotURL);
		
		/**=====================RESULT 주소 END=====================**/
		
		
		
		/**=====================jobTalk Setting=====================**/
		FileInputFormat.addInputPath(jobTalk, new Path(inputUri));
		FileOutputFormat.setOutputPath(jobTalk, new Path(outputTalk));
		jobTalk.setInputFormatClass(TextInputFormat.class); //읽어들인것에 대한 데이터 타입 inputUri를가지고 FileInputFormat.addInputPath 한것에대한것 
		jobTalk.setOutputFormatClass(TextOutputFormat.class); //outputUri가지고 FileOutputFormat.setOutputPath 한것에 대한것 
		//작성할때 output
		jobTalk.setOutputKeyClass(Text.class); //reduce에서의 outputkey 타입 
		jobTalk.setOutputValueClass(IntWritable.class);//reduce에서의 outputvalue 타입
		jobTalk.setJarByClass(this.getClass()); //자기자신클래스 (Main)
		jobTalk.setMapperClass(KaKaoMapper.class);
		jobTalk.setReducerClass(KaKaoReducer.class);
		jobTalk.waitForCompletion(true); //job 실행
		/**=====================END================================**/
		
		/**=====================jobSlang Setting=====================**/
		FileInputFormat.addInputPath(jobSlang, new Path(inputUri));
		FileOutputFormat.setOutputPath(jobSlang, new Path(outputSlang));
		jobSlang.setInputFormatClass(TextInputFormat.class);  
		jobSlang.setOutputFormatClass(TextOutputFormat.class);  
		jobSlang.setOutputKeyClass(Text.class); 
		jobSlang.setOutputValueClass(IntWritable.class);
		jobSlang.setJarByClass(this.getClass()); 
		jobSlang.setMapperClass(KaKaoSlangMapper.class);
		jobSlang.setReducerClass(KaKaoReducer.class);
		jobSlang.waitForCompletion(true); 
		/**=====================END================================**/
		
		/**=====================jobTerm Setting=====================**/
		FileInputFormat.addInputPath(jobTerm, new Path(inputUri));
		FileOutputFormat.setOutputPath(jobTerm, new Path(outputTerm));
		jobTerm.setInputFormatClass(TextInputFormat.class);  
		jobTerm.setOutputFormatClass(TextOutputFormat.class);  
		jobTerm.setOutputKeyClass(Text.class); 
		jobTerm.setOutputValueClass(IntWritable.class);
		jobTerm.setJarByClass(this.getClass()); 
		jobTerm.setMapperClass(KaKaoTermMapper.class);
		jobTerm.setReducerClass(KaKaoReducer.class);
		jobTerm.waitForCompletion(true); 
		/**=====================END================================**/
		
		/**=====================jobTimeSlot Setting=====================**/
		FileInputFormat.addInputPath(jobTimeSlot, new Path(inputUri));
		FileOutputFormat.setOutputPath(jobTimeSlot, new Path(outputTimeSlot));
		jobTimeSlot.setInputFormatClass(TextInputFormat.class);  
		jobTimeSlot.setOutputFormatClass(TextOutputFormat.class);  
		jobTimeSlot.setOutputKeyClass(Text.class); 
		jobTimeSlot.setOutputValueClass(IntWritable.class);
		jobTimeSlot.setJarByClass(this.getClass()); 
		jobTimeSlot.setMapperClass(KaKaoTimeSlotMapper.class);
		jobTimeSlot.setReducerClass(KaKaoReducer.class);
		jobTimeSlot.waitForCompletion(true); 
		/**=====================END================================**/
		
		System.out.println("============================END==============================");
		
		return urlMap;
	}
	
	public HashMap<String, Object> fileUpload(MultipartFile[] files, HttpServletRequest req, HttpServletResponse res){
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy년MM월dd일_HH시mm분ss초");  
	    LocalDateTime now = LocalDateTime.now();  
		
	    HashMap<String, Object> param = new HashMap<String,Object>();
	    HashMap<String, Object> resultmap = new HashMap<String,Object>();
	    
	    List list = new ArrayList<>(); 
	    
		String userNo = req.getParameter("userNo");
		String fileTitle  = req.getParameter("fileTitle");
		System.out.println("fileTitle : " + fileTitle);
		
		param.put("userNo", userNo);
		
		for(int i =0; i<files.length; i++) {
			
			try {
				FileSystem file = FileSystem.get(conf);
				
				byte[] bytes = files[i].getBytes();
				
				String originalFileName = files[i].getOriginalFilename().replaceAll(".txt","");
				
				System.out.println("===============originalFileName==============" + originalFileName);
				
				String hadoopFileName = dtf.format(now);		//hadoop 영역에 저장될 이름 
				String fileURL = "/kakao/" + originalFileName +"_" + hadoopFileName;	// hadoop 영역 경로
				String Name = files[i].getOriginalFilename(); 	// filename.확장자명. 밑에서 가공할 것. 
				String fileName = FilenameUtils.removeExtension(Name); // DB에 들어갈 file이름
				Path path = new Path(fileURL);
				
				if(fileName.trim().length() > 0 && fileTitle.trim().length() > 0) {
					System.out.println("======================file insert=======================");
					
					/*******************************DB 에 파일정보 넣기 userNo와*************/
					param.put("fileName", fileName);
					param.put("fileURL", fileURL);
					param.put("fileTitle", fileTitle);
					System.out.println(param);
					
					int status = kfd.fileUpload(param);
					
					
					/************************************************************************/
					if(!file.exists(new Path("/kakao"))) { //존재여부 확인 
						file.mkdirs(new Path("/kakao"));  //존재하지 않으면 생성 처리 
					}
					FSDataOutputStream fsos = file.create(path); //출력 객체 생성
					fsos.write(bytes); 				 			//내용 작성 하기
					fsos.close();								//출력 객체 종료
					list.add(status);
				}else {
					
					list.add(0);
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		resultmap.put("status", list);
		System.out.println(resultmap);
		System.out.println("==========================END===========================");
		
		return resultmap;
	}
	
	
	public HashMap<String, Object> chartData(String part_r_URL) throws IOException {
		
		System.out.println("시작===============================================");
		
		URI uri = URI.create(part_r_URL);
        Path path = new Path(uri);
		FileSystem file = FileSystem.get(uri, conf);
		FSDataInputStream fsis = file.open(path);
		
		byte[] buffer = new byte[50000];
		int byteRead = 0;
		String result = "";
		
		while((byteRead = fsis.read(buffer)) > 0) { 
		   result = new String(buffer, 0, byteRead);
		}
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> resultMap = new HashMap<String,Object>();
		
		String[] rows = result.split("\n");
		for (int j = 0; j < rows.length; j++) {
			String row = rows[j];
			String[] cols = row.split("\t");
			HashMap<String, Object> map = new HashMap<String, Object>(); //map을 새롭게 객체를 만들어주므로써 for문안에서 새로 생성되는 데이터를 받는다. 

			 for(int c = 0; c < cols.length; c++) {
			    map.put(c + "", cols[c]); //int 더하기 string은 스트링으로 변한다. 
		     }
		list.add(map);
		}
		resultMap.put("result", list);
		System.out.println("끝===============================================");
		return resultMap;
	}
	
	
	public HashMap<String, Object> fileDelete(HttpServletRequest req, HttpServletResponse res){
		
		HashMap<String, Object> param = new HashMap<String,Object>();
		HashMap<String, Object> resultmap = new HashMap<String,Object>();
		String fileNo = req.getParameter("fileNo");
		param.put("fileNo", fileNo);
		
		int status = kfd.fileDelete(param);
		
		resultmap.put("status", status);
		
		return resultmap;
	}
	
	public HashMap<String, Object> fileRowCount(HttpServletRequest req,HttpServletResponse res){
		
		HashMap<String, Object> resultmap = new HashMap<String,Object>();
		HashMap<String, Object> param = new HashMap<String,Object>();
		
		String userNo = req.getParameter("userNo");
		param.put("userNo", userNo);
		
		HashMap<String, Object> map = kfd.fileRowCount(param); 
		
		resultmap.put("cnt", map);
		System.out.println(map);
		
		return resultmap;
	}
	public HashMap<String, Object> fileList(HttpServletRequest req,HttpServletResponse res){
		
		String userNo = req.getParameter("userNo");
		String start = req.getParameter("start");
		int intStart = Integer.parseInt(start);
		
		int DbToStart = intStart * 20;
		System.out.println(DbToStart);
		
		HashMap<String, Object> param = new HashMap<String,Object>();
		HashMap<String, Object> resultmap = new HashMap<String,Object>();
		
		
		param.put("userNo", userNo);
		param.put("start", DbToStart);
		
		List<HashMap<String, Object>> map = kfd.fileList(param);
				
		resultmap.put("map", map);
		
		return resultmap;
	}
}
