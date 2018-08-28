package com.java.web.mapper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.java.web.bean.KaKaoBean;

public class KaKaoTermMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		KaKaoBean bean = new KaKaoBean(value);
		
		Text outputKey = new Text();
		IntWritable outputValue = new IntWritable(1);

		//몇년도 몇월이 가장 활발하였는가.  
		outputKey.set(bean.getDate());
		if(bean.getContents().length() > 0 ) {  
			context.write(outputKey, outputValue);
		}
	}
}
