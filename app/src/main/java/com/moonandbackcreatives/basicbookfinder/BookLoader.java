package com.moonandbackcreatives.basicbookfinder;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by bryantruong on 7/2/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>>{
    private String mURL;

    public BookLoader(Context context, String url){
        super(context);
        mURL = url;
    }

    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mURL == null){
            return null;

        }
        List<Book> bookList = QueryUtils.fetchBookData(mURL);
        return bookList;
    }
}
