package com.mobileia.facebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.facebook.CallbackManager;

/**
 * Created by matiascamiletti on 13/2/18.
 */

class BaseActivity extends Activity {

    /**
     * Variable que almacena el callback de Facebook
     */
    protected CallbackManager mCallbackManager;

    /**
     * Metodo que se encarga de crear el activity
     * @param savedInstanceState
     * @param persistentState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // Crear manager para facebook
        createCallbackManager();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // Crear manager para facebook
        createCallbackManager();
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

    /**
     * Funcion que se encarga de crear al callback para el login
     */
    protected void createCallbackManager(){
        // Crear manager
        mCallbackManager = CallbackManager.Factory.create();
    }
}
