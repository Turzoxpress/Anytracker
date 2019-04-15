package com.lonetiger.onetracker.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.lonetiger.onetracker.Other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelGetData implements IModelGetData {

    Constants constants;
    DatabaseReference databaseReference;
    private static String TAG = "ModelGetData";


    @Override
    public void getDataFromServer(final String user_id, final GetValueInteractor gt, Context ct) {


        //--
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("code", user_id);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(ct);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.server_path+Constants.getDataURL, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);
                        //iosDialog.cancel();
                        //Parse_signup_data(respo);

                        String codeS = parse_getData1(respo);

                        if(codeS.equalsIgnoreCase("200")){

                            //GetValue gtv = snapshot2.getValue(GetValue.class);
                           // gt.onFetchDataSuccessfully(gtv);
                            //Log.d(TAG,gtv.getUser_id()+"---------------"+gtv.getUser_lat()+"----------------"+gtv.getUser_long());
                            Gson gson = new Gson();
                            GetValue gtv = gson.fromJson(parse_getData2(respo).toString(),GetValue.class);
                            gt.onFetchDataSuccessfully(gtv);


                        }else if(codeS.equalsIgnoreCase("201")){

                            gt.onFetchDataFailure(2);

                        }else {

                            gt.onFetchDataFailure(2);
                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        //iosDialog.cancel();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        //rgs.onHandleCodeFromModel(404);
                        gt.onFetchDataFailure(1);
                        Log.d(TAG,error.toString());
                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);


        //--------------

/*

        //--
        final String saving_path = constants.New_User_Path;

        //creating the collection or table of user
        databaseReference = FirebaseDatabase.getInstance().getReference(saving_path);


        //--
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(user_id)) {

                    //--
                    DatabaseReference datarf = FirebaseDatabase.getInstance().getReference(saving_path).child(user_id);
                    datarf.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot2) {

                            GetValue gtv = snapshot2.getValue(GetValue.class);
                            gt.onFetchDataSuccessfully(gtv);
                            //Log.d(TAG,gtv.getUser_id()+"---------------"+gtv.getUser_lat()+"----------------"+gtv.getUser_long());


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            //Log.d(TAG,"Error! Database error");
                            gt.onFetchDataFailure(1);
                        }
                    });


                    //-----------


                } else {

                    //Log.d(TAG,"Error! Id not found!");
                    //askingForCode();
                    gt.onFetchDataFailure(2);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //Log.d(TAG,"Error! Database error");
                gt.onFetchDataFailure(1);
            }
        });


        //--------


        //----------
        */


    }

    @Override
    public void stopDataFromServer(GetValueInteractor gt) {

        gt.onStoppedGettingData("Getting data from server stopped");


    }

    private String parse_getData1(String getData){

        String tempS = "";
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(getData);
            tempS = jsonObject.getString("code");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  tempS;

    }

    private JSONObject parse_getData2(String getData){

        JSONObject tempS = null;
        try {
            JSONObject jsonObject=new JSONObject(getData);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray jsonArray=jsonObject.getJSONArray("msg");
                JSONObject userdata = jsonArray.getJSONObject(0);
                tempS = userdata;

            }else {

            }

        } catch (JSONException e) {
            //iosDialog.cancel();
            e.printStackTrace();
        }


      return tempS;

    }

}