package com.store.user;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateMaster {

    public RestTemplate getRestTemplate(int timeoutSeconds) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(timeoutSeconds * 1000);
        requestFactory.setConnectTimeout(timeoutSeconds * 1000);
        return new RestTemplate(requestFactory);
    }

    // Overloaded method with default timeout
    public RestTemplate getRestTemplate() {
        return getRestTemplate(20);
    }
}
