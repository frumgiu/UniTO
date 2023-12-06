package com.prog3.project.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    private static final Pattern EMAIL_REGEX =
        Pattern.compile("^[a-z]+[a-z0-9_]+([.-]?[a-z0-9_]+)*@[a-z]+([.-]?[a-z]+)*(\\.[a-z]{2,3})+$");

    public static boolean validate(String emailStr) {
        Matcher matcher = EMAIL_REGEX.matcher(emailStr);
        return matcher.find();
    }
}