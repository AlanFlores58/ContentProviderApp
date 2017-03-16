package com.example.alanflores.contentproviderapp;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.edit_nombre);
        button = (Button)findViewById(R.id.button_nuevo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put("name",editText.getText().toString());
                Uri uri = getContentResolver().insert(MyProviderf.CONTENT_URI, values);
                Toast.makeText(getBaseContext(), "Nuevo elemento agregado",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
