package com.example.mybookshopapp.config.security;

import com.example.mybookshopapp.config.security.jwt.JWTRequestFilter;
import com.example.mybookshopapp.config.security.oauth2.CustomAuthorizationRequestResolver;
import com.example.mybookshopapp.config.security.oauth2.CustomOauthLoginSuccessHandler;
import com.example.mybookshopapp.config.security.oauth2.CustomOidcUserService;
import com.example.mybookshopapp.model.enums.StatusType;
import com.example.mybookshopapp.model.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTRequestFilter filter;
    private final CustomOidcUserService customOidcUserService;

    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final CustomOauthLoginSuccessHandler oauthLoginSuccessHandler;


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter() {
        return new SecurityContextHolderAwareRequestFilter();
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
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/**/img/save",
                        "/add/**",
                        "/delete/**",
                        "/users",
                        "/users/**",
                        "/api/users",
                        "/admin/**"

                )
                .hasRole("ADMIN")
                .antMatchers(
                        "/my",
                        "/profile",
                        "/postponed",
                        "/cart",
                        "/myarchive",
                        "/api/bookReview",
                        "/api/rateBook",
                        "/api/rateBookReview",
                        "/api/changeBookStatus",
                        "/payment",
                        "/api/payment",
                        "/recentlyViewed",
                        "/transactions",
                        "/api/payment/check",
                        "/download/**"
                )
                .hasRole("USER")

                .antMatchers("/**").permitAll()
                .and().formLogin().loginPage("/signin")
                .defaultSuccessUrl("/").failureForwardUrl("/signin")
                .and()
                .logout()
                .logoutUrl("/logout").logoutSuccessUrl("/")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("token", StatusType.CART.getCookieName(), StatusType.KEPT.getCookieName())
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .and().oauth2Login(oauth2Login -> {
                    oauth2Login.authorizationEndpoint(authorizationEndpoint ->
                            authorizationEndpoint
                                    .authorizationRequestResolver(
                                            new CustomAuthorizationRequestResolver(
                                                    this.clientRegistrationRepository))
                    );
                    oauth2Login.successHandler(oauthLoginSuccessHandler);
                    oauth2Login.userInfoEndpoint(userInfoEndpoint ->
                            userInfoEndpoint.oidcUserService(customOidcUserService)
                                    .customUserType(UserEntity.class, "google")
                    );
                }).oauth2Client();
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}


