package com.brightminds.assignment.util;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/*
 * This utility class copied from  https://drive.google.com/file/d/1Ki782Sldlpq_1wo-dUP70qvo-acRC97h/view?usp=sharing
 * With changes from My side... Taha
 * 
 * */
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtil implements Serializable {
	@Autowired
	private LoggerUtil logger;
	
	private static final long serialVersionUID = 3136421288574341857L;

	@Value("${token.expire.period.min}")
	private long tokenExpirationPeriod;

    private String secret = "secret";
    
    private static  Map<String, Object> blockList  = new  HashMap<String, Object>();

    public static void addToBlockList(String userName, String token) {
    	blockList.put(userName, token);
    }
    
    public static void deleteFromBlockList(String userName) {
    	blockList.remove(userName);
    }
    
    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
    	if(token == null || token.isEmpty()) {
    		return true;
    	}
    	try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
    	}catch(ExpiredJwtException e) {
    		logger.error("Expired token exception", e);
    	}
    	return true;
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
    	logger.debug("--------------------------------------------------------");
    	logger.debug("------->" + this.tokenExpirationPeriod);
    	Date currentTime = new Date(System.currentTimeMillis() + this.tokenExpirationPeriod * 60 * 1000);
    	logger.debug(currentTime.toString());
        String tokenStr = Jwts.builder().setClaims(claims).setSubject(subject)
        					 	.setIssuedAt(new Date(System.currentTimeMillis()))
        					 	.setExpiration(currentTime)
        					 	.signWith(SignatureAlgorithm.HS512, secret)
        					 	.compact();
        
        addToBlockList(subject, tokenStr);
        return tokenStr;
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && blockList.containsKey(userDetails.getUsername()));
    }
    //Delete token from list
    public void deleteToken(String userName) {
		 deleteFromBlockList(userName);
    }
    
    public boolean isUserLoggedIn(String userName) {
    	return  blockList.containsKey(userName);
    }
    
    public String getUserSavedToken(String userName) {
    	return (String)blockList.get(userName);
    }
}