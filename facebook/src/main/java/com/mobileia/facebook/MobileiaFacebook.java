package com.mobileia.facebook;

import android.app.Activity;
import android.content.Intent;

import com.mobileia.facebook.activity.InviteGameActivity;
import com.mobileia.facebook.entity.Profile;
import com.mobileia.facebook.listener.OnErrorLogin;
import com.mobileia.facebook.listener.OnInviteComplete;
import com.mobileia.facebook.listener.OnSuccessLogin;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by matiascamiletti on 31/7/17.
 */

public class MobileiaFacebook {
    /**
     * Almacena la Activity que lo inicia
     */
    protected Activity mActivity;
    /**
     * Contiene los permisos que se van a pedir en el login
     */
    protected Collection<String> mPermissions;
    /**
     * Almacena listener para las respuestas correctas
     */
    protected OnSuccessLogin mSuccessListener;
    /**
     * Almacena listener para las respuestas erroneas
     */
    protected OnErrorLogin mErrorListener;
    /**
     * Almacena el listener para las respuestas de invitaciones
     */
    protected OnInviteComplete mInviteListener;
    /**
     * Determina si se esta logueando o no.
     */
    public boolean isLoading = false;
    /**
     * Almacena la unica instancia de la libreria
     */
    private static final MobileiaFacebook sOurInstance = new MobileiaFacebook();

    public void login(){
        // Iniciar activity
        startTranparentActivity();
    }

    /**
     * Funcion para enviar invitaciones
     * @param message
     * @param callback
     */
    public void sendInvitation(String message, OnInviteComplete callback){
        // Guardamos callback
        mInviteListener = callback;
        // Abrimos activity
        InviteGameActivity.startActivity(mActivity, message);
    }

    /**
     * Funcion que se llama una vez logueado al usuario
     * @param profile
     */
    public void processSuccessResponse(Profile profile){
        if(mSuccessListener != null){
            mSuccessListener.onSuccess(profile);
        }
    }

    /**
     * Funcion que se llama si no se pudo loguear
     */
    public void processErrorResponse(String message){
        if(mErrorListener != null){
            mErrorListener.onError(message);
        }
    }

    /**
     * Funcion que se llama si se pudo enviar correctamente las invitaciones
     * @param profiles
     */
    public void processInviteResponse(ArrayList<Profile> profiles){
        if(mInviteListener != null){
            mInviteListener.onSuccess(profiles);
        }
    }
    /**
     * Setea el Activity
     * @param activity
     */
    public void setActivity(Activity activity){
        this.mActivity = activity;
    }

    /**
     * Setea los permisos requeridos
     * @param permissions
     */
    public void setPermissions(Collection<String> permissions){
        this.mPermissions = permissions;
    }

    /**
     * Setea listener cuando se loguea
     * @param listener
     */
    public void setSuccessListener(OnSuccessLogin listener){
        this.mSuccessListener = listener;
    }

    /**
     * Setea el listener cuando no se loguea
     * @param listener
     */
    public void setErrorListener(OnErrorLogin listener){ this.mErrorListener = listener; }

    /**
     * Obtiene los permisos configurados
     * @return
     */
    public Collection<String> getPermissions(){ return this.mPermissions; }
    /**
     * Obtiene la instancia creada
     * @return
     */
    public static MobileiaFacebook getInstance() {
        return sOurInstance;
    }

    protected void startTranparentActivity(){
        Intent intent = new Intent(mActivity, FacebookActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }

    private MobileiaFacebook() {
    }
}