package com.prog3.project.server;

import com.example.common.Email;
import java.io.*;
import java.util.*;

public class FileManipulator {

    private static final String FILE_EXTENSION = ".email";

    private static void createDirectory(String name) {
        File directory = new File(name);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static boolean existsDirectory(String name) {
        File directory = new File(name);
        return directory.exists();
    }

    public static ArrayList<Email> readEmails(String name) {
    FileInputStream fi = null;
    ObjectInputStream oi = null;
    File directory = new File(name);
    ArrayList<Email> listEmails = new ArrayList<>();

    if (directory.exists() && directory.isDirectory()) {
        for (File fileEntry: Objects.requireNonNull(directory.listFiles())) {
            try {
                fi =  new FileInputStream(fileEntry);
                oi = new ObjectInputStream((fi));
                if (fi.available() > 0) {
                    Object object =  oi.readObject();
                    if (object instanceof Email)
                        listEmails.add((Email) object);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (fi != null)
                        fi.close();
                    if (oi != null)
                        oi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    } else
        createDirectory(name);
    return listEmails;
    }

    public static void writeEmail(Email email, String directory) {
        FileOutputStream fo = null;
        ObjectOutputStream bo = null;
        try {
            File file = new File(directory, email.getId().toString() + FILE_EXTENSION);
            fo = new FileOutputStream(file);
            bo = new ObjectOutputStream(fo);
            bo.writeObject(email);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fo != null)
                    fo.close();
                if (bo != null)
                    bo.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static boolean cancelEmail(String directoryName, UUID idEmail) {
        File directory = new File(directoryName);
        for (File file: Objects.requireNonNull(directory.listFiles())) {
            System.out.println(file.getName());
            if (file.getName().equals(idEmail.toString() + FILE_EXTENSION))
                return file.delete();
        }
        return false;
    }

    public static void readEmail(String directoryName, Email emailToRead) {
        cancelEmail(directoryName, emailToRead.getId());
        writeEmail(emailToRead, directoryName);
    }
/*
    public static void main(String[] args) {
        createDirectory("inbox-giulia@gmail.com");
        ArrayList<String> r = new ArrayList<>();
        r.add("giulia@gmail.com");
        writeEmail(new Email("alessandro@gmail.com", r, "prova", "ciccia ciccia", "03/07/2022 11:53: 01 PM"), "inbox-giulia@gmail.com");
        ArrayList<Email> result = readEmails("inbox-giulia@gmail.com");
        System.out.println(result);
        //cancelEmail("inbox-giulia@gmail.com", result.get(0).getId());
    }
*/
}