package cc.cinea.huanyou.config;

import cc.cinea.huanyou.dto.ApiResp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author cinea
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig {
    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(config -> config
                        // .requestMatchers("/swagger-ui").authenticated()
                        .anyRequest().permitAll()) // 我们有部分接口是允许未登录用户查看的
                .formLogin(config -> config.loginProcessingUrl("/login")
                        .loginPage("/login.html")
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.setContentType("application/json");

                            var apiResponse = ApiResp.success(null);

                            var out = response.getOutputStream();
                            out.write(new ObjectMapper().writeValueAsBytes(apiResponse));
                            out.close();
                        })
                        .failureHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.setContentType("application/json");

                            var apiResponse = ApiResp.failure(4001, "登录失败");

                            var out = response.getOutputStream();
                            out.write(new ObjectMapper().writeValueAsBytes(apiResponse));
                            out.close();
                        })
                        .permitAll())
                .logout(config -> config.logoutUrl("/logout").logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(200);
                    response.setContentType("application/json; charset=utf-8");

                    var out = response.getOutputStream();
                    var resp = ApiResp.success(null);
                    new ObjectMapper().writeValue(out, resp);
                    out.close();
                }))
                .exceptionHandling(config -> config.authenticationEntryPoint((request, response, authException) -> {
                    var resp = ApiResp.failure(4001, "未登录/登录过期");

                    response.setStatus(401);
                    response.setContentType("application/json; charset=utf-8");
                    var out = response.getOutputStream();
                    new ObjectMapper().writeValue(out, resp);
                    out.close();
                }))
        ;
        return http.build();
    }
}
