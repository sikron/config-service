package com.skronawi.configservice.core.strategies;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.core.SourceLoadingException;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RestLoadingStrategy extends AbstractExternalSourceLoadingStrategy {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private TypeReference<HashMap<String, String>> hashMapTypeReference;

    @Override
    public void init(ConfigSourceConfiguration configSourceConfiguration) {
        super.init(configSourceConfiguration);
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
        hashMapTypeReference = new TypeReference<HashMap<String, String>>() {
        };
    }

    @Override
    public Map<String, String> load(String name) throws SourceLoadingException {

        HttpEntity httpEntity = null;
        if (credentials != null) {
            httpEntity = authorizeBasic();
        }

        try {
            ResponseEntity<String> response = restTemplate
                    .exchange(url + name, HttpMethod.GET, httpEntity, String.class);
            if (response.getStatusCode() != HttpStatus.OK
                    || !response.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON)) {
                throw new SourceLoadingException("error on http request: " + response.getStatusCode());
            }
            return objectMapper.readValue(response.getBody(), hashMapTypeReference);

        } catch (SourceLoadingException e) {
            throw e;
        } catch (Exception e) {
            throw new SourceLoadingException(e);
        }
    }

    private HttpEntity authorizeBasic() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64Utils.encodeToString(
                (credentials.getUsername() + ":" + credentials.getPassword()).getBytes()));
        return new HttpEntity(headers);
    }

    //authentication is optional
    @Override
    public void assertValid() {
//        assert credentials != null;
//        assert credentials.getUsername() != null && credentials.getUsername().length() > 0;
//        assert credentials.getPassword() != null && credentials.getPassword().length() > 0;
        assert url != null && url.length() > 0;
    }
}
