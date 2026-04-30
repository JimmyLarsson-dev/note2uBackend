//package com.example.note2ubackendnosecurity.authentication;
//
//import jakarta.servlet.ReadListener;
//import jakarta.servlet.ServletInputStream;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletRequestWrapper;
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//
//public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
//
//    private final byte[] body;
//
//    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
//        super(request);
//        this.body = request.getInputStream().readAllBytes();
//    }
//
//    public byte[] getBodyAsByteArray() { return body; }
//
//    @Override
//    public ServletInputStream getInputStream() {
//        ByteArrayInputStream stream = new ByteArrayInputStream(body);
//        return new ServletInputStream() {
//            public int read() { return stream.read(); }
//            public boolean isFinished() { return stream.available() == 0; }
//            public boolean isReady() { return true; }
//            public void setReadListener(ReadListener rl) {}
//        };
//    }
//
//    @Override
//    public BufferedReader getReader() {
//        return new BufferedReader(new InputStreamReader(
//                getInputStream(), StandardCharsets.UTF_8));
//    }
//}