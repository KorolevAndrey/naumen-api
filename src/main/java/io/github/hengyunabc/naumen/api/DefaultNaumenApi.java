package io.github.hengyunabc.naumen.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DefaultNaumenApi implements NaumenApi {
	private static final Logger logger = LoggerFactory.getLogger(DefaultNaumenApi.class);

	private CloseableHttpClient httpClient;

	private URI uri;

	private volatile String accessKey;

	public DefaultNaumenApi(String url) {
		try {
			uri = new URI(url.trim());
		} catch (URISyntaxException e) {
			throw new RuntimeException("url invalid", e);
		}
	}

	public DefaultNaumenApi(URI uri) {
		this.uri = uri;
	}

	public DefaultNaumenApi(String url, CloseableHttpClient httpClient) {
		this(url);
		this.httpClient = httpClient;
	}

	public DefaultNaumenApi(URI uri, CloseableHttpClient httpClient) {
		this(uri);
		this.httpClient = httpClient;
	}

	@Override
	public void init() {
		if (httpClient == null) {
			httpClient = HttpClients.custom().build();
		}
	}

	@Override
	public void destroy() {
		if (httpClient != null) {
			try {
				httpClient.close();
			} catch (Exception e) {
				logger.error("close httpclient error!", e);
				throw new RuntimeException("DefaultNaumenApi destroy call exception!", e);
			}
		}
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}


	public static String getResponseString(HttpResponse response) {
		if (response == null) {
			return null;
		}

		try {
			HttpEntity entity = response.getEntity();
			Header contentEncoding = entity.getContentEncoding();
			if (contentEncoding != null) {
				if (contentEncoding.getValue().toLowerCase(Locale.getDefault())
						.contains("gzip")) {
					GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(
							entity);
					return EntityUtils.toString(gzipEntity);
				}
			}
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getEntityString(HttpEntity entity) {
		if (entity == null) {
			return null;
		}

		try {
			Header contentEncoding = entity.getContentEncoding();
			if (contentEncoding != null) {
				if (contentEncoding.getValue().toLowerCase(Locale.getDefault())
						.contains("gzip")) {
					GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(
							entity);
					return EntityUtils.toString(gzipEntity);
				}
			}
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	public HttpEntity call(boolean usePOST, Request request) throws RuntimeException {
		if (request.getAccessKey() == null) {
			request.setAccessKey(this.accessKey);
		}

		HttpUriRequest httpRequest;
		String url = uri + "/" + request.getMethod() + "?accessKey=" + request.getAccessKey();
		String contType = "application/json;charset=utf-8";
		StringEntity se;

		try {

			se = new StringEntity(JSON.toJSONString(request.getParams()), ContentType.APPLICATION_JSON);

			if (usePOST) {
				httpRequest = org.apache.http.client.methods.RequestBuilder.post().setUri(url)
						.addHeader("Content-Type", contType)
						.setEntity(se)
						.build();
			} else {
				httpRequest = org.apache.http.client.methods.RequestBuilder.get().setUri(url)
						.addHeader("Content-Type", contType)
						.setEntity(se)
						.build();
			}
			//CloseableHttpClient client = HttpClients.custom()
			//		.disableContentCompression()
			//		.build();
			//CloseableHttpResponse response = client.execute(httpRequest);
			CloseableHttpResponse response = httpClient.execute(httpRequest);
			int sc = response.getStatusLine().getStatusCode();
			if(sc < 200 || sc >= 300) {
				throw new RuntimeException("DefaultNaumenApi call HttpStatus error, HttpStatus = " + Integer.toString(sc));
			}
			HttpEntity entity = response.getEntity();
			return entity;
		} catch (IOException e) {
			throw new RuntimeException("DefaultNaumenApi call exception!", e);
		}
	}

	public String callToStr(boolean usePOST, Request request) throws RuntimeException {
		HttpEntity entity;
		String res;
		entity = call(usePOST, request);
		res = getEntityString(entity);
		return res;
	}

	public JSONObject callToJSONObj(boolean usePOST, Request request)  throws RuntimeException {
		HttpEntity entity;
		String res;
		byte[] data;
		try {
			entity = call(usePOST, request);
			data = EntityUtils.toByteArray(entity);
		} catch (IOException ioe) {
			throw new RuntimeException("DefaultNaumenApi callToStr exception!", ioe);
		}

		return (JSONObject) JSON.parse(data);
	}

	public JSONArray callToJSONArr(boolean usePOST, Request request)  throws RuntimeException {
		HttpEntity entity;
		String res;
		byte[] data;
		try {
			entity = call(usePOST, request);
			data = EntityUtils.toByteArray(entity);
		} catch (IOException ioe) {
			throw new RuntimeException("DefaultNaumenApi callToStr exception!", ioe);
		}

		return (JSONArray) JSON.parse(data);
	}





}
