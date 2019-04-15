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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.lonetiger.onetracker.Other.Constants;
import com.lonetiger.onetracker.Presenter.IPresenterRegisterUser;
import com.lonetiger.onetracker.Presenter.PresenterRegisterUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelRegisterUser implements IModelRegisterUser {




    private String TAG = "ModelRegisterUser";
    private Constants constants;
    int returnCode = 0;

    IPresenterRegisterUser iPresenterRegisterUser;


    //Communication with Firebase database
    DatabaseReference databaseReference;
    RegisterInteractor rgs;
    private Gson gson;;






    public void registerUserInFirebase(final String num,final String user_name ,final String user_lat, final String user_long, final RegisterInteractor rgs, Context ct) {


        //--
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("code", num);
            parameters.put("user_name",user_name);
            parameters.put("user_lat", user_lat);
            parameters.put("user_long", user_long);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(ct);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.server_path+Constants.registrationURL, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);
                        //iosDialog.cancel();
                        //Parse_signup_data(respo);

                        String tempS = Parse_register_data(respo);
                        if(tempS.equalsIgnoreCase("201")){
                            //-- id already exists
                            rgs.onHandleCodeFromModel(201);

                        }else if(tempS.equalsIgnoreCase("200")){

                            rgs.onHandleCodeFromModel(200);

                        }else {

                            rgs.onHandleCodeFromModel(404);

                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        //iosDialog.cancel();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        rgs.onHandleCodeFromModel(404);
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
        String saving_path = constants.New_User_Path;

        //creating the collection or table of user
        databaseReference = FirebaseDatabase.getInstance().getReference(saving_path);



        //--
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(num)) {

                 rgs.onHandleCodeFromModel(401);

                }else{

                    //create new user
                    StoreValue strValue = new StoreValue(num,user_name,user_lat,user_long);
                    databaseReference.child(num).setValue(strValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            rgs.onHandleCodeFromModel(301);


                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    rgs.onHandleCodeFromModel(404);


                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                rgs.onHandleCodeFromModel(404);
            }
        });

        //------------

*/


    }

    @Override
    public void updateSelfData(final String num,final String user_name, final String user_lat, final String user_long, Context ct) {


        //--
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("code", num);
            parameters.put("user_name",user_name);
            parameters.put("user_lat", user_lat);
            parameters.put("user_long", user_long);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(ct);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.server_path+Constants.updateURL, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);
                        //iosDialog.cancel();
                        //Parse_signup_data(respo);

                        String tempS = Parse_update_data(respo);
                        if(tempS.equalsIgnoreCase("201")){
                            //-- id already exists
                            //rgs.onHandleCodeFromModel(201);
                            Log.d(TAG,"Database failure,can't update data on the same id");

                        }else if(tempS.equalsIgnoreCase("200")){

                           // rgs.onHandleCodeFromModel(200);
                            Log.d(TAG,"Data updated successfully! ---- "+String.valueOf(num)+"----- "+String.valueOf(user_name)+"----- "+String.valueOf(user_lat)+"-----------"+String.valueOf(user_long));

                        }else {

                            //rgs.onHandleCodeFromModel(404);
                            Log.d(TAG,"Database failure,can't update data on the same id");

                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        //iosDialog.cancel();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"Database failure,can't update data on the same id");
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
        String saving_path = constants.New_User_Path;

        //creating the collection or table of user
        databaseReference = FirebaseDatabase.getInstance().getReference(saving_path);


        StoreValue strValue = new StoreValue(num,user_name,user_lat,user_long);
        databaseReference.child(num).setValue(strValue).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

               Log.d(TAG,"Data updated successfully! ---- "+String.valueOf(num)+"----- "+String.valueOf(user_name)+"----- "+String.valueOf(user_lat)+"-----------"+String.valueOf(user_long));


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG,"Database failure,can't update data on the same id");


                    }
                });



       // Log.d(TAG,"Came to model");
*/

    }

    @Override
    public String getNumber() {
        return null;
    }

    @Override
    public String getLat() {
        return null;
    }

    @Override
    public String getLong() {
        return null;
    }

    public String Parse_register_data(String registerData){

        String tempS = "";
        try {
            JSONObject jsonObject=new JSONObject(registerData);
            String code=jsonObject.optString("code");
            tempS = code;


        } catch (JSONException e) {

            e.printStackTrace();
        }

        return tempS;

    }

    public String Parse_update_data(String registerData){

        String tempS = "";
        try {
            JSONObject jsonObject=new JSONObject(registerData);
            String code=jsonObject.optString("code");
            tempS = code;


        } catch (JSONException e) {

            e.printStackTrace();
        }

        return tempS;

    }


}
