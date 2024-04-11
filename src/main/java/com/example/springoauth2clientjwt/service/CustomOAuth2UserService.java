package com.example.springoauth2clientjwt.service;

import com.example.springoauth2clientjwt.dto.*;
import com.example.springoauth2clientjwt.entity.UserEntity;
import com.example.springoauth2clientjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info(String.valueOf(oAuth2User));

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderID();
        UserEntity existUser = userRepository.findByUsername(username);

        if (existUser == null) {

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            userEntity.setRole("ROLE_USER");
            userEntity.setOauth2name(registrationId);

            UserDTO userDTO = new UserDTO();
            userDTO.setName(username);
            userDTO.setUsername(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");
            userDTO.setOauth2name(registrationId);

            return new CustomOAuth2User(userDTO);
        }else{
            existUser.setEmail(oAuth2Response.getEmail());
            existUser.setName(oAuth2Response.getName());

            userRepository.save(existUser);

            UserDTO userDTO = new UserDTO();
            userDTO.setName(username);
            userDTO.setUsername(oAuth2Response.getName());
            userDTO.setRole(existUser.getRole());
            userDTO.setOauth2name(registrationId);

            return new CustomOAuth2User(userDTO);
        }
    }
}
