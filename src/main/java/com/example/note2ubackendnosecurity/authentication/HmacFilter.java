//package com.example.note2ubackendnosecurity.authentication;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.security.InvalidKeyException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.*;
//
//@Component
//@Order(1)
//public class HmacFilter extends OncePerRequestFilter {
//
//    @Value("${hmacSecret}")
//    private static String SECRET;
//    private static final long TIMESTAMP_TOLERANCE_MS = 5 * 60 * 1000; // 5 minutes
//
//    // Cache of recently seen nonces to prevent replay attacks
//    private final Set<String> usedNonces = Collections.synchronizedSet(
//            Collections.newSetFromMap(new LinkedHashMap<>() {
//                protected boolean removeEldestEntry(Map.Entry<String, Boolean> eldest) {
//                    return size() > 10_000; // cap memory usage
//                }
//            })
//    );
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain chain)
//            throws ServletException, IOException {
//        String timestamp = request.getHeader("X-Timestamp");
//        String nonce     = request.getHeader("X-Nonce");
//        String signature = request.getHeader("X-Signature");
//
//        if (timestamp == null || nonce == null || signature == null) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing HMAC headers");
//            return;
//        }
//
//        // 1. Check timestamp freshness
//        long requestTime = Long.parseLong(timestamp);
//        if (Math.abs(System.currentTimeMillis() - requestTime) > TIMESTAMP_TOLERANCE_MS) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Request expired");
//            return;
//        }
//
//        // 2. Check nonce hasn't been used before (replay attack prevention)
//        String nonceKey = timestamp + ":" + nonce;
//        if (!usedNonces.add(nonceKey)) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Replayed request");
//            return;
//        }
//
//        // 3. Read body (need a wrapper since InputStream can only be read once)
//        CachedBodyHttpServletRequest wrappedRequest =
//                new CachedBodyHttpServletRequest(request);
//        String body = new String(wrappedRequest.getBodyAsByteArray(), StandardCharsets.UTF_8);
//
//        // 4. Recompute expected signature
//        String message = timestamp + "." + nonce + "." + body;
//        String expectedSignature = computeHmac(message, SECRET);
//
//        if (!MessageDigest.isEqual(
//                expectedSignature.getBytes(StandardCharsets.UTF_8),
//                signature.getBytes(StandardCharsets.UTF_8))) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid signature");
//            return;
//        }
//
//        chain.doFilter(wrappedRequest, response);
//    }
//
//    private String computeHmac(String message, String secret) throws IOException {
//        try {
//            Mac mac = Mac.getInstance("HmacSHA256");
//            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
//            byte[] hash = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
//            return Base64.getEncoder().encodeToString(hash);
//        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
//            throw new IOException("Failed to compute HMAC", e);
//        }
//    }
//}
