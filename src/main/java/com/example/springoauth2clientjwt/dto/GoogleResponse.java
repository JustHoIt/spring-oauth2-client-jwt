package com.example.springoauth2clientjwt.dto;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class GoogleResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {

        return "google";
    }

    @Override
    public String getProviderID() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
