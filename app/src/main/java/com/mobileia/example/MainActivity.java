package com.mobileia.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mobileia.facebook.MobileiaFacebook;
import com.mobileia.facebook.builder.LoginBuilder;
import com.mobileia.facebook.entity.Profile;
import com.mobileia.facebook.listener.OnErrorLogin;
import com.mobileia.facebook.listener.OnInviteComplete;
import com.mobileia.facebook.listener.OnSuccessLogin;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickFacebook(View view){
        new LoginBuilder()
                .withActivity(this)
                .withPermissions(LoginBuilder.PERMISSIONS_WITH_INFO_AND_FRIENDS)
                .withSuccessResult(new OnSuccessLogin() {
                    @Override
                    public void onSuccess(Profile profile) {
                        System.out.println("Se logueo correctamente");

                        System.out.println("USER: " + profile.id);
                        System.out.println("USER: " + profile.token);
                        System.out.println("USER: " + profile.firstname);
                        System.out.println("USER: " + profile.lastname);
                        System.out.println("USER: " + profile.email);
                        System.out.println("USER: " + profile.picture);
                    }
                })
                .withErrorResult(new OnErrorLogin() {
                    @Override
                    public void onError(String message) {
                        System.out.println("NO se logueo: " + message);
                    }
                })
                .build();
    }

    public void onClickInviteGame(View view){
        MobileiaFacebook.getInstance().setActivity(this);
        MobileiaFacebook.getInstance().sendInvitation("Invitación para ShowProde:", new OnInviteComplete() {
            @Override
            public void onSuccess(ArrayList<Profile> profiles) {
                for(Profile profile : profiles){
                    System.out.println("INVITACION: " + profile.firstname);
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
