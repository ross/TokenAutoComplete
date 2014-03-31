package com.tokenautocomplete.example;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Simple container object for contact data
 *
 * Created by mgod on 9/12/13.
 * @author mgod
 */
public class Person implements Serializable {

    public static Person get(final String completionText) {
        return new Person(completionText, completionText);
    }

    public static Person get(final Cursor cursor) {
        return new Person(cursor.getString(2), cursor.getString(3));
    }

    private String name;
    private String number;

    public Person(String n, String e) {
        name = n;
        number = e;
    }

    public String getName() { return name; }
    public String getNumber() { return number; }

    @Override
    public String toString() { return name; }
}
