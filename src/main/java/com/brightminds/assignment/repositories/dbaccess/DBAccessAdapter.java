package com.brightminds.assignment.repositories.dbaccess;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.brightminds.assignment.dto.StatementDTO;
import com.brightminds.assignment.models.Statement;
import com.brightminds.assignment.util.LoggerUtil;

@Component
public class DBAccessAdapter {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	LoggerUtil logger;
	
	public List<Statement> getAccountStatements(Integer accountId, StatementDTO statementParams) {
		String query = generateStatementQuery(accountId, statementParams);
		List<Statement> result =  jdbcTemplate.query(query, new StatementMapper());
		return result;
	}
	
	private String generateStatementQuery(Integer accountId, StatementDTO statementParams){
		String query = " SELECT statement.[ID], "
						+ " account.[account_number], "
						+ " statement.[account_id], "
						+ " statement.[datefield], "
						+ " statement.[amount] "
						+ " FROM statement INNER JOIN account on account.[ID] = statement.[account_id] "
						+ " WHERE 1=1 ";

		//BUILD condition
		if(accountId != null) {
			query += " AND statement.[account_id] = "+ accountId.toString();
		}
		
		String strToDateSQLSegment = " dateserial(Val(Right(statement.[datefield],4)), Val(Mid(statement.[datefield],4,2)) ,Val(Left(statement.[datefield],2))) ";
		
		if(statementParams != null) {
			//Amount criteria
			if(statementParams.getAmountFrom() != null && statementParams.getAmountTo() != null) {
				query += " AND CDbl(Val(statement.[amount])) Between "+ statementParams.getAmountFrom().doubleValue() + " AND "+ statementParams.getAmountTo().doubleValue() ;
			}else if(statementParams.getAmountFrom() != null) {
				query += " AND CDbl(Val(statement.[amount])) >= "+ statementParams.getAmountFrom().doubleValue();
			}else if(statementParams.getAmountTo() != null) {
				query += " AND CDbl(Val(statement.[amount])) <= "+ statementParams.getAmountTo().doubleValue();
			}
			
			//Date Criteria
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
			
			if(statementParams.getDateFrom() != null && statementParams.getDateTo() != null) {
				query += String.format(" AND %s between Cdate('%s') and Cdate('%s')", strToDateSQLSegment, dateFormatter.format(statementParams.getDateFrom()), dateFormatter.format(statementParams.getDateTo()));
			}else if(statementParams.getDateFrom() != null) {
				query += String.format(" AND %s >= Cdate(\"%s\")", strToDateSQLSegment, dateFormatter.format(statementParams.getDateFrom()));
			}else if(statementParams.getDateTo() != null) {
				query += String.format(" AND %s >= CDate(\"%s\")", strToDateSQLSegment, dateFormatter.format(statementParams.getDateTo()));
			}else {
				//If there is no date parameter passed, only return three months back statements
				query += String.format(" AND %s >= DateAdd(\"m\", -3, Date())", strToDateSQLSegment);
			}
			
		}else{
			//If there is no parameter passed, only return three months back statements
			query += String.format(" AND %s >= DateAdd(\"m\", -3, Date())", strToDateSQLSegment);
		}
		
		logger.debug("Query Generated:");
		logger.info(query);
		
		return query;
	}
}
