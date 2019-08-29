package jamila.ipyana.basic;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserJpaRepository users;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				UserDetails details = users.findByUsername(username);
				if(details == null) {
					throw new UsernameNotFoundException(username);
				}
				return details;
			}
		};
	}

	@Override
	protected void configure(HttpSecurity http) {
		try {
			http
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/users").permitAll()
				.antMatchers(HttpMethod.POST, "/cards").permitAll()
				.antMatchers(HttpMethod.POST, "/users/forgot").permitAll()
				.antMatchers(HttpMethod.POST, "/users/admin/{userId}").hasAuthority("ADMIN")
				
				.antMatchers(HttpMethod.GET, "/{cardId}/image").permitAll()
				.and()
				.httpBasic() 
				.and()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	
//	@Override
//	protected AuthenticationManager authenticationManager()
//		throws AuthenticationException {
//		
//		return new AuthenticationManager() {
//			
//			@Override
//			public Authentication authenticate(Authentication auth) throws AuthenticationException {
//				String username = auth.getName();
//				String password = auth.getCredentials().toString();
//				
//				User user = users.findByUsername(username);
////				if(user != null && passwordEncoder().matches(password, user.getPassword())) {
//					return new UsernamePasswordAuthenticationToken(
//							username, password, Arrays.asList());
////				}
////				return null;
//			}
//		};
//	}
}
