package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.config.jwt.JWTRequestFilter;
import com.example.MyBookShopApp.services.security.BookstoreUserDetailsService;
import com.example.MyBookShopApp.services.security.CustomOidcUser;
import com.example.MyBookShopApp.services.security.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableScheduling
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTRequestFilter filter;
    private final CustomOidcUserService customOidcUserService;
    private final OAuthSuccessHandler successHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;


    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(bookstoreUserDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().anonymous().disable()
                .authorizeRequests()
                .antMatchers("/my", "/profile", "/api/bookReview").hasRole("USER")
                .antMatchers("/**").hasAnyAuthority("ROLE_USER", "ROLE_ANONYMOUS")

                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().formLogin().loginPage("/signin")
                .failureUrl("/signin")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, e)
                        -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("token")
                .and().oauth2Login()
                .defaultSuccessUrl("/my").successHandler(successHandler)
                .userInfoEndpoint(userInfoEndpoint ->
                        userInfoEndpoint.oidcUserService(customOidcUserService)
                                .customUserType(CustomOidcUser.class, "google")
                )
                .and().oauth2Client();

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }


}


