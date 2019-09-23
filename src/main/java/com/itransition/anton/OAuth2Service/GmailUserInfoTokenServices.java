package com.itransition.anton.OAuth2Service;

import com.itransition.anton.domain.Role;
import com.itransition.anton.domain.User;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * Created by qanto on 21.09.2019.
 */
public class GmailUserInfoTokenServices extends AbstractUserInfoTokenServices {

    public GmailUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        super(userInfoEndpointUrl, clientId);
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(super.getUserInfoEndpointUrl(), accessToken);
        System.out.println(map);
        if(map.containsKey("sub"))
        {
            User user = userRepo.findByGoogleId((String) map.get("sub"));

            if(user == null)
            {
                user = new User();
                user.setEmail((String) map.get("email"));
                user.setRoles(Collections.singleton(Role.USER));
                user.setBlock(false);
                user.setLastname((String) map.get("family_name"));
                user.setFirstname((String) map.get("given_name"));
                user.setFilename((String) map.get("picture"));
                user.setGoogleId((String) map.get("sub"));
                user.setRegistrationDate(new Date());
                userRepo.save(user);
            }
            return extractAuthentication(user);
        }

        if (map.containsKey("error"))
        {
            throw new InvalidTokenException(accessToken);
        }
        return null;
    }

    @SuppressWarnings({ "unchecked" })
    protected Map<String, Object> getMap(String path, String accessToken) {
        try {
            OAuth2RestOperations restTemplate = super.restTemplate;
            if (restTemplate == null) {
                BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
                resource.setClientId(super.clientId);
                restTemplate = new OAuth2RestTemplate(resource);
            }
            OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext()
                    .getAccessToken();
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(
                        accessToken);
                token.setTokenType(super.tokenType);
                restTemplate.getOAuth2ClientContext().setAccessToken(token);
            }
            Map<String, Object> profile = restTemplate.getForEntity(path, Map.class).getBody();



            return profile;
        }
        catch (Exception ex) {
            return Collections.<String, Object>singletonMap("error",
                    "Could not fetch user details");
        }
    }
}
