package com.weather.app.weather;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Controller
@Slf4j
@RequestMapping("/weather/*")
public class WeatherController {
	@Value("${weather.encoding.key}")
	private String servicekey;
	

	
	//WebClient 사용
	@GetMapping("wc")
	public String weatherApiWc(Model model) throws Exception {
		//현재 시간 구하기 
		Date today = new Date();
		SimpleDateFormat dataformat = new SimpleDateFormat("yyyyMMdd-HH,mm");
		String date = dataformat.format(today);
		String [] ar = date.split("-");
		String baseDate = ar[0];
		String baseTime = ar[1];
		String[] br = baseTime.split(",");
		int num = Integer.parseInt(br[0]);  //시간
		int sum = Integer.parseInt(br[1]);	//분
		if(sum < 46)num--;					//api 45분부터 응답제공 45분 미만일시 이전 시간 데이터 받아오기
		baseTime = ""+num+"00";
		
		WebClient webClient =  WebClient.builder()  //기본 설정
				.baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0")
				.build();
		Mono<String> response  = webClient.get()
				.uri(UriBuilder ->UriBuilder
				.path("/getUltraSrtNcst")			//추가 url
				.queryParam("serviceKey",servicekey)//파라미터 설정
				.queryParam("numOfRows", 10)
				.queryParam("pageNo", "1")
				.queryParam("dataType", "JSON")
				.queryParam("base_date", baseDate)
				.queryParam("base_time", "1300")    //fix 
				.queryParam("nx", "58")
				.queryParam("ny", "125")
				.build())
				.header("Content-type", "application/json")//헤더값 설정
				.header("charset","UTF-8")
				.retrieve()
				.bodyToMono(String.class);			//리턴 타입
		String result =response.block();
		 JSONParser parser = new JSONParser();
	        JSONObject obj = (JSONObject)parser.parse(result);
	        JSONObject responses = (JSONObject)obj.get("response");
	        JSONObject body = (JSONObject)responses.get("body");
	        JSONObject items = (JSONObject) body.get("items");
	        JSONArray list = (JSONArray)items.get("item");
	        Map sm = (JSONObject)list.get(0);
	        Map tm =  (JSONObject)list.get(3);
	        String region = "현재 날씨 금천구";
	        String status = (String) sm.get("obsrValue");
	        String temp = (String) tm.get("obsrValue")+"°";
	        model.addAttribute("region",region);
	        model.addAttribute("status",status);
	        model.addAttribute("temp",temp);
		return "index";
	}
	
	
	
	
	//HttpURLConnection 사용
	@GetMapping("huc")
	public String weatherApiHuc(Model model) throws Exception {
		//현재 시간 구하기 
		Date today = new Date();
		SimpleDateFormat dataformat = new SimpleDateFormat("yyyyMMdd-HH,mm");
		String date = dataformat.format(today);
		String [] ar = date.split("-");
		String baseDate = ar[0];
		String baseTime = ar[1];
		String[] br = baseTime.split(",");
		int num = Integer.parseInt(br[0]);  //시간
		int sum = Integer.parseInt(br[1]);	//분
		if(sum < 46)num--;					//api 45분부터 응답제공 45분 미만일시 이전 시간 데이터 받아오기
		baseTime = ""+num+"00";
		//URL 작성
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
	    urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + servicekey); /*Service Key*/
	    urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
	    urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	    urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
	    urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /*‘21년 6월 28일 발표*/
	    urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /*06시 발표(정시단위) */
	    urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("58", "UTF-8")); /*예보지점의 X 좌표값*/
	    urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("125", "UTF-8")); /*예보지점의 Y 좌표값*/
	    URL url = new URL(urlBuilder.toString());
	    //REST API 연결 HttpURLConnection 사용
	    HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-type", "application/json");
	    System.out.println("Response code: " + con.getResponseCode()); //응답코드
	    BufferedReader rd; 				//데이터 읽기
	    //응답 코드 확인
	    if(con.getResponseCode() >= 200 && con.getResponseCode() <= 300) {
	            rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        } else {
	            rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	        }
	    StringBuilder sb = new StringBuilder();
	    String line;
	    	while ((line = rd.readLine()) != null) {	//null 이 아닐떄까지 읽기
	            sb.append(line);
	        }
	    rd.close();					//연결 종료
	    con.disconnect();			//연결 종료		자원 닫아주기
	        String result = sb.toString();				//JSON으로 응답받은 데이터 파싱
	        JSONParser parser = new JSONParser();
	        JSONObject obj = (JSONObject)parser.parse(result);
	        JSONObject response = (JSONObject)obj.get("response");
	        JSONObject body = (JSONObject)response.get("body");
	        JSONObject items = (JSONObject) body.get("items");
	        JSONArray list = (JSONArray)items.get("item");
	        Map sm = (JSONObject)list.get(0);
	        Map tm =  (JSONObject)list.get(3);
	        String region = "현재 날씨 금천구";
	        String status = (String) sm.get("obsrValue");
	        String temp = (String) tm.get("obsrValue")+"°";
	        model.addAttribute("region",region);
	        model.addAttribute("status",status);
	        model.addAttribute("temp",temp);
	        return "index";
	}
	
	
}
