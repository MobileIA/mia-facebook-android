package com.mobileia.facebook;

import android.app.Activity;
import android.content.Intent;

import com.mobileia.facebook.entity.Profile;
import com.mobileia.facebook.listener.OnSuccessLogin;

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
     * Funcino que se llama una vez logueado al usuario
     * @param profile
     */
    public void processSuccessResponse(Profile profile){
        if(mSuccessListener != null){
            mSuccessListener.onSuccess(profile);
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