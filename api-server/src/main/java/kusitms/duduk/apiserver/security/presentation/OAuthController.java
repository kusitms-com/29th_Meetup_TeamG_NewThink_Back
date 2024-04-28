package kusitms.duduk.apiserver.security.presentation;

import kusitms.duduk.apiserver.security.application.OAuthService;
import kusitms.duduk.apiserver.security.presentation.dto.OAuthLoginResponse;
import kusitms.duduk.domain.security.domain.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping( "/api/oauth")
public class OAuthController implements OAuthControllerDocs {

    private final OAuthService oAuthService;

    @GetMapping("/{provider}")
    public ResponseEntity<OAuthLoginResponse> oAuthLogin(
        @PathVariable final String provider,
        @RequestHeader("Authorization") final String accessToken) {
        return new ResponseEntity<>(oAuthService.login(Provider.from(provider), accessToken),
            HttpStatus.OK);
    }
}