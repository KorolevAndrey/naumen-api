package io.github.hengyunabc.naumen.api.test;

import com.alibaba.fastjson.JSONArray;
import io.github.hengyunabc.naumen.api.DefaultNaumenApi;
import io.github.hengyunabc.naumen.api.Request;
import io.github.hengyunabc.naumen.api.RequestBuilder;
import io.github.hengyunabc.naumen.api.NaumenApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class DefaultNaumenApiTest1 {

	private DefaultNaumenApi naumenApi;

	private void writetofile(String fln, String msg) {
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("C:\\TEMP\\" + fln), "UTF-8"));
			out.write(msg);
		} catch (FileNotFoundException fnfe) {
			System.err.println("ERROR = " + fnfe.getMessage());
		} catch (Exception e) {
			System.err.println("ERROR = " + e.getMessage());
		} finally {
			try {
				out.close();
			} catch (IOException ioe) {
				System.err.println("ERROR = " + ioe.getMessage());
			}
		}
	}

	@Before
	public void before() {
		String url = "";

		url = "http://............./sd/services/rest";
		String AccessKey = "......................................";

		System.err.println("init ok");
		naumenApi = new DefaultNaumenApi(url);
		naumenApi.setAccessKey(AccessKey);
		naumenApi.init();
	}

	@After
	public void after() {
	    try {
            naumenApi.destroy();
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
	}


	@Test
	public void check_status() {
		Request request = RequestBuilder.newBuilder().method("check-status")
				.build();
		String response = naumenApi.callToStr(false, request);
		System.err.println("OUTPUT = " + response);
		Assert.assertTrue(response.equals("Operation completed successfully"));
	}


	@Test
	public void check_find() {
		Request request = RequestBuilder.newBuilder().method("find/team")
				.paramEntry("title","Первая линия")
				.paramEntry("metaClass","team$baseteam").build();

		JSONArray response = naumenApi.callToJSONArr(true, request);
		System.err.println("OUTPUT = " + response.toJSONString());
		writetofile("outfilename", response.toJSONString());

		Assert.assertTrue(response == null || response.size() == 0);
	}

	@Test
	public void check_sc() {
		Request request = RequestBuilder.newBuilder().method("find/serviceCall")
				.paramEntry("title","SD299")
				.paramEntry("metaClass","serviceCall$request").build();

		JSONArray response = naumenApi.callToJSONArr(true, request);
		System.err.println("OUTPUT = " + response.toJSONString());
		System.err.println("OUTPUT = " + ((JSONObject)response.get(0)).getString("title"));
		writetofile("outfilename1", response.toJSONString());

		Assert.assertTrue(response != null && response.size() == 1);
	}

	@Test
	public void check_emp() {
		Request request = RequestBuilder.newBuilder().method("find/employee")
				.paramEntry("login","..........")
				.build();

		JSONArray response = naumenApi.callToJSONArr(true, request);
		System.err.println("OUTPUT = " + response.toJSONString());
		System.err.println("OUTPUT = " + ((JSONObject)response.get(0)).getString("firstName"));
		writetofile("outfilename2", response.toJSONString());

		Assert.assertTrue(response != null && response.size() == 1);
	}



	@Test
	public void check_createm2mJSON() {

		String DEFAULT_CLIENT = "employee$1469"; 
		String DEFAULT_AGREEMENT = "agreement$8002"; //соглашение
		String DEFAULT_SERVICE = "slmService$7466"; //Услуга
		String DEFAULT_METACLASS = "serviceCall$service"; //Инцидент
		String SCCategory = "catrgories$7760"; //Категория 
		//String responsibleTeam = "team$8148"; //Отв команда
		String Descr = "<b>" +
				"Problem name: Оборудование недоступно<br>" +
				"Host: TEST<br>" +
				"IT-Service ID: TEST<br>" +
				"Severity: Not classified<br>" +
				"<br>" +
				"Original problem ID: 1913" +
				"</b>";

		Request request = RequestBuilder.newBuilder().method("create-m2m/serviceCall$serviceCall")
				.paramEntry("metaClass",DEFAULT_METACLASS)
				.paramEntry("client",DEFAULT_CLIENT)
				.paramEntry("agreement",DEFAULT_AGREEMENT)
				.paramEntry("service",DEFAULT_SERVICE)
				.paramEntry("SCCategory", SCCategory)
				//.paramEntry("responsibleTeam", responsibleTeam)
				.paramEntry("shortDescr", "Zabbix: Оборудование недоступно")
				.paramEntry("descriptionRTF", Descr)
				.build();
		JSONObject response = naumenApi.callToJSONObj(true, request);
		System.err.println("OUTPUT = " + response.toJSONString());
		System.err.println("OUTPUT = " + response.getString("title"));

		writetofile("outfilename3", response.toJSONString());
	}
}
