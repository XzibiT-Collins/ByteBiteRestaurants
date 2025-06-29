package com.example.auth_service.security.oauth2;

import com.example.auth_service.models.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.utils.RoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomOidcUserService extends OidcUserService {
    private final UserRepository userRepository;

    public CustomOidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();

        if(userRepository.findByEmail(email).isEmpty()) {
            String username = oidcUser.getName();
            userRepository.save(
                    User
                            .builder()
                            .email(email)
                            .username(username)
                            .password("N/A")
                            .build()
            );
        }

        //set a role for Oauth users
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>(oidcUser.getAuthorities());
        grantedAuthorities.add(new SimpleGrantedAuthority(RoleEnum.ROLE_CUSTOMER.toString()));

        return new DefaultOidcUser(grantedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
