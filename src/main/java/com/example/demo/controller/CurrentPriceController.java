package com.example.demo.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.CurrentPrice;
import com.example.demo.entity.CurrentPriceRowMapper;

@RestController
public class CurrentPriceController{
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	private static final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	//查詢	http://localhost:9100/hi 
	@RequestMapping("/hi")
	public String hello(){
		return "Hello[" + df.format(new Date()) + "]";
	}

	//查詢	 http://localhost:9100/getCurrentPrices
	@RequestMapping("/getCurrentPrices")
	public List<CurrentPrice> query(){
	    String sql = " SELECT id, short_name, long_name, exchange_rate, upd_date FROM current_price order by ID";
	    Map<String, Object> map = new HashMap<>();
	    CurrentPriceRowMapper rowMapper = new CurrentPriceRowMapper();
	    List<CurrentPrice> result = namedParameterJdbcTemplate.query(sql, map, rowMapper);
	    return result;
	}

	//新增	 http://localhost:9100/currentPrice	{"shortName":"YEN","longName":"日幣","exchangeRate":0.21}	
	@PostMapping("/currentPrice")
	public String create(@RequestBody CurrentPrice currentPrice){
		System.out.println("CurrentPriceController.create()");
//		System.out.println("currentPrice=[" + currentPrice + "]");
		
		//新增資料
		String sql = " INSERT INTO current_price(short_name, long_name, exchange_rate, upd_date) VALUES (:shortName, :longName, :exchangeRate, :updDate)";

		Map<String, Object> map = new HashMap<String, Object>();
		{
			map.put("shortName", currentPrice.getShortName());
			map.put("longName", currentPrice.getLongName());
			map.put("exchangeRate", currentPrice.getExchangeRate());
			map.put("updDate", new Date());
		}
//		System.out.println("currentPrice.getShortName()=[" + currentPrice.getShortName() + "]");
//		System.out.println("currentPrice.getLongName()=[" + currentPrice.getLongName() + "]");
//		System.out.println(currentPrice);

		int i = namedParameterJdbcTemplate.update(sql, map);
		
		return "新增" + i + "筆資料";
	}
	
	//修改	 http://localhost:9100/currentPrice/YEN	{"longName":"Japan","exchangeRate":0.21}	
	@RequestMapping("/currentPrice/{shortName}")
	public CurrentPrice update(@PathVariable String shortName, @RequestBody CurrentPrice currentPrice){
		System.out.println("CurrentPriceController.update()");
		System.out.println("shortName=[" + shortName + "]");

		//先更新
		String sql = " UPDATE current_price SET long_name=:longName, exchange_rate=:exchangeRate, upd_date=:updDate where short_name=:shortName";

		Map<String, Object> map = new HashMap<String, Object>();
		{
			map.put("shortName", shortName);
			map.put("longName", currentPrice.getLongName());
			map.put("exchangeRate", currentPrice.getExchangeRate());
			map.put("updDate", new Date());
		}
		int i = namedParameterJdbcTemplate.update(sql, map);
		System.out.println("更新" + i + "筆資料");

		//後查詢
	    String sql2 = " SELECT * FROM current_price where short_name=:shortName order by upd_date DESC";
	    Map<String, Object> map2 = new HashMap<>();
	    {
	    	map2.put("shortName", shortName);
	    }
	    CurrentPriceRowMapper rowMapper = new CurrentPriceRowMapper();
	    List<CurrentPrice> result = namedParameterJdbcTemplate.query(sql2, map2, rowMapper);
	    if(result.size()>0){ return result.get(0); }
	    return null;
	}

	//刪除	 http://localhost:9100/currentPrice/USD	
	@RequestMapping("/delCurrentPrice/{shortName}")
	public Integer delete(@PathVariable String shortName){
		System.out.println("shortName=[" + shortName + "]");
		
		String sql = " DELETE FROM current_price where short_name=:shortName";
		
		Map<String, Object> map = new HashMap<String, Object>();
		{
			map.put("shortName", shortName);
		}
		
		int i = namedParameterJdbcTemplate.update(sql, map);
		System.out.println("刪除" + i + "筆資料");
		return i;
	}
	
	//叫用其它api	http://localhost:9100/coindesk
	@RequestMapping("/coindesk")
    public String callExternalApi(){
        String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
//        url = "http://localhost:9100/hi";
        
        try{
        	ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        	return response.getBody();
        } catch(RestClientException e){
        	e.printStackTrace();
        	return "叫用失敗";
        }
    }

	//叫用其它api	http://localhost:9100/coindesk2
	@RequestMapping("/coindesk2")
    public List<CurrentPrice> callExternalApi2(){
		String apiStr = callExternalApi();
//		if(apiStr.equals("叫用失敗")){ return null; }
		
		//呈現資料
		String sql = " select a.id, a.short_name, long_name, a.exchange_rate, a.upd_date"
			+ " from current_price a"
			+ " join (SELECT short_name, max(upd_date) as upd_date FROM CURRENT_PRICE  group by short_name) b"
			+ " on a.short_name=b.short_name and a.upd_date=b.upd_date"
			+ " order by a.short_name";
	    Map<String, Object> map = new HashMap<>();
	    CurrentPriceRowMapper rowMapper = new CurrentPriceRowMapper();
	    List<CurrentPrice> result = namedParameterJdbcTemplate.query(sql, map, rowMapper);
	    return result;
    }

}

/*
 * 
    @Column String shortName; //幣別
    @Column String longName; //幣別中文名稱
    @Column double exchangeRate; //匯率
 
insert into current_price(short_name, long_name, exchange_rate, upd_date) values('YEN', '日幣', 0.21, CURRENT_TIMESTAMP());
insert into current_price(short_name, long_name, exchange_rate, upd_date) values('USD', '美金', 30.83, CURRENT_TIMESTAMP());
insert into current_price(short_name, long_name, exchange_rate, upd_date) values('EUR', '歐元', 35.39, CURRENT_TIMESTAMP());

SELECT * FROM CURRENT_PRICE ;
 */


