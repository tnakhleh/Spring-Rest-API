package com.brightminds.assignment;

import static org.junit.Assert.*;

//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.brightminds.assignment.controllers.UserController;
import com.brightminds.assignment.security.config.CustomeUserAuthenticationEntryPoint;
import com.brightminds.assignment.security.config.ValidateAccessTokenFilter;
import com.brightminds.assignment.services.ISecurityService;
import com.brightminds.assignment.services.impl.CustomUserDetailsService;
import com.brightminds.assignment.services.impl.SecurityService;
import com.brightminds.assignment.util.LoggerUtil;
import com.brightminds.assignment.util.TokenUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private LoggerUtil logger;
	@MockBean
	private SecurityService securityService;
	@MockBean
	private CustomUserDetailsService customUserDetailsService;
	@MockBean
	private TokenUtil tokenUtil;
	@MockBean
	CustomeUserAuthenticationEntryPoint customeUserAuthenticationEntryPoint;
	
	private String adminCredentialJson = "{\"username\":\"admin\",\"password\":\"admin\"}";
	private String userCredentialJson = "{\"username\":\"user\",\"password\":\"user\"}";
	
	@Test
	public void testLoginApi() throws Exception {
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders
//											.post( "/api/user/login")
//											.accept(MediaType.APPLICATION_JSON)
//											.content(adminCredentialJson)
//											.contentType(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		logger.info(result.getResponse().getContentAsString());
//		String expected = "{id:Course1,name:Spring,description:10Steps}";
//		 DocumentContext context = JsonPath.parse(result.getResponse().getContentAsString());
//		 String tokenRet = context.read("$.accessToken",String.class);
//		 assertNotNull(tokenRet);
//		 logger.info("Token: "+tokenRet);
//		 assertTrue("Token length is greater than 150 char ", tokenRet.length() > 150);
	}
}
