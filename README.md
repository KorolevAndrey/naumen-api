# naumen-api
REST naumen api java (based on zabbix api https://github.com/hengyunabc/zabbix-api)

##Example

	  
	url = "http://............./sd/services/rest";
	String AccessKey = "......................................";

	System.err.println("init ok");
	naumenApi = new DefaultNaumenApi(url);
	naumenApi.setAccessKey(AccessKey);
	naumenApi.init();
    
   	Request request = RequestBuilder.newBuilder().method("find/team")
				.paramEntry("title","Первая линия")
				.paramEntry("metaClass","team$baseteam")
        			.build();

	JSONArray response = naumenApi.callToJSONArr(true, request);
	System.err.println("OUTPUT = " + response.toJSONString());
    
  
