package com.mobileia.facebook.builder;

import android.app.Activity;

import com.mobileia.facebook.MobileiaFacebook;
import com.mobileia.facebook.listener.OnErrorLogin;
import com.mobileia.facebook.listener.OnSuccessLogin;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class LoginBuilder {
    /**
     * Constante que contiene los permisos necesarios para obtener la informacion basica del usuario.
     */
    public static final Collection<String> PERMISSIONS_WITH_INFO = Arrays.asList("public_profile", "email");
    /**
     * Constante que contiene los permisos necesarios para obtener la informacion basica del usuario y los amigos.
     */
    public static final Collection<String> PERMISSIONS_WITH_INFO_AND_FRIENDS = Arrays.asList("public_profile", "email", "user_friends");

    /**
     * Configura el activity
     * @param activity
     * @return
     */
    public LoginBuilder withActivity(Activity activity){
        MobileiaFacebook.getInstance().setActivity(activity);
        return this;
    }
    /**
     * Metodo que registra los permisos que se van a utilizar
     * @param permissions
     * @return
     */
    public LoginBuilder withPermissions(Collection<String> permissions){
        MobileiaFacebook.getInstance().setPermissions(permissions);
        return this;
    }
    /**
     * Configura el manejador cuando el usuario se logueado correctamente
     * @param listener
     * @return
     */
    public LoginBuilder withSuccessResult(OnSuccessLogin listener){
        MobileiaFacebook.getInstance().setSuccessListener(listener);
        return this;
    }

    /**
     * Configura el manejador para cuando el usuario no se pudo loguear
     * @param listener
     * @return
     */
    public LoginBuilder withErrorResult(OnErrorLogin listener){
        MobileiaFacebook.getInstance().setErrorListener(listener);
        return this;
    }

    /**
     * Ejecuta el login a traves de facebook
     */
    public void build(){
        MobileiaFacebook.getInstance().login();
    }
}
