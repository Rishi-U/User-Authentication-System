package com.rishi.authsystem.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW = 60 * 1000;

    private final Map<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                   @NonNull HttpServletResponse response,
                                   @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // 🔥 Apply ONLY on login
        if (!path.equals("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        long currentTime = Instant.now().toEpochMilli();

        // 🔥 Clean old entries
        requestCounts.entrySet().removeIf(entry ->
                currentTime - entry.getValue().timestamp > TIME_WINDOW
        );

        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }

        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        RequestInfo info = requestCounts.getOrDefault(ip, new RequestInfo(0, currentTime));

        if (currentTime - info.timestamp > TIME_WINDOW) {
            info = new RequestInfo(0, currentTime);
        }

        info.count++;
        requestCounts.put(ip, info);

        if (info.count > MAX_REQUESTS) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                    "status": 429,
                    "message": "Too many requests. Try again later."
                }
            """);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static class RequestInfo {
        int count;
        long timestamp;

        RequestInfo(int count, long timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }
}