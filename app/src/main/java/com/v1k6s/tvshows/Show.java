package com.v1k6s.tvshows;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by vik on 3/7/15.
 */

class Show {
    final private static String JSON_ID = "id";
    final private static String JSON_NAME = "name";
    final private static String JSON_SEASON = "season";
    final private static String JSON_EPISODE = "episode";

    private UUID id;
    private String name;
    private int season;
    private int episode;

    public Show(String name, int season, int episode) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.season = season;
        this.episode = episode;
    }

    public Show(JSONObject ob) throws JSONException {
        this.id = UUID.fromString(ob.getString(JSON_ID));
        this.name = ob.getString(JSON_NAME);
        this.episode = ob.getInt(JSON_EPISODE);
        this.season = ob.getInt(JSON_SEASON);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject ob = new JSONObject();
        ob.put(JSON_ID, id.toString());
        ob.put(JSON_NAME, name);
        ob.put(JSON_EPISODE, episode);
        ob.put(JSON_SEASON, season);
        return ob;
    }
}
