package com.example.cit_up;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.CollationElementIterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainPage extends AppCompatActivity {
    private TextView textViewResult;
    private Button loadingBtn;

//    private DrawerLayout drawerLayout;
//    private View drawerView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        ProgressBar progressBar1 = (ProgressBar)findViewById(R.id.progressBar1);
//        ProgressBar progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);
//        ProgressBar progressBar3 = (ProgressBar)findViewById(R.id.progressBar3);



        TextView textView1 = (TextView) findViewById(R.id.textView1);
//        TextView textView2 = (TextView) findViewById(R.id.textView2);
//        TextView textView3 = (TextView) findViewById(R.id.textView3);

        Retrofit retrofit = new Retrofit.Builder()
                //장고 서버와 연결하기 위한 ngrok 주소 입력, ngrok가 실행 될 때마다 주소가 바뀌니 실행 시 주소 변경
                .baseUrl("https://e22c-119-201-40-146.jp.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //앱 실행 시 포화도
        Call<List<Post>> call = jsonPlaceHolderApi.getPost();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                CollationElementIterator textViewResult = null;
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                List<Post> posts = response.body();
                System.out.println("test : " + posts.size());
                int index = posts.size() - 1;
                Post a = posts.get(index);
                float volume = a.getVolume();
                System.out.println("volume : " + volume);
                progressBar1.setProgress((int) (volume));

                for (Post post : posts) {
                    String content = "";
                    content += + post.getVolume() + "%";
                    textView1.setText(content);
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                CollationElementIterator textViewResult = null;
                textView1.setText(t.getMessage());
            }
        });

        //버튼 클릭 시 쓰레기통 내 포화도 값 불러옴
        Button loadingBtn = findViewById(R.id.loadingBtn);
        loadingBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Call<List<Post>> call = jsonPlaceHolderApi.getPost();

                   call.enqueue(new Callback<List<Post>>() {
                       @Override
                       public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                           CollationElementIterator textViewResult = null;
                           if (!response.isSuccessful()) {
                               textViewResult.setText("Code: " + response.code());
                               return;
                           }

                           List<Post> posts = response.body();
                           System.out.println("test : "+posts.size());
                           int index = posts.size()-1;
                           Post a = posts.get(index);
                           float volume = a.getVolume();
                           System.out.println("volume : "+volume);
                           progressBar1.setProgress((int)(volume));

                           for (Post post : posts) {
                               String content = "";
////                    content += "ID: " + post.getId() + "\n";
////                    content += "Name: " + post.getName() + "\n";
                               content += + post.getVolume() + "%";
////                    content += "Date: " + post.getDate() + "\n\n";
//                               float volume = post.getVolume();
//                               progressBar1.setProgress((int)(volume));
////                    progressBar2.setProgress((int)(volume));
////                    progressBar3.setProgress((int)(volume));
//
                      textView1.setText(content);
////                    textView2.setText(content);
////                    textView3.setText(content);
                           }
                       }

                       @Override
                       public void onFailure(Call<List<Post>> call, Throwable t) {
                           CollationElementIterator textViewResult = null;
                           textView1.setText(t.getMessage());
//                textView2.setText(t.getMessage());
//                textView3.setText(t.getMessage());
                       }
                   });

               }
           });











        Button SettingsBtn = findViewById(R.id.SettingsBtn);
        SettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Settings.class);
                startActivity(intent);
            }
        });

        final Button CompressBtn1 = (Button) findViewById(R.id.CompressBtn1);
        CompressBtn1.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainPage.this);
                dlg.setTitle("쓰레기"); //제목
                dlg.setMessage("압축을 시작합니다"); // 메시지
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.show();
            }
        });

//        final Button CompressBtn2 = (Button) findViewById(R.id.CompressBtn2);
//        CompressBtn2.setOnClickListener(new View.OnClickListener(){
//
//            public void onClick(View view){
//
//                AlertDialog.Builder dlg = new AlertDialog.Builder(MainPage.this);
//                dlg.setTitle("캔"); //제목
//                dlg.setMessage("캔 압축을 시작합니다"); // 메시지
////                버튼 클릭시 동작
//                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                dlg.show();
//            }
//        });

//        final Button CompressBtn3 = (Button) findViewById(R.id.CompressBtn3);
//        CompressBtn3.setOnClickListener(new View.OnClickListener(){
//
//            public void onClick(View view){
//
//                AlertDialog.Builder dlg = new AlertDialog.Builder(MainPage.this);
//                dlg.setTitle("일반쓰레기"); //제목
//                dlg.setMessage("일반쓰레기 압축을 시작합니다"); // 메시지
////                버튼 클릭시 동작
//                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                dlg.show();
//            }
//        });

    }


}
