package com.moonandbackcreatives.basicbookfinder;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryantruong on 7/2/17.
 */

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils(){
    }

    public static List<Book> fetchBookData(String requestUrl){
        // Create URL object
        URL url = createURL(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Book> books = extractFeatureFromJson(jsonResponse);
        return books;
    }

    public static URL createURL(String stringURL){
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch(MalformedURLException e){
            Log.e(QueryUtils.class.getSimpleName(),"Problem creating the URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;

    }

    //helper method for makeHttpRequest helper method
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //@param earthquakeJSON is what is returned from the makeHttpRequest helper method
    private static List<Book> extractFeatureFromJson(String bookJSON){
        if (TextUtils.isEmpty(bookJSON)){
            return null;
        }
        List<Book> books = new ArrayList<>();
        try{
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");
            for (int i = 0; i < bookArray.length(); i++){
                JSONObject currentBookInformation = bookArray.getJSONObject(i);
                //TODO: Should it be bookArray.getJSONObject[i]?
                JSONObject bookInformation = currentBookInformation.getJSONObject("volumeInfo");
                String title = bookInformation.getString("title");
                JSONArray authorsArrayJSON = bookInformation.getJSONArray("authors");
                String[] authorsArrayStrings = new String[authorsArrayJSON.length()];
                for(int j = 0; j < authorsArrayStrings.length; j++){
                    authorsArrayStrings[j] = authorsArrayJSON.getString(j);
                }
                String description = bookInformation.getString("description");
                String infoLink = bookInformation.getString("infoLink");
                Book book = new Book(title, authorsArrayStrings, description, infoLink);
                books.add(book);
            }
        } catch (JSONException e){
                Log.e(LOG_TAG, "Problem extracting from the JSON string", e);
        }
        return books;
    }
}//ends class
