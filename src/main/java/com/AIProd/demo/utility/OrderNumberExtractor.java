//package com.AIProd.demo.utility;
//
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Component
//public class OrderNumberExtractor {
//
//    private static final Pattern PATTERN = Pattern.compile("#?(\\w{3,})");
//
//    public Optional<String> extract(String message) {
//        Matcher matcher = PATTERN.matcher(message);
//        return matcher.find()
//                ? Optional.of(matcher.group(1))
//                : Optional.empty();
//    }
//}
