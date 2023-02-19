package com.example.telegrammicroservice.helpers;

import java.util.Scanner;

public class SimpleHelper {
    public static String promptString(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}