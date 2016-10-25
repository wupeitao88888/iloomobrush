package com.iloomo.bean;

import java.io.Serializable;

public class BaseModel implements Serializable {
	private String message;
	private String code;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
