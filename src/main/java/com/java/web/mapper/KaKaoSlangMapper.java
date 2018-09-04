package com.java.web.mapper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.java.web.bean.KaKaoBean;

public class KaKaoSlangMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		KaKaoBean bean = new KaKaoBean(value);
		
		Text outputKey = new Text();
		IntWritable outputValue = new IntWritable(1);
		String content = bean.getContents();
		
		outputKey.set(bean.getName());
		if(content.contains("병신") == true || content.contains("ㅄ") == true || content.contains("ㅂㅅ") == true || content.contains("시발") == true 
				 || content.contains("씨발") == true  || content.contains("ㅅㅂ") == true  || content.contains("씌발") == true  || content.contains("ㅈㄹ") == true
				 || content.contains("호구") == true || content.contains("지랄") == true || content.contains("미친놈") == true || content.contains("호로") == true
				 || content.contains("새끼") == true || content.contains("좆") == true || content.contains("졷") == true || content.contains("조까") == true
				 || content.contains("걸레") == true || content.contains("걸1레") == true || content.contains("아가리") == true || content.contains("아갈통") == true
				 || content.contains("악알이") == true || content.contains("닥쳐") == true || content.contains("창녀") == true || content.contains("창남") == true
				 || content.contains("장애인") == true || content.contains("좡애인") == true  || content.contains("보지") == true || content.contains("섹스") == true
				 || content.contains("섻스") == true || content.contains("애미") == true || content.contains("창년") == true || content.contains("존나") == true
				 || content.contains("졷나") == true || content.contains("줫나") == true || content.contains("졸라") == true || content.contains("존니") == true) {
			
			context.write(outputKey, outputValue);
		}
	}
}
