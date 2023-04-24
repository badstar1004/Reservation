package com.store.reservation.config.jwt.filter;

import com.store.reservation.config.jwt.JwtAuthenticationProvider;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@WebFilter(urlPatterns = "/member/*")
@RequiredArgsConstructor
public class MemberFilter implements Filter {

    private final JwtAuthenticationProvider jwtProvider;
    private final String TOKEN = "ACCOUNT-TOKEN";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(TOKEN);

        // 회원가입, 로그인 요청 제외
        String path = httpServletRequest.getRequestURI()
            .substring(httpServletRequest.getContextPath().length());
        if (!path.startsWith("/member/signin") && !path.startsWith("/member/signup")) {
            if (!jwtProvider.validateToken(token)) {
                throw new ServletException("유효하지않은 접근입니다.");
            }
        }

        chain.doFilter(request, response);
    }
}
