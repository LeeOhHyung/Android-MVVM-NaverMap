package com.example.geom_databinding.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.geom_databinding.model.Point;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

// Do not use
public class PostCameraPositionTask extends AsyncTask<String, Void, ArrayList<Point>> {

    private final String TAG = getClass().getSimpleName();
    private final String url_address = "YOUR_URL_ADDRESS";

    //private ArrayList<Point> result_points = new ArrayList<>();
    private ArrayList<Point> result_points;
    private ArrayList<Marker> result_markers;
    private ArrayList<String> temp = new ArrayList<>();
    private MutableLiveData<List<Marker>> LiveDataMarkers;
    private NaverMap mMap;

    // 생성자
    public PostCameraPositionTask(NaverMap naverMap,
                                  ArrayList<Point> points,
                                  ArrayList<Marker> markers,
                                  MutableLiveData<List<Marker>> liveDataMarkers){

        this.result_points = points;
        this.mMap = naverMap;
        this.result_markers = markers;
        this.LiveDataMarkers = liveDataMarkers;

        result_points.clear();
        result_markers.clear();
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Point> doInBackground(String... params){
        Log.d(TAG, "Post Center Point of the Camera");

        try{
            JSONObject jsonObject = new JSONObject();

            /** POST 스키마 **/
            jsonObject.accumulate("lng", Double.parseDouble(params[0])); // 카메라 경도
            jsonObject.accumulate("lat", Double.parseDouble(params[1])); // 카메라 위도

            /** 남서, 남동, 북동, 북서, 남서 **/
            jsonObject.accumulate("swlng", Double.parseDouble(params[2])); // 남서
            jsonObject.accumulate("swlat", Double.parseDouble(params[3]));

            jsonObject.accumulate("selng", Double.parseDouble(params[4])); // 남동
            jsonObject.accumulate("selat", Double.parseDouble(params[5]));

            jsonObject.accumulate("nelng", Double.parseDouble(params[6])); // 북동
            jsonObject.accumulate("nelat", Double.parseDouble(params[7]));

            jsonObject.accumulate("nwlng", Double.parseDouble(params[8])); // 북서
            jsonObject.accumulate("nwlat", Double.parseDouble(params[9]));

            HttpURLConnection con = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(url_address + "/current");

                Log.d(TAG, "url : " + url_address + "/current");

                /** Server Connection **/
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");

                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json"); //application을 JSON형식으로 전송
                con.setRequestProperty("Accept", "text/html");

                con.setDoOutput(true); // Outstream으로 Post데이터를 넘겨주겠다는 의미
                con.setDoInput(true); // Inputstream으로 서버로 부터 응답을 받겠다는 의미

                con.connect();

                //서버로 보내기 위해서 스트림을 만듬 <-> InputStream inStream...
                OutputStream outStream = con.getOutputStream();

                //버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());

                Log.d(TAG, "서버로 보내는 문자열 : " + jsonObject.toString());

                writer.flush();
                writer.close();

                /** 서버로 부터 응답 메세지를 받음 **/
                InputStream stream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine()) != null){

                    /** buffer에 요청한 정보가 담겨있음. **/
                    buffer.append(line);
                }
                Log.d(TAG, "받은 문자열 : " + buffer.toString());

//                JSONArray JA = new JSONArray(buffer.toString());
//                for(int i = 0; i < JA.length(); ++i){
//                    JSONObject JO = (JSONObject) JA.get(i);
//
//                    temp.add(JO.get("g").toString());
//                    JSONObject geomJO = new JSONObject(temp.get(i));
//
//                    Point point = new Point();
//                    point.setPoint(Integer.parseInt(JO.getString("id")),
//                            new LatLng(Double.parseDouble(geomJO.getString("y")),
//                                    Double.parseDouble(geomJO.getString("x"))),
//                            "");
//
//                    result_points.add(point);
//                }

                Log.d(TAG, "받은 위치 개수 : " + result_points.size());

            }catch (MalformedURLException e){
                e.printStackTrace();
                Log.e(TAG, e + " 에러 발생");
            }catch (IOException e){
                e.printStackTrace();
                Log.e(TAG, e + " 에러 발생");
            }

        }catch (JSONException e){
            e.printStackTrace();
            Log.e(TAG, e + " 에러 발생");
        }

        return result_points;
    }

    @Override
    protected void onPostExecute(ArrayList<Point> aParams){
        super.onPostExecute(aParams);

        final int zoom = (int) mMap.getCameraPosition().zoom;

        for(int i = 0; i < aParams.size(); i = i + 2){

            Log.d(TAG, "aParams.size() : " + aParams.size());

//            Marker marker = new Marker(aParams.get(i).getLatlng());
//            marker.setIcon(MarkerIcons.YELLOW);
//            marker.setCaptionHaloColor(Color.rgb(200, 255, 200));
//            marker.setCaptionColor(Color.BLUE);
//            marker.setCaptionAlign(Align.Bottom);
//            marker.setCaptionText(String.valueOf(aParams.get(i).getId()));
//            marker.setCaptionTextSize(14);
//            marker.setWidth(zoom * 6);
//            marker.setHeight(zoom * 8);
//
//            //marker.setMap(mMap);
//
//            result_markers.add(marker);
        }

        LiveDataMarkers.setValue(result_markers);

        Log.d(TAG, "onPostExecute 성공함.");
    }
}
