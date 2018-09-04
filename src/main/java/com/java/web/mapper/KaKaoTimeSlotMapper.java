package com.java.web.mapper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.java.web.bean.KaKaoBean;

public class KaKaoTimeSlotMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		KaKaoBean bean = new KaKaoBean(value);
		
		Text outputKey = new Text();
		IntWritable outputValue = new IntWritable(1);
		
		String tt = bean.getTimeSlot();
		int tl = tt.length();
		
		String timeSlot = "";
		if(tl >= 6) {
			timeSlot = tt.substring(0, tt.lastIndexOf(":")) + "시";
		}
		
		
		//시간대별 분석
		outputKey.set(timeSlot);
		
		if(bean.getContents().length() > 10 ) {
			if(timeSlot.indexOf("�") == -1) {
				context.write(outputKey, outputValue);
			}
		}
	}
}
