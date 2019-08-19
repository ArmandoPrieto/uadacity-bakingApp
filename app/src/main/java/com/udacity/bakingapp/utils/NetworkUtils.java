package com.udacity.bakingapp.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils {
    private final static String BAKING_JSON_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private NetworkUtils() {
    }

    public static URL buildUrl(String sortBy, int page) {
        Uri builtUri;
        URL url = null;

        builtUri = Uri.parse(BAKING_JSON_BASE_URL).buildUpon().build();

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
