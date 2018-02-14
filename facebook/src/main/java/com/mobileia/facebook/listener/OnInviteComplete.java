package com.mobileia.facebook.listener;

import com.mobileia.facebook.entity.Profile;

import java.util.ArrayList;

/**
 * Created by matiascamiletti on 13/2/18.
 */

public interface OnInviteComplete {
    void onSuccess(ArrayList<Profile> profiles);
    void onError(String message);
}
