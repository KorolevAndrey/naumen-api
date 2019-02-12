# naumen-api
REST naumen api java (based on zabbix api https://github.com/hengyunabc/zabbix-api)

Use IntelliJ IDEA + maven to build jar library

##Example

	  
	url = "http://............./sd/services/rest";
	String AccessKey = "......................................";

	System.out.println("init ok");
	naumenApi = new DefaultNaumenApi(url);
	naumenApi.setAccessKey(AccessKey);
	naumenApi.init();
    
   	Request request = RequestBuilder.newBuilder().method("find/team")
				.paramEntry("title","Первая линия")
				.paramEntry("metaClass","team$baseteam")
        			.build();

	JSONArray response = naumenApi.callToJSONArr(true, request);
	System.out.println("OUTPUT = " + response.toJSONString());
    
  
##AccessKey

	//Can be generated inside NaumenSD in the console
	
	return api.auth.getAccessKey('username').setReusable().setDeadlineDays(9999).uuid
	
