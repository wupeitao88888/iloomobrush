package com.iloomo.net;




import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import android.net.ParseException;

/**
 * 消息响应基类
 * 
 * @author liyi
 * 
 */
public abstract class ResponseMessage implements MessageIF {
	private JSONParser parser;

	private long errorCode;
	private String data;

	public ResponseMessage() {
		setData("");
		setErrorCode(0);
		parser = new JSONParser();
	}

	private void parseHeader(JSONObject obj) throws Exception {
		if (obj.containsKey("errorCode")) {
			setErrorCode(Long.parseLong(obj.get("errorCode").toString()));
		}

		if (obj.containsKey("data")) {
			setData(obj.get("data").toString());
		}
	}

	public void parseResponse(String jsonStr) throws ParseException,
			ClassCastException {
		if (jsonStr == null || jsonStr.equals("")) {
			return;
		}
		try {
			JSONObject obj = (JSONObject) parser.parse(jsonStr);
			parseHeader(obj);
			// todo 状态判断
			if(errorCode == 0){
				parseBody(obj);
			}

		} catch (ParseException pe) {
			throw pe;
		} catch (ClassCastException ce) {
			throw ce;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 解析消息体
	 * 
	 * @param obj
	 */
	protected abstract void parseBody(JSONObject obj) throws ParseException;

	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}

	public long getErrorCode() {
		return errorCode;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

}
