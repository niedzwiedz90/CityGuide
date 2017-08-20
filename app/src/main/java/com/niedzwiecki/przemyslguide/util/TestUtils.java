package com.niedzwiecki.przemyslguide.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by niedzwiedz on 20.08.17.
 */

class TestUtils {

    @SuppressLint("NewApi")
    public static String getResourceAsString(String name) {
        InputStream input = TestUtils.class.getClassLoader().getResourceAsStream(name);
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
            String line;
            StringBuilder resultBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line);
            }

            return resultBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Resource with name :%s doesn't exist.", name));
        }
    }

}
