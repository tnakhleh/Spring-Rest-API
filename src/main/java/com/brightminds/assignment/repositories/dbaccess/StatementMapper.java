package com.brightminds.assignment.repositories.dbaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.jdbc.core.RowMapper;

import com.brightminds.assignment.exceptions.DataSourceException;
import com.brightminds.assignment.models.Statement;

public class StatementMapper implements RowMapper<Statement> {

    @Override
    public Statement mapRow(ResultSet rs, int rowNum) throws SQLException {
    	Statement statement = new Statement();

    	try {
        	statement.setId(rs.getInt("ID"));
        	statement.setAccountId(rs.getInt("account_id"));
        	statement.setAccountNumber(rs.getString("account_number"));
        	statement.setAmount(rs.getDouble("amount"));

        	SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			statement.setDate(formatter.parse(rs.getString("datefield")));
			
		} catch (ParseException | SQLException e) {
            if (e instanceof ParseException) {
            	throw new DataSourceException("DB Date Parse Exception: "+e.getMessage());	
            }else if (e instanceof SQLException) {
            	throw new DataSourceException("DB Mapper Exception: "+e.getMessage());
            }
		}

        return statement;
    }
}