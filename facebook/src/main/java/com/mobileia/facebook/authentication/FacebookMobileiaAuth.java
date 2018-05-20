package com.mobileia.facebook.authentication;

import android.app.Activity;

import com.google.gson.JsonObject;
import com.mobileia.authentication.core.MobileiaAuthBase;
import com.mobileia.authentication.core.rest.AuthRestBase;
import com.mobileia.facebook.builder.LoginBuilder;
import com.mobileia.facebook.entity.Profile;
import com.mobileia.facebook.listener.OnErrorLogin;
import com.mobileia.facebook.listener.OnSuccessLogin;
import com.mobileia.core.entity.Error;

import java.util.Collection;

public class FacebookMobileiaAuth extends MobileiaAuthBase {

    /**
     * Almacena el token del usuario
     */
    protected String mFacebookId;
    /**
     * Almacena el secret del usuario
     */
    protected String mFacebookAccessToken;
    /**
     * Almacenamos los permisos para pedir en Facebook
     */
    protected Collection<String> mPermissions = LoginBuilder.PERMISSIONS_WITH_INFO;

    /**
     * Constructor
     *
     * @param activity
     */
    public FacebookMobileiaAuth(Activity activity) {
        super(activity);
    }

    /**
     * Setea los permisos que se requieren
     * @param permissions
     */
    public void setPermissions(Collection<String> permissions){
        this.mPermissions = permissions;
    }

    @Override
    public void requestAccessToken() {
        // Generamos objeto para enviar los parametros
        JsonObject params = new JsonObject();
        params.addProperty("grant_type", "facebook");
        params.addProperty("facebook_id", mFacebookId);
        params.addProperty("facebook_access_token", mFacebookAccessToken);
        new AuthRestBase().oauth(params, mAccessTokenResult);
    }

    @Override
    public void requestNewAccount() {
        // Generamos objeto para enviar los parametros
        JsonObject params = new JsonObject();
        params.addProperty("register_type", "facebook");
        params.addProperty("facebook_id", mFacebookId);
        params.addProperty("facebook_access_token", mFacebookAccessToken);
        new AuthRestBase().register(params, mRegisterResult);
    }

    @Override
    public void openSocial() {
        new LoginBuilder()
                .withActivity(mActivity)
                .withPermissions(mPermissions)
                .withSuccessResult(new OnSuccessLogin() {
                    @Override
                    public void onSuccess(Profile profile) {
                        // Guardamos datos del token
                        mFacebookId = profile.id;
                        mFacebookAccessToken = profile.token;
                        // Ya se logueo con Facebook, realizamos peticion para generar AccessToken
                        requestAccessToken();
                    }
                })
                .withErrorResult(new OnErrorLogin() {
                    @Override
                    public void onError(String message) {
                        // Llamamos al callback con error
                        mCallback.onError(new Error(-1, "No se pudo loguear con la cuenta de Facebook."));
                    }
                })
                .build();
    }
}
