package com.example.cit_up;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import java.text.CollationElementIterator;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainPage extends AppCompatActivity {
    private static final String TAG = "bluetooth2";
    private ConnectedThread mConnectedThread;
    Handler h;

    final int RECIEVE_MESSAGE = 1;
    private StringBuilder sb = new StringBuilder();
    //    private static int flag = 0;
    private ActivityResultLauncher<Intent> resultLauncher;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;



    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address = "00:21:09:00:5A:91";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        TextView textView1 = (TextView) findViewById(R.id.textView1);

        Retrofit retrofit = new Retrofit.Builder()
                //장고 서버와 연결하기 위한 ngrok 주소 입력, ngrok가 실행 될 때마다 주소가 바뀌니 실행 시 주소 변경
                .baseUrl("https://7ef8-1-210-95-42.jp.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Post>> call = jsonPlaceHolderApi.getPost();

        Button SettingsBtn = findViewById(R.id.SettingsBtn);
        SettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                CollationElementIterator textViewResult = null;
                if (!response.isSuccessful()) {
//                    textViewResult.setText("Code: " + response.code());
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
                    content += +post.getVolume() + "%";
                    textView1.setText(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                CollationElementIterator textViewResult = null;
                textView1.setText(t.getMessage());
            }
        });

        //스레드 적용
        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted())
                    try {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //앱 실행 시 포화도
                                Call<List<Post>> call = jsonPlaceHolderApi.getPost();
                                call.enqueue(new Callback<List<Post>>() {
                                    @Override
                                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                                        CollationElementIterator textViewResult = null;
                                        if (!response.isSuccessful()) {
//                                            textViewResult.setText("Code: " + response.code());
                                            return;
                                        }
                                        List<Post> posts = response.body();
//                                        System.out.println("test : " + posts.size());
                                        int index = posts.size() - 1;
                                        Post a = posts.get(index);
                                        float volume = a.getVolume();
//                                        System.out.println("volume : " + volume);
                                        progressBar1.setProgress((int) (volume));


                                        for (Post post : posts) {
                                            String content = "";
                                            content += +post.getVolume() + "%";
                                            textView1.setText(content);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<Post>> call, Throwable t) {
                                        CollationElementIterator textViewResult = null;
                                        textView1.setText(t.getMessage());
                                    }
                                });
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        })).start();


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
//                               textViewResult.setText("Code: " + response.code());
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
////                    content += "ID: " + post.getId() + "\n";
////                    content += "Name: " + post.getName() + "\n";
                            content += +post.getVolume() + "%";
                            textView1.setText(content);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        CollationElementIterator textViewResult = null;
                        textView1.setText(t.getMessage());
                    }
                });

            }
        });


        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            int CallType = intent.getIntExtra("CallType", 0);
                            if (CallType == 0) {
                                //실행될 코드
                            } else if (CallType == 1) {
                                //실행될 코드
                            } else if (CallType == 2) {
                                //실행될 코드
                            }
                        }
                    }
                });
        h = new Handler(Looper.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);
                        int endOfLineIndex = sb.indexOf("\r\n");
                        if (endOfLineIndex > 0) {
                            String sbprint = sb.substring(0, endOfLineIndex);
                            sb.delete(0, sb.length());
                        }
                        break;
                }
            };
        };
        btAdapter=BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        final Button CompressBtn1 = (Button) findViewById(R.id.CompressBtn1);
        CompressBtn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mConnectedThread.write("1");
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainPage.this);
                dlg.setTitle("쓰레기"); //제목
                dlg.setMessage("압축을 시작합니다"); // 메시지
                // 버튼 클릭시 동작
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.show();
            }
        });

    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 19){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
//        return createBluetoothSocket();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "....Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBtIntent.putExtra("CallType", 1);
                resultLauncher.launch(enableBtIntent);
//                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    public class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }

    }


}
