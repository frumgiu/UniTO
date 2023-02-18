package com.example.demotelegramapi.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SimpleHelper {
    public static String promptString(String prompt) {
        System.out.print(prompt);
       /* BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        try {
            str = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str; */
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
