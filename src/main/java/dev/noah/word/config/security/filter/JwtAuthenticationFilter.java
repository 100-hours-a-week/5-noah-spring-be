package dev.noah.word.config.security.filter;

import dev.noah.word.common.JwtProvider;
import dev.noah.word.config.security.token.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwt = jwtProvider.extractJwt(httpServletRequest);

        if (jwt == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (!jwtProvider.validateJwt(jwt)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        long memberId = jwtProvider.getMemberIdFromJwt(jwt);

        // INFO 방어적 프로그래밍이라면 여기서 memberId를 검사했을 것이다.

        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(memberId));

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
