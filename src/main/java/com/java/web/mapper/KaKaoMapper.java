package com.java.web.mapper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.java.web.bean.KaKaoBean;

public class KaKaoMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		KaKaoBean bean = new KaKaoBean(value);
		
		Text outputKey = new Text();
		IntWritable outputValue = new IntWritable(1);
		
//		누가 제일 말을 많이 했는가
		outputKey.set(bean.getName());
		
		if(bean.getContents().length() > 0 ) {  
			if(bean.getName().indexOf("�") == -1) {
				context.write(outputKey, outputValue);
			}
		}
	}
}
