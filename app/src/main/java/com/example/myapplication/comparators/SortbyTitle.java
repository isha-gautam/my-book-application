package com.example.myapplication.comparators;

import com.example.myapplication.Book;

import java.util.Comparator;

public class SortbyTitle implements Comparator<Book> {

    @Override
    public int compare(Book b1, Book b2) {
        int titleDiff = b1.get_title().compareToIgnoreCase(b2.get_title());
        int authorDiff = b1.get_author().compareToIgnoreCase(b2.get_author());
        int idDiff = b1.getId() - b2.getId();

        if (titleDiff != 0)
            return titleDiff;
        else if (authorDiff != 0)
            return authorDiff;
        else
            return idDiff;
    }
}
