package com.example.demo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.entity.CurrentPrice;
import com.example.demo.entity.CurrentPriceRowMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class Tutorial2ApplicationTests {
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	ObjectMapper objectMapper; //要將物件轉成JSON可以@Autowired ObjectMapper ,利用ObjectMapper來做轉換

	@Autowired
    private WebApplicationContext webApplicationContext;
	MockMvc mvc; //創建MockMvc類的物件

	CurrentPrice currentPrice;
	CurrentPrice currentPrice2;

	@Before
	public void setup(){
		//新增DB資料
		{
			String sql = " INSERT INTO current_price(short_name, long_name, exchange_rate, upd_date) VALUES (:shortName, :longName, :exchangeRate, :updDate)";
	
			Map<String, Object> map = new HashMap<String, Object>();
			{
				//19.79 新臺幣
				map.put("shortName", "AUD");
				map.put("longName", "澳幣");
				map.put("exchangeRate", 19.79);
				map.put("updDate", new Date());
			}
			namedParameterJdbcTemplate.update(sql, map);
			
			{
				//3.96 新臺幣
				map.put("shortName", "HKD");
				map.put("longName", "港幣");
				map.put("exchangeRate", 3.96);
				map.put("updDate", new Date());
			}
			namedParameterJdbcTemplate.update(sql, map);
		}
		
		//新增測試用
		currentPrice = new CurrentPrice();
		{
			currentPrice.setShortName("YEN");
			currentPrice.setLongName("日幣");
			currentPrice.setExchangeRate(0.21);
		}

		currentPrice2 = new CurrentPrice();
		{
			currentPrice2.setShortName("AUD");
			currentPrice2.setLongName("澳幣");
			currentPrice2.setExchangeRate(20.79);
		}


		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	
	@Test
	public void hello() throws Exception{
		String uri = "/hi";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		String content = result.getResponse().getContentAsString();
		System.out.println("content=[" + content + "]");

		int status = result.getResponse().getStatus();
		System.out.println("status=[" + status + "]");

		Assert.assertEquals("錯誤",200,status);
	}

	//1. 測試呼叫查詢幣別對應表資料API，並顯示其內容。
	@Test
	public void query() throws Exception{
		System.out.println("Tutorial2ApplicationTests.query() 查詢");

		String uri = "/getCurrentPrices";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		result.getResponse().setCharacterEncoding("UTF-8"); //要設置好，不然原本為 ISO-8859-1 出來的內容有可能會亂碼
		String content = result.getResponse().getContentAsString();
		System.out.println("content=[" + content + "]");
		
		int status = result.getResponse().getStatus();
		System.out.println("status=[" + status + "]");

		Assert.assertEquals("錯誤",200,status);
	}

	//2. 測試呼叫新增幣別對應表資料API。
	@Test
	public void create() throws Exception{
		System.out.println("Tutorial2ApplicationTests.create()");

		String uri = "/currentPrice";
		try{
			String json = objectMapper.writeValueAsString(currentPrice);
			System.out.println(json);
			MvcResult result
				= mvc.perform(MockMvcRequestBuilders.post(uri)//.content(json)
						.contentType(MediaType.APPLICATION_JSON) //要設定 contentType
						.content(json)
						.accept(MediaType.APPLICATION_JSON)).andReturn();
			result.getResponse().setCharacterEncoding("UTF-8"); //要設置好，不然原本為 ISO-8859-1 出來的內容有可能會亂碼
			String content = result.getResponse().getContentAsString();
			System.out.println("content=[" + content + "]");

			int status = result.getResponse().getStatus();
			System.out.println("status=[" + status + "]");

			Assert.assertEquals("錯誤",200,status);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//3. 測試呼叫更新幣別對應表資料API，並顯示其內容。
	@Test
	public void update() throws Exception{
		System.out.println("Tutorial2ApplicationTests.update()");

		String uri = "/currentPrice/AUD";
		try{
			String json = objectMapper.writeValueAsString(currentPrice2);
			System.out.println(json);
			MvcResult result
				= mvc.perform(MockMvcRequestBuilders.post(uri)//.content(json)
						.contentType(MediaType.APPLICATION_JSON) //要設定 contentType
						.content(json)
						.accept(MediaType.APPLICATION_JSON)).andReturn();
			result.getResponse().setCharacterEncoding("UTF-8"); //要設置好，不然原本為 ISO-8859-1 出來的內容有可能會亂碼
			String content = result.getResponse().getContentAsString();
			System.out.println("content=[" + content + "]");

			int status = result.getResponse().getStatus();
			System.out.println("status=[" + status + "]");

			Assert.assertEquals("錯誤",200,status);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//4. 測試呼叫刪除幣別對應表資料API。
	@Test
	public void delete() throws Exception{
		System.out.println("Tutorial2ApplicationTests.update()");

		String uri = "/delCurrentPrice/HKD";
		try{
			MvcResult result
				= mvc.perform(MockMvcRequestBuilders.post(uri)//.content(json)
						.contentType(MediaType.APPLICATION_JSON) //要設定 contentType
						.accept(MediaType.APPLICATION_JSON)).andReturn();
			result.getResponse().setCharacterEncoding("UTF-8"); //要設置好，不然原本為 ISO-8859-1 出來的內容有可能會亂碼
			String content = result.getResponse().getContentAsString();
			System.out.println("content=[" + content + "]");

			int status = result.getResponse().getStatus();
			System.out.println("status=[" + status + "]");

			Assert.assertEquals("錯誤",200,status);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}