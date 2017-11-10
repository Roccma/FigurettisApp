package com.mauro.figurettisapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by usuario on 1/10/2017.
 */

public class LoginResponse {
    @SerializedName("result")
    private boolean result;

    public LoginResponse(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
