package com.nam.chat.config;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nam.chat.security.AuthoritiesConstants;

import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    public static final String IP_ADDRESS = "IP_ADDRESS";

    private final JHipsterProperties jHipsterProperties;

    public WebSocketConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                // .addInterceptors(httpSessionHandshakeInterceptor())
                // .setHandshakeHandler(defaultHandshakeHandler())
                .setAllowedOrigins("http://localhost:4200", "http://localhost:8080", "http://localhost:8100")
                .withSockJS();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MediaType.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);

        return false;
    }

    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(
                    ServerHttpRequest request,
                    ServerHttpResponse response,
                    WebSocketHandler wsHandler,
                    Map<String, Object> attributes) throws Exception {
                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                    String authHeader = servletRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7); // Extract the token
                        String username = extractUsernameFromToken(token); // Implement token decoding to extract the
                                                                           // username

                        if (username != null) {
                            attributes.put("username", username); // Store in session attributes
                        }
                    }
                }
                return true;
            }

            @Override
            public void afterHandshake(
                    ServerHttpRequest request,
                    ServerHttpResponse response,
                    WebSocketHandler wsHandler,
                    Exception exception) {
            }
        };
    }

    private DefaultHandshakeHandler defaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                    Map<String, Object> attributes) {
                Principal principal = request.getPrincipal();
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (principal == null) {
                    if (authentication != null) {
                        return authentication;
                    }
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));
                    principal = new AnonymousAuthenticationToken("WebsocketConfiguration",
                            "anonymous", authorities);
                }
                return principal;
            }
        };
    }

    private String extractUsernameFromToken(String token) {
        // Implement this method to decode the JWT token and extract the username
        // Example: Use a JWT library like JJWT to extract the subject (username)
        try {
            // Sample code for JWT decoding (you can replace this with your actual logic)
            Claims claims = Jwts.parser()
                    .setSigningKey("yourSecretKey")
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject(); // Assuming the username is stored as the subject
        } catch (Exception e) {
            return null; // Handle invalid token
        }
    }
}
