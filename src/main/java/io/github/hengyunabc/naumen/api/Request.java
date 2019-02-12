package io.github.hengyunabc.naumen.api;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class Request {
	private Map<String, Object> params = new HashMap<>();
	private String method;
	private String accessKey;


	public void putParam(String key, Object value) {
		params.put(key, value);
	}

	public Object removeParam(String key) {
		return params.remove(key);
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this.params);
	}
}
