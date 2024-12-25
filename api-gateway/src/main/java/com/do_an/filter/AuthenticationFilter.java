package com.do_an.filter;

import com.do_an.service.JwtService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtService jwtService;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            ServerHttpRequest request=null;
            if (validator.isSecured.test(exchange.getRequest())){
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                   // throw new RuntimeException("missing authorization header");
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    boolean hasRole = checkUserRole(authHeader, config.getRequiredRole());
                    if (!hasRole) {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                   request = exchange.getRequest()
                            .mutate()
                            .header("loggedIdUser", jwtService.extractIdUser(authHeader).toString())
                            .build();

                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("un authorized access to application");
                }
            }
            assert request != null;
            return chain.filter(exchange.mutate().request(request).build());
        }));
    }

    private boolean checkUserRole(String authHeader, String requiredRole) {
        String hasRole= jwtService.extractRole(authHeader);
        String[] requireRoles= requiredRole.split(",");
        for (String role:requireRoles){
            if (hasRole !=null && hasRole.equals(role)){
                return true;
            }
        }
        return false;
    }

    @Getter
    @Setter
    public static class Config {
        private String requiredRole;
    }

}
