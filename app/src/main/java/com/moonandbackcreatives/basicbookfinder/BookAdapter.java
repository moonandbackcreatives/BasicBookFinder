package com.moonandbackcreatives.basicbookfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bryantruong on 6/29/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, List<Book> books){
        super(context, 0 ,books);
    }

    public View getView(int position, View convertedView, ViewGroup parent){
        View listItemView = convertedView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item
            , parent, false);
        }
        Book currentBook = getItem(position);
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.authors);
        String authors = "";
        for (int i = 0; i < currentBook.getAuthors().length; i++){
            if (authors.isEmpty()) {
                authors = currentBook.getAuthors()[i];
            } else{
                authors = authors + " & " + currentBook.getAuthors()[i];
            }
            authorTextView.setText(authors);
        }
        TextView bookTitleView = (TextView) listItemView.findViewById(R.id.bookTitle);
        String bookTitle = currentBook.getBookTitle();
        bookTitleView.setText(bookTitle);

        TextView descriptionTextView = (TextView) listItemView.findViewById(R.id.description);
        String bookDescription = currentBook.getBookDescription();
        descriptionTextView.setText(bookDescription);

        return listItemView;

    }
}
