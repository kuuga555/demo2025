package com.example.demo;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.junit.Assert;


//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@WebAppConfiguration
//@SpringBootTest
class DemoApplicationTests {
	@Autowired
    private WebApplicationContext webApplicationContext;
	
//	@Autowired
	private MockMvc mvc;

	@Before(value = "")
	public void setup(){
		try {
			mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void contextLoads() {
		System.out.println("開始進行單元測試");
		
//		asse
	}
	
	private String nullTest;
    private String testString = "test";
    @Test
    public void useAssert(){
        Assert.assertEquals("test",testString);
        Assert.assertEquals("tes",testString);
        Assert.assertNull(nullTest);        
    }
    
	@Test
	void testGetUserInfoSuccess() throws Exception {
		System.out.println("DemoApplicationTests.testGetUserInfoSuccess()");
		
		String uri = "/hi";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		int status = result.getResponse().getStatus();
		System.out.println("status=[" + status + "]");
//		Assert.assertEquals("錯誤",200,status);
		
		/*
		MockHttpServletRequestBuilder requestBuilder = get("/hix").param("userId", "666");
		
		MvcResult result = mockMvc.perform(requestBuilder)
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();
		// 以下為轉換並且撰寫相關 Assertion
        Response<GetUserInfoResp> resp = this.parseResponseContent(result, GetUserInfoResp.class);
        GetUserInfoResp respondPayload = resp.getData();
        Assertions.assertThat(resp.getResult()).isEqualTo("0000");
        Assertions.assertThat(respondPayload.getCheckResults().getNickname()).isEqualTo("Vivi");
        Assertions.assertThat(respondPayload.getCheckResults().getGender()).isEqualTo("F");
        Assertions.assertThat(respondPayload.getCheckResults().getDescription()).isEqualTo("喜歡睡覺");
        */
    
	}

}
/*
@RequestMapping("/hi")
	public String hello(){
		return "Hello[" + df.format(new Date()) + "]";
	}
 * 
 * */
