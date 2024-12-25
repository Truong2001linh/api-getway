package com.do_an.config;

//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
//@EnableWebFluxSecurity
//@RequiredArgsConstructor
//@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {"/auth/**"};

//    @Bean
//    SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {
//        http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange((exchanges) -> exchanges
//                        .pathMatchers(WHITE_LIST_URL).permitAll()
//                        .anyExchange().permitAll()
//                )
//                .httpBasic(withDefaults());
//        return http.build();
//    }
}
