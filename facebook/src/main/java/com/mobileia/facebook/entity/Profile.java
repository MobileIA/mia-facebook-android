package com.mobileia.facebook.entity;

import org.json.JSONObject;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class Profile {
    public String id;
    public String token;

    public String firstname;
    public String lastname;
    public String fullname;
    public String email;
    public String picture;

    public void fill(JSONObject data) {
        try {
            this.fullname = data.getString("name");

            if(data.has("first_name")){
                this.firstname = data.getString("first_name");
            }

            if(data.has("last_name")){
                this.lastname = data.getString("last_name");
            }

            /*String birthday = "";
            if (jsonObject.has("birthday")) {
                birthday = jsonObject.getString("birthday");
            }
            String gender = "";
            if (jsonObject.has("gender")) {
                gender = jsonObject.getString("gender");
            }
            String city = "";
            if (jsonObject.has("hometown")) {
                city = jsonObject.getJSONObject("hometown").getString("name");
            }*/

            if (data.has("email")) {
                this.email = data.getString("email");
            }

            this.picture = "http://graph.facebook.com/" + this.id + "/picture?type=large";
            //String fbId = jsonObject.getString("id");

        } catch (Exception e) {

        }
    }
}
