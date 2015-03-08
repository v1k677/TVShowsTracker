package com.v1k6s.tvshows;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.io.Writer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import android.util.Log;

import android.content.Context;

class ShowStore {
    final static String TAG = "ShowStore";
    final static String FILE_NAME = "shows";
    private static ArrayList tvShows = null;

    static public void load(Context c) throws IOException, JSONException {
        Log.d(TAG, " laoding ..");
        tvShows = new ArrayList();

        BufferedReader reader = null;
        try {
            InputStream in = c.openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                tvShows.add(new Show(array.getJSONObject(i)));
            }
            Log.d(TAG, " loaded of size " + tvShows.size());
        } catch (FileNotFoundException e) {
            Log.e(TAG, "EX ", e);
        } finally {
            if (reader != null) { reader.close(); }
        }
    }

    static public void store(Context c) throws JSONException, IOException {
        Log.d(TAG, " stroing ...");
        if (tvShows == null || tvShows.isEmpty()) {
            return;
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < tvShows.size(); i++) {
            Show s = (Show) tvShows.get(i);
            jsonArray.put(s.toJSON());
        }
        Log.d(TAG, " convertex to json ");
        Writer writer = null;
        try {
            OutputStream out = c.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
            Log.d(TAG, "writtedn");
        } finally {
            if (writer != null) { writer.close();}
        }
    }

    static public ArrayList getAllShows(Context c) {
        if (tvShows == null) {
            try {
                load(c);
            } catch (Exception e) {
            }
        }
        return tvShows;
    }

    static public int getCountShows(Context c) {
        if (tvShows == null) {
            try {
                load(c);
            } catch (Exception e) {
            }
        }
        return tvShows.size();
    }

    static public Show getShowByIndex(int idx) {
        return (Show) tvShows.get(idx);
    }

    static public void add(Show s) {
        tvShows.add(s);
    }

    static public void overRide(int position, Show newShow) {
        tvShows.set(position, newShow);
    }

    static public void delete(int idx) {
        tvShows.remove(idx);
    }
}