package csd214.bookstore.pojos;

import java.io.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 * @author fcarella
 */


public abstract class Editable implements Serializable, SaleableItem {
    public Editable() {
    }
//    private Long id;

    public abstract void edit(Scanner input);
    public abstract void initialize(Scanner input);

    public String getInput(Scanner input, String defaultValue) {
        String ss = input.nextLine();
        if (ss.trim().isEmpty()) {
            return defaultValue;
        }
        return ss.trim();
    }

    public int getInput(Scanner input, int defaultValue) {
        String s = input.nextLine();
        if (s.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public double getInput(Scanner input, double defaultValue) {
        String s = input.nextLine();
        if (s.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public boolean getInput(Scanner input, boolean defaultValue) {
        String s = input.nextLine();
        if (s.trim().isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(s.trim());
    }

    public Date getInput(Scanner input, Date defaultValue) {
        String s = input.nextLine();
        if (s.trim().isEmpty()) {
            return defaultValue;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            return formatter.parse(s.trim());
        } catch (ParseException e) {
            IO.println("Invalid Date. Keeping default.");
            return defaultValue;
        }
    }
}