package com.leski;

import com.leski.model.JwtAuthenticationFilter;
import com.leski.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class LibraryApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
		/*JwtService jwtService = new JwtService();
		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoiQURNSU4ifQ.SHbxm74Bt1_6AURNACP2QsFq47kQv7P-LZViLAXzg4c";
		Claims claims = jwtService.extractAllClaims(token);
		claims.forEach((string, o) -> System.out.println(string + " " + o));
		var username = jwtService.extractUserName(token);
		var role = jwtService.extractRoles(token);
		System.out.println(username);
		role.stream().forEach(System.out::println);*/
		//jwtService.isTokenValid2("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoidXNlciIsImF1ZCI6IlVTRVIifQ._Fy4LE0tuRlypyOH5qCF08iYn_eRtT_3qLbONHPUIyo");
		//String username = jwtService.extractUserName("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoidXNlciIsInJvbGUiOiJVU0VSIn0.tpTPwJiy0Wbgy8V_nRj13jTqIpOYzpUqTcviJF7kDug");
	}
}
