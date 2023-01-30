package com.example.englishlearningwebsite.component;

import static com.example.englishlearningwebsite.component.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.example.englishlearningwebsite.config.AppProperties;
import com.example.englishlearningwebsite.controller.ScheduleController;
import com.example.englishlearningwebsite.entities.RefreshToken;
import com.example.englishlearningwebsite.entities.principal.UserPrincipal;
import com.example.englishlearningwebsite.exception.BadRequestException;
import com.example.englishlearningwebsite.service.RefreshTokenService;
import com.example.englishlearningwebsite.utils.CookieUtils;
import com.example.englishlearningwebsite.utils.TokenProvider;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private TokenProvider tokenProvider;

  private AppProperties appProperties;
  private final static Log logger = LogFactory.getLog(ScheduleController.class);

  private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Autowired
  private RefreshTokenService refreshTokenService;


  @Autowired
  OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AppProperties appProperties,
      HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
    this.tokenProvider = tokenProvider;
    this.appProperties = appProperties;
    this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String targetUrl = determineTargetUrl(request, response, authentication);

    if (response.isCommitted()) {
      logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }

    clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  //Create url redirect
  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
        .map(Cookie::getValue);

    if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
      throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
    }

    String targetUrl = redirectUri.orElse("http://localhost:3002/login");

    String token = tokenProvider.createToken(authentication);

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication);
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    return UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam("token", token)
        .queryParam("refreshToken", refreshToken.getToken())
        .queryParam("userId", userPrincipal.getId())
        .build().toUriString();
  }

  protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
  }

  private boolean isAuthorizedRedirectUri(String uri) {
    URI clientRedirectUri = URI.create(uri);

    return appProperties.getOauth2().getAuthorizedRedirectUris()
        .stream()
        .anyMatch(authorizedRedirectUri -> {
          // Only validate host and port. Let the clients use different paths if they want to
          URI authorizedURI = URI.create(authorizedRedirectUri);
          if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
              && authorizedURI.getPort() == clientRedirectUri.getPort()) {
            return true;
          }
          return false;
        });
  }
}
