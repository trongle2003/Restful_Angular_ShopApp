package com.project.shopapp.configuration;

import com.project.shopapp.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableWebMvc
public class WebSecurityConfiguration {
    private final JwtTokenFilter jwtTokenFilter;

    //cấu hình SecurityFilterChain (authorization) "ai được quyền truy cập cái gì".
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)//Vô hiệu hóa CSRF (thường dùng trong REST API).
                .authorizeHttpRequests(requests -> requests
                        // PUBLIC GETs
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**", "/api/v1/categories/**", "/api/v1/categories/get-orders-by-keyword", "/api/v1/order_details/**", "/api/v1/order_details/order/**", "/api/v1/role").permitAll().requestMatchers(HttpMethod.GET, "/api/v1/orders/**").permitAll()

                        // PUBLIC POSTs
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/login", "/api/v1/users/register").permitAll()

                        // PUBLIC GET ảnh sản phẩm
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/images/**").permitAll()

                        // Cho tất cả các request còn lại: cần AUTH
                        .anyRequest().authenticated());

        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));//cho phép tất cả các đường dẫn
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); //cho phép method nào cũng được đi qua
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));// cho phép tất cả các header đi qua
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
