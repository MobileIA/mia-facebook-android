package com.mobileia.facebook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.widget.GameRequestDialog;
import com.mobileia.facebook.MobileiaFacebook;
import com.mobileia.facebook.entity.Profile;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by matiascamiletti on 13/2/18.
 */

public class InviteGameActivity extends BaseActivity implements FacebookCallback<GameRequestDialog.Result> {

    /**
     * Constante que representa el texto enviado en la invitacion.
     */
    public static final String EXTRA_MESSAGE = "com.mobileia.facebook.activity.EXTRA_MESSAGE";

    /**
     * Almacena el dialogo para invitar
     */
    protected GameRequestDialog mRequestDialog;
    /**
     * Determina si ya se ha abierto el dialogo
     */
    protected boolean isInviting = false;
    /**
     * Mensaje que ira en la invitacion.
     */
    protected String mMessage = "Se ha enviado una invitacion";
    /**
     * Guardada el Token de facebook del usuario
     */
    protected AccessToken mToken;
    /**
     * Alamacena el total de usuario seleccionados
     */
    protected int mTotal = 0;
    /**
     * Almacena los usuarios seleccionados ya procesados
     */
    protected ArrayList<Profile> mProfiles = new ArrayList<Profile>();

    /**
     * Funcion que crea el Manager
     */
    @Override
    protected void createCallbackManager() {
        super.createCallbackManager();
        // Procesar parametros
        processExtras();
        // Crear dialogo
        mRequestDialog = new GameRequestDialog(this);
        // Registar callback
        mRequestDialog.registerCallback(mCallbackManager, this);
        System.out.println("Se ha creado el manager.");
        // seteamos que el dialogo todavia no se abrio
        isInviting = false;
        // Obtener AccessToken por si ya esta generado
        mToken = AccessToken.getCurrentAccessToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificamos si el dialogo se abrio
        if(isInviting){
            return;
        }
        // Abrir dialogo
        showDialog();
    }

    @Override
    public void onSuccess(GameRequestDialog.Result result) {
        // Guardamos el total de seleccionados
        mTotal = result.getRequestRecipients().size();
        // Creamos Batch para buscar la informacion de los usuarios
        GraphRequestBatch batch = new GraphRequestBatch();
        ArrayList<GraphRequest> requests = new ArrayList<GraphRequest>();
        // recoremos todos los Usuarios invitados
        for(String ids : result.getRequestRecipients()){
            // Agregamos La request al batch
            requests.add(fetchProfile(ids));
        }
        // Agregamos al batch
        batch.addAll(requests);
        // Ejecutamos Batch
        batch.executeAsync();
    }

    @Override
    public void onCancel() {
        // Cerramos Activity
        finish();
    }

    @Override
    public void onError(FacebookException error) {
        // Cerramos Activity
        finish();
    }

    /**
     * Funcion para obtener los datos del usuario invitado
     * @param facebookId
     */
    protected GraphRequest fetchProfile(final String facebookId){
        GraphRequest request = GraphRequest.newGraphPathRequest(mToken, "/" + facebookId, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                // Creamos usuario
                Profile profile = new Profile();
                profile.id = facebookId;
                profile.token = "";
                profile.fill(response.getJSONObject());
                // Agregamos al listado
                mProfiles.add(profile);
                // Restamos del total
                mTotal--;
                // verificamos si ya termino
                if(mTotal == 0){
                    // Llamamos al listener
                    MobileiaFacebook.getInstance().processInviteResponse(mProfiles);
                    // Cerramos pantalla
                    finish();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,first_name,last_name");
        request.setParameters(parameters);
        return request;
    }

    /**
     * metodo que ejecuta el dialogo para invitar a los amigos
     */
    protected void showDialog(){
        GameRequestContent content = new GameRequestContent.Builder()
                .setMessage(mMessage)
                .build();
        mRequestDialog.show(content);
        // Setear que se abrio el dialogo
        isInviting = true;
    }
    /**
     * Procesa los parametros enviados para customizar
     */
    protected void processExtras(){
        // Verificamos que haya parametros
        if (getIntent().getExtras() == null || getIntent().getExtras().isEmpty()) {
            // Cerrar pantalla
            finish();
            return;
        }
        // obtenemos el mensaje para la invitacion
        mMessage = getIntent().getExtras().getString(EXTRA_MESSAGE);
    }

    /**
     * Inicia la pantalla con los parametros requeridos para devolver los datos
     * @param activity
     * @param message
     */
    public static void startActivity(Activity activity, String message){
        Intent intent = new Intent(activity, InviteGameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
