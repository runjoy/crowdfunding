package com.itransition.anton.config;

import com.itransition.anton.OAuth2Service.FacebookUserInfoTokenServices;
import com.itransition.anton.OAuth2Service.GmailUserInfoTokenServices;
import com.itransition.anton.repo.UserRepo;
import com.itransition.anton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qanto on 02.09.2019.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Client
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/", "/error", "/registration", "/activate/*", "/bootstrap-editable/**", "/resources/**", "/static/**", "/js/**", "/css/**", "/img/**", "/company/**", "/profile/*", "/login/**", "/vote/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
                    .and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
    }

    private Filter ssoFilter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();

        OAuth2ClientAuthenticationProcessingFilter facebookFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/facebook");
        OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(facebook(), oauth2ClientContext);
        facebookFilter.setRestTemplate(facebookTemplate);
        FacebookUserInfoTokenServices facebookTokenServices = new FacebookUserInfoTokenServices(facebookResource().getUserInfoUri(), facebook().getClientId());
        //Переопределить UserInfoTokenServices на FacebookUserInfoTokenServices
        facebookTokenServices.setRestTemplate(facebookTemplate);
        facebookFilter.setTokenServices(facebookTokenServices);
        facebookTokenServices.setUserRepo(userRepo);
        filters.add(facebookFilter);

        OAuth2ClientAuthenticationProcessingFilter gmailFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/gmail");
        OAuth2RestTemplate gmailTemplate = new OAuth2RestTemplate(gmail(), oauth2ClientContext);
        gmailFilter.setRestTemplate(gmailTemplate);
        GmailUserInfoTokenServices gmailTokenServices = new GmailUserInfoTokenServices(gmailResource().getUserInfoUri(), gmail().getClientId());
        //UserInfoTokenServices googleTokenServices = new UserInfoTokenServices(googleResource().getUserInfoUri(), google().getClientId());
        gmailTokenServices.setRestTemplate(gmailTemplate);
        gmailFilter.setTokenServices(gmailTokenServices);
        gmailTokenServices.setUserRepo(userRepo);
        //googleTokenServices.setPasswordEncoder(passwordEncoder);
        //Переопределить tokenServices на FacebookUserInfoTokenServices
        filters.add(gmailFilter);

        filter.setFilters(filters);
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { //аутентификация
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    @ConfigurationProperties("facebook.client")
    public AuthorizationCodeResourceDetails facebook() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("facebook.resource")
    public ResourceServerProperties facebookResource() {
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("gmail.client")
    public AuthorizationCodeResourceDetails gmail() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("gmail.resource")
    public ResourceServerProperties gmailResource() {
        return new ResourceServerProperties();
    }

    //Используется?
    @Bean
    public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }
}
