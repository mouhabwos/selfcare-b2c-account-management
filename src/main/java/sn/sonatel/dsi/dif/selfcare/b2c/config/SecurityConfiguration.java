package sn.sonatel.dsi.dif.selfcare.b2c.config;

import java.util.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.client.RestTemplate;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import sn.sonatel.dsi.dif.selfcare.b2c.security.*;
import sn.sonatel.dsi.dif.selfcare.b2c.security.SecurityUtils;
import sn.sonatel.dsi.dif.selfcare.b2c.security.oauth2.AudienceValidator;
import sn.sonatel.dsi.dif.selfcare.b2c.security.oauth2.JwtGrantedAuthorityConverter;
import sn.sonatel.dsi.dif.selfcare.b2c.web.interceptor.RestTemplateAddTokenInterceptor;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.JHipsterProperties;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JHipsterProperties jHipsterProperties;

    @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}")
    private String issuerUri;

    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties, SecurityProblemSupport problemSupport) {
        this.problemSupport = problemSupport;
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
            .disable()
            .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
        .and()
            .headers()
            .contentSecurityPolicy(jHipsterProperties.getSecurity().getContentSecurityPolicy())
        .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
        .and()
            .permissionsPolicy().policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
        .and()
            .frameOptions()
            .deny()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/api/account-management/v1/lite/reset-password").permitAll()
            .antMatchers("/api/account-management/v2/check_number/**").permitAll()
            .antMatchers("/api/auth/login-succeeded/**").permitAll()
            .antMatchers("/api/auth/login-failed/**").permitAll()
            .antMatchers("/api/account-management/register").permitAll()
            .antMatchers("/api/account-management/v2/register").permitAll()
            .antMatchers("/api/account-management/v3/register").permitAll()
            .antMatchers("/api/account-management/check_number").permitAll()
            .antMatchers("/api/account-management/v2/check_number").permitAll()
            .antMatchers("/api/account-management/v3/check_number").permitAll()
            .antMatchers("/api/account-management/email-already-exist").permitAll()
            .antMatchers("/api/account-management/account/**").permitAll()
            .antMatchers("/api/account-management/view-tutorial/**").permitAll()
            .antMatchers("/api/abonne/information-abonne/**").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/auth-info").permitAll()
            .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/health/**").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(authenticationConverter())
                .and()
            .and()
                .oauth2Client();
        // @formatter:on
    }

    Converter<Jwt, AbstractAuthenticationToken> authenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthorityConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(jHipsterProperties.getSecurity().getOauth2().getAudience());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    @Bean(value = {"customLoadBalancedRestTemplate","loadBalancedRestTemplate"})
    @Profile("!"+JHipsterConstants.SPRING_PROFILE_K8S)
    @Qualifier("loadBalancedRestTemplate")
    public RestTemplate loadBalancedRestTemplate(RestTemplateCustomizer customizer) {
        RestTemplate restTemplate =  getAuthenticatedRestTemplate();
        customizer.customize(restTemplate);
        return restTemplate;
    }

    @Bean
    @Profile(JHipsterConstants.SPRING_PROFILE_K8S)
    @Qualifier("loadBalancedRestTemplate")
    public RestTemplate loadBalancedRestTemplateK8s() {
        return getAuthenticatedRestTemplate();
    }

    private RestTemplate getAuthenticatedRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        restTemplate.setRequestFactory(factory);
        restTemplate.setInterceptors( Collections.singletonList(new RestTemplateAddTokenInterceptor()) );
        return restTemplate;
    }

    @Bean
    @Qualifier("vanillaRestTemplate")
    public RestTemplate vanillaRestTemplate() {
        return new RestTemplate();
    }
}
