package com.example.geom_databinding.utils;

import android.util.Log;

import com.example.geom_databinding.model.Point;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonPointDeserializer implements JsonDeserializer<Point> {

    private final String TAG = getClass().getSimpleName();

    // De-serialize
    @Override
    public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{

        Point point = new Point();

        if(json.isJsonObject()){

            for(Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()){

                if(entry.getKey().equals("g")){

                    Log.d(TAG, "Primitive : " + entry.getKey() + " = " + entry.getValue().getAsString());
                }
            }
        }

        return point;
    }

}
