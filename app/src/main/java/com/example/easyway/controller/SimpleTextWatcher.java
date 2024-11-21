package com.example.easyway.controller;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class SimpleTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Não faz nada por padrão
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Para ser implementado na classe anônima ou filha
    }
    @Override
    public void afterTextChanged(Editable s) {
    }

}

