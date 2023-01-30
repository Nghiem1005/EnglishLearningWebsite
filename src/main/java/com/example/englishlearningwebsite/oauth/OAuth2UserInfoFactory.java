package com.example.englishlearningwebsite.oauth;

import com.example.englishlearningwebsite.entities.enums.AuthProvider;
import com.example.englishlearningwebsite.exception.OAuth2AuthenticationProcessingException;
import java.util.Map;

public class OAuth2UserInfoFactory {
  public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
    //Check provider login
    if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
      return new GoogleOAuth2UserInfo(attributes);
    } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
      return new FacebookOAuth2UserInfo(attributes);
    } else {
      throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
    }
  }
}
