package com.example.tripapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {

    EditText editText;
    static StringBuilder stringBuilder = new StringBuilder();
    @NonNull
    @Override
    public Dialog onCreateDialog( @NonNull Bundle savedInstanceState) {
        editText = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.activity_custom_dialog, null);

        builder.setView(editText)
                .setTitle("Add notes")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Log.i("dialog","is working");
                        stringBuilder.append(editText.getText().toString());
                        stringBuilder.append(",");
                        Toast.makeText(getContext(), stringBuilder.toString()+"", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }


    public StringBuilder saveNotes (){
        return stringBuilder;
    }
}
