package com.brightminds.assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.brightminds.assignment.dto.StatementDTO;
import com.brightminds.assignment.models.Statement;
import com.brightminds.assignment.repositories.dbaccess.DBAccessAdapter;
import com.brightminds.assignment.services.IStatementsService;
import com.brightminds.assignment.services.impl.SecurityService;
import com.brightminds.assignment.util.TokenUtil;

@SpringBootTest
class AssignmentApplicationTests {
	
	@Autowired
	private DBAccessAdapter dBAccessAdapter;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private IStatementsService statementsService;
	@Autowired
	private PasswordEncoder passwordEncoder;	
	@Autowired
	private TokenUtil tokenUtil;	
	
	@Test
	void contextLoads() {
	}
	
	
	@Test
	void dataAccessTest() throws Exception {
	//Test access to MS Access DB and return all records
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date fromDate = dateFormatter.parse("01/01/2000"); //Old Date to get all records
		Date toDate = new Date(); //Current date
		
		StatementDTO statementDTO = new StatementDTO(fromDate, toDate, null, null);
		List<Statement> result =dBAccessAdapter.getAccountStatements(1, statementDTO);
		
		//result must contain 31 records (for account id 1)
		assertEquals(result.size(), 31);
		
	}
	
	@Test
	void statementServiceTest() throws Exception {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date fromDate = dateFormatter.parse("01/01/2000"); //Old Date to get all records
		Date toDate = new Date(); //Current date
		
		StatementDTO statementDTO = new StatementDTO(fromDate, toDate, null, null);
		List<Statement> result = statementsService.getAccountStatements(2, statementDTO);
		
		//result must contain 19 records (for account id 1)
		assertEquals(result.size(), 19);
	}
	
	@Test
	void generateTokenTest() {
		UserDetails userDetails = new User("user", passwordEncoder.encode("user"), new ArrayList<GrantedAuthority>() {
			{
				add(new SimpleGrantedAuthority("ROLE_USER"));
			}
		});
		
		String token = tokenUtil.generateToken(userDetails);
		String username = tokenUtil.getUsernameFromToken(token);
		assertEquals(username, userDetails.getUsername());
		
	}
	
}
