package com.bornbytes.ctvolleyapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private Button mButtonDo;
    private TextView mTextView;
    private String mUrlString = "https://www.google.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        mButtonDo = (Button) findViewById(R.id.btn_do);
        mTextView = (TextView) findViewById(R.id.tv);

        mButtonDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextView.setText("");

                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        mUrlString,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                mTextView.setText(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
            }
        });
    }
}
