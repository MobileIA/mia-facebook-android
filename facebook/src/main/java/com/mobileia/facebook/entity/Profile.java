package com.mobileia.facebook.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class Profile implements Parcelable {
    public String id;
    public String token;

    public String firstname;
    public String lastname;
    public String fullname;
    public String email;
    public String picture;

    public Profile(){}

    public Profile(Parcel in){
        id = in.readString();
        token = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        fullname = in.readString();
        email = in.readString();
        picture = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(token);
        parcel.writeString(firstname);
        parcel.writeString(lastname);
        parcel.writeString(fullname);
        parcel.writeString(email);
        parcel.writeString(picture);
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {

        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

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
