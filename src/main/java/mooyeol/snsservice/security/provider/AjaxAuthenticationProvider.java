package mooyeol.snsservice.security.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.security.service.MemberContext;
import mooyeol.snsservice.security.service.MemberDetailsServiceImpl;
import mooyeol.snsservice.security.token.AjaxAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@RequiredArgsConstructor
@Slf4j
public class AjaxAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MemberDetailsServiceImpl memberDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        MemberContext memberContext = (MemberContext) memberDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, memberContext.getMember().getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new AjaxAuthenticationToken(memberContext.getMember(), null, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AjaxAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
