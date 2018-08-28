package com.java.web.bean;

import java.util.ArrayList;

import org.apache.hadoop.io.Text;

public class KaKaoBean {
	
	String date;
	String name;
	String contents;
	String timeSlot;
	
	public KaKaoBean(Text value) {
		
		String[] colm = value.toString().split(",");
		
		setDate(colm[0] == null ? "" : colm[0]);
		setName(colm[1] == null ? "" : colm[1]);
		setTimeSlot(colm[2] == null ? "" : colm[2]);
		setContents(colm[3] == null ? "" : colm[3]);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}


	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	
}
