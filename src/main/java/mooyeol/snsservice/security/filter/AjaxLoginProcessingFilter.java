package mooyeol.snsservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.controller.MemberDto;
import mooyeol.snsservice.security.token.AjaxAuthenticationToken;
import mooyeol.snsservice.security.util.WebUtil;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    private ObjectMapper mapper = new ObjectMapper();

    public AjaxLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/member/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
            throw new IllegalArgumentException("not supported");
        }

        MemberDto memberDto = mapper.readValue(request.getReader(), MemberDto.class);

        if (!StringUtils.hasLength(memberDto.getEmail()) || !StringUtils.hasLength(memberDto.getPassword())) {
            throw new AuthenticationServiceException("이메일 혹은 비밀번호를 입력해주세요.");
        }

        AjaxAuthenticationToken token = new AjaxAuthenticationToken(memberDto.getEmail(), memberDto.getPassword());
        return this.getAuthenticationManager().authenticate(token);
    }


}
