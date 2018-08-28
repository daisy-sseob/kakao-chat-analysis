package com.java.util;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class Utils {
	public static void JsonWriter(HttpServletResponse res, HashMap<String, Object> map) {
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=utf-8");
		JSONObject j = new JSONObject();
		j = JSONObject.fromObject(JSONSerializer.toJSON(map));
		try {
			res.getWriter().write(j.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
