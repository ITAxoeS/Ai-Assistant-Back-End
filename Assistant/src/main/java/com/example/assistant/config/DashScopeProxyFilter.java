package com.example.assistant.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Component
public class DashScopeProxyFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_KEY = "sk-79d8bc7b20ce4043ad2f230910efa538";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/dashscope");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();

        System.out.println("\n====== DashScope Proxy Start ======");
        System.out.println("Request URI: " + requestURI);

        String targetUrl = "https://dashscope.aliyuncs.com"
                + requestURI.replaceFirst("/api/dashscope", "")
                + (queryString == null ? "" : "?" + queryString);

        System.out.println("Target URL: " + targetUrl);

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (!"host".equalsIgnoreCase(name)) {
                headers.addAll(name, Collections.list(request.getHeaders(name)));
            }
        }

        headers.setBearerAuth(API_KEY);
        System.out.println("Authorization header set.");

        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        System.out.println("Request Body Length: " + body.length);

        RequestEntity<byte[]> requestEntity =
                new RequestEntity<>(body, headers, HttpMethod.valueOf(request.getMethod()), URI.create(targetUrl));

        ResponseEntity<byte[]> result = restTemplate.exchange(requestEntity, byte[].class);

        System.out.println("Response Status: " + result.getStatusCode().value());

        byte[] responseBody = result.getBody();
        boolean isGzip = false;

        List<String> encodingHeaders = result.getHeaders().get("Content-Encoding");
        if (encodingHeaders != null) {
            for (String encoding : encodingHeaders) {
                if (encoding.toLowerCase().contains("gzip")) {
                    isGzip = true;
                    break;
                }
            }
        }

        if (isGzip) {
            System.out.println("Detected gzip response, decompressing...");
            try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(responseBody));
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzip.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                responseBody = out.toByteArray();
                System.out.println("Decompressed size: " + responseBody.length);
            } catch (Exception e) {
                System.err.println("Failed to decompress gzip: " + e.getMessage());
            }
        }

        response.setStatus(result.getStatusCode().value());
        result.getHeaders().forEach((k, v) -> {
            if (!"content-encoding".equalsIgnoreCase(k)) {
                v.forEach(h -> response.addHeader(k, h));
            }
        });

        System.out.println("Writing response to frontend, length: " + responseBody.length);
        ServletOutputStream os = response.getOutputStream();
        os.write(responseBody);
        os.flush();
        System.out.println("====== DashScope Proxy End ======\n");
    }
}
