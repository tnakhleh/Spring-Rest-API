package com.brightminds.assignment.security.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.brightminds.assignment.services.impl.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired 
	private ValidateAccessTokenFilter validateAccessTokenFilter;
	@Autowired
	CustomeUserAuthenticationEntryPoint customeUserAuthenticationEntryPoint;
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Define 2 Users
		List<User> usersList = new ArrayList<>();
		usersList.add(new User("admin", passwordEncoder().encode("admin"), new ArrayList<GrantedAuthority>() {
			{
				add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			}
		}));

		usersList.add(new User("user", passwordEncoder().encode("user"), new ArrayList<GrantedAuthority>() {
			{
				add(new SimpleGrantedAuthority("ROLE_USER"));
			}
		}));

		// Assign list of users to user details service
		customUserDetailsService.setUsersList(usersList);
		auth.eraseCredentials(false);
		auth.userDetailsService(customUserDetailsService);

		/*auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN")
				.and().withUser("user").password(passwordEncoder().encode("user")).roles("USER");*/
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Session Management 
		http.sessionManagement()
			.maximumSessions(1)
			.maxSessionsPreventsLogin(true);
		
		http.csrf().disable()
			.authorizeRequests()
				.antMatchers("/v1/account/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/user/logout*").hasAnyRole("USER", "ADMIN")
				.antMatchers("/user/login*").permitAll()
				.anyRequest().authenticated()
			//.and().httpBasic()
			.and().logout()
				  .logoutRequestMatcher(new AntPathRequestMatcher("api/user/logout"))
				  .invalidateHttpSession(true)
				  .clearAuthentication(true)
				  .deleteCookies("JSESSIONID")
				  .permitAll()
			.and()
				.exceptionHandling()
				.authenticationEntryPoint(customeUserAuthenticationEntryPoint);
			;
		 http.addFilterBefore(validateAccessTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

//	
//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return new CustomAuthenticationFailureHandler();
//    }
//    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    // Register HttpSessionEventPublisher
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }
    
//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }
    
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
//		 return NoOpPasswordEncoder.getInstance();
	}

}