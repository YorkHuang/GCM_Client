package com.grtek.user.gcm_client.http;


import java.util.HashMap;

/**
 * Created by user on 2016/7/25.
 */
public class URLParameter {

    public URLParameter(){

    }

    public HashMap<String, String> getCbf(String cat, String data, String date, String mac,
                                          String op, String type, String userid, String arraydata){


        HashMap<String, String> params = new HashMap<>();
        try {
            params.put("cat",cat);
            params.put("data",data);
            params.put("date",date);
            params.put("mac",mac);
            params.put("op",op);
            params.put("type",type);//SINGLELIST
            params.put("userid",userid);
            params.put("arraydata",arraydata);
        } catch (Exception e){

        }
        return params;
    }


    public HashMap<String, String> updateStreeLightLocation(String mac, String lat, String lng){

        HashMap<String, String> params = new HashMap<>();
        try{
            params.put("type", "UPDATE_STL_LOCATION");
            params.put("mac", mac);
            params.put("lat", lat);
            params.put("lng", lng);
        } catch (Exception e){

        }
        return params;
    }


    public HashMap<String, String> setUsersAppToken(String userid, String token){

        HashMap<String, String> params = new HashMap<>();
        try{
            params.put("type", "UPDATE_APP_TOKEN");
            params.put("userid", userid);
            params.put("token", token);
        }catch(Exception e){

        }
        return params;
    }


    public HashMap<String, String> getLogin(String email, String password){

        HashMap<String, String> params = new HashMap<>();
        try{
            params.put("type", "LOGIN");
            params.put("email", email);
            params.put("password", password);
        }catch(Exception e){

        }
        return params;
    }

    public HashMap<String, String> getSignIn(String email, String password){

        HashMap<String, String> params = new HashMap<>();
        try{
            params.put("type", "NEW_USER");
            params.put("email", email);
            params.put("password", password);
        }catch(Exception e){

        }
        return params;
    }

    public HashMap<String, String> getEmergencyStreeLightInfo(){

        HashMap<String, String> params = new HashMap<>();
        try{
            params.put("type", "SELECT_EMERGENCY_STL");
        } catch (Exception e){

        }
        return params;
    }

}
