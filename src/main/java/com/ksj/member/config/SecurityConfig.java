package com.ksj.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf((crsfConfig) -> crsfConfig.disable())
            .headers((headerConfig) -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
            .authorizeHttpRequests((authoziedRequest) -> authoziedRequest
                    .requestMatchers("/").permitAll()
//                    .requestMatchers("/login/**").permitAll()
                    .requestMatchers("/member/**").hasAnyRole("ADMIN", "MANAGER")
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                    .loginPage("/login/member_login.page")
                    .loginProcessingUrl("/login/member_login")
                    .usernameParameter("id")
                    .passwordParameter("passwd")
                    .defaultSuccessUrl("/member_main.page")
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/login/member_logout.do")
                    .logoutSuccessUrl("/login/member_login.page")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
