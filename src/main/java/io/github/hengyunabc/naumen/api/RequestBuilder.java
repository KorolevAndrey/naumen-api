package io.github.hengyunabc.naumen.api;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestBuilder {
    private static final AtomicInteger nextId = new AtomicInteger(1);

	private Request request = new Request();
	
	private RequestBuilder(){

	}
	
	static public RequestBuilder newBuilder(){
		return new RequestBuilder();
	}
	
	public Request build(){
		return request;
	}
	
	public RequestBuilder version(String version){
		return this;
	}
	
	public RequestBuilder paramEntry(String key, Object value){
		request.putParam(key, value);
		return this;
	}
	

	public RequestBuilder method(String method){
		request.setMethod(method);
		return this;
	}
	
	/**
	 * Do not necessary to call this method.If don not set id, RequestBuilder will auto generate.
	 * @param id
	 * @return
	 */
	public RequestBuilder id(Integer id){
		return this;
	}
}
