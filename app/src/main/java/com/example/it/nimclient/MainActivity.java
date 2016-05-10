package com.example.it.nimclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    EditText nickname;
    class Register {
        public String status;
        public int player;

        public Register(String status, int player) {
            this.status = status;
            this.player = player;
        }
    }
    class User {
        public String action;
        public String nickname;

        public User(String action, String nickname) {
            this.action = action;
            this.nickname = nickname;
        }
    }

    interface JsonServer {
        @POST("/")
        Call<Register> register(@Body User user);
    }

    public static final String API_URL = "http://194.176.114.21:8048/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nickname = (EditText) findViewById(R.id.nickname);

    }

    public void onClickRegister(View v)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        JsonServer jsonServer = retrofit.create(JsonServer.class);

        Call<Register> call = jsonServer.register(new User("register", nickname.getText().toString()));

        final Button registerbutton = (Button) findViewById(R.id.register);

        registerbutton.setEnabled(false);

        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, retrofit2.Response<Register> response) {
                Register register = response.body();
                Toast.makeText(MainActivity.this, "status: " + register.status
                        + ", id: " + register.player, Toast.LENGTH_SHORT).show();

                registerbutton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error:"+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.w("DEBUG", t.getMessage());
                registerbutton.setEnabled(true);
            }
        });
    }




}
