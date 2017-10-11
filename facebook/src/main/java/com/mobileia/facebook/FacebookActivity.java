package com.mobileia.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mobileia.facebook.entity.Profile;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class FacebookActivity extends Activity implements FacebookCallback<LoginResult> {
    /**
     * Variable que almacena el callback de Facebook
     */
    protected CallbackManager mCallbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // Crear manager para facebook
        createCallbackManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificamos si ya se ejecuto el login
        if(MobileiaFacebook.getInstance().isLoading){
            return;
        }
        // Seteamos login
        MobileiaFacebook.getInstance().isLoading = true;
        // Iniciamos login con Facebook
        LoginManager.getInstance().logInWithReadPermissions(this, MobileiaFacebook.getInstance().getPermissions());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Verificamos si el callback fue creado
        if(mCallbackManager == null){
            createCallbackManager();
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        // Pedimos los datos del usuario
        requestInformation(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        // Llamamos al listener
        MobileiaFacebook.getInstance().processErrorResponse("Se ha cancelado la solicitud.");
        // Finalizamos el login
        finished();
    }

    @Override
    public void onError(FacebookException error) {
        // Llamamos al listener
        MobileiaFacebook.getInstance().processErrorResponse(error.getMessage());
        // Finalizamos el login
        finished();
    }

    /**
     * Funcion que genera el request para obtener los datos del usuario
     * @param accessToken
     */
    protected void requestInformation(final AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                // Creamos usuario
                Profile profile = new Profile();
                profile.id = accessToken.getUserId();
                profile.token = accessToken.getToken();
                profile.fill(jsonObject);
                // Llamamos al listener
                MobileiaFacebook.getInstance().processSuccessResponse(profile);
                // Finalizamos el login
                finished();
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }
    /**
     * Funcion que se ejecuta una vez obtenido resultado del login
     */
    protected void finished(){
        // Seteamos que se termino el login
        MobileiaFacebook.getInstance().isLoading = false;
        // Cerramos activity
        finish();
    }
    /**
     * Funcion que se encarga de crear al callback para el login
     */
    protected void createCallbackManager(){
        // Crear manager
        mCallbackManager = CallbackManager.Factory.create();
        // Crear callbacks
        LoginManager.getInstance().registerCallback(mCallbackManager, this);
    }

}