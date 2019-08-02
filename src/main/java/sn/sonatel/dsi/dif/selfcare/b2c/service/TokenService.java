package sn.sonatel.dsi.dif.selfcare.b2c.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.TokenDTO;

@Service
@Transactional
public class TokenService {


    private final Logger log = LoggerFactory.getLogger(TokenService.class);

    private final RestTemplate restTemplate;

    private final ApplicationProperties applicationProperties;


    public TokenService(@Qualifier("vanillaRestTemplate") RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;

        this.applicationProperties = applicationProperties;
    }


    public TokenDTO getToken() {
        log.debug("get API MANAGEMENT Token");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", applicationProperties.getGrantType());
        map.add("client_id", applicationProperties.getClientId());
        map.add("client_secret", applicationProperties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<TokenDTO> response = restTemplate.postForEntity(applicationProperties.getKeyAccessTokenUri(), request, TokenDTO.class);

        return response.getBody();

    }

}
