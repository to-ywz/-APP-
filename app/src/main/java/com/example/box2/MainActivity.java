package com.example.box2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //控件声明
    private EditText Send;              //物品输入框
    private Button CLEAR;               //清空列表
    private Button IPSure;              //IP确认
    private Button light;               //点灯
    private Button DataSure;            //物品存储确认键
    private EditText IpInput;           //IP输入框
    private TextView TCP;               //输入提示框
    private TextView Test;
    private TextView ID;                //显示ID
    private Spinner spinner;            //下拉列表


    //变量声明
    /*socket*/
    private Socket socket = null;
    /*连接线程*/
    private Thread connectThread;
    private Timer timer = new Timer();
    private OutputStream outputStream;
    private int index = 1;
    private String ip = "192.168.4.1";
    private int port = 8080;
    private TimerTask task;
    private  boolean isConnect = false;
    /*默认重连*/
    private boolean isReConnect = true;

    private String name;
    private boolean IsEmpty = true;     //BoxNameIndex索引最大值
    private ArrayList<String> BoxName = new ArrayList<>();
    //private String IP;                  //存储IP
    private String SendText;            //存储发送文本
    private Boolean IsConfirmIP = false;         //确认IP已输入
    //private Thread thread;
    private ArrayAdapter<String> adapter;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onDestroy(){
        super.onDestroy();
        SaveToData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //BoxName = getArrayList(MainActivity.this);
        LoadToList();
        if (BoxName.isEmpty()){
            InitBoxName();
        }

        //控件变量与相应控件类建立连接
        CLEAR = (Button)findViewById(R.id.CLEAR);
        IPSure = (Button)findViewById(R.id.IPSure);
        DataSure = (Button)findViewById(R.id.DataSure);
        light = (Button)findViewById(R.id.light);
        Send = (EditText)findViewById(R.id.Send);
        IpInput = (EditText)findViewById(R.id.IpInput);
        TCP = (TextView)findViewById(R.id.TCP);
        Test = (TextView)findViewById(R.id.Test);
        spinner = (Spinner)findViewById(R.id.spinner);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, BoxName);
        spinner.setAdapter(adapter);
        ((ArrayAdapter)spinner.getAdapter()).notifyDataSetChanged();


        Send.setText("");
        IPSure.setText("连接");

        //存入数据库
        DataSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Send.getText().toString().equals(" ")) {
                    SavetheData();
                    spinner.setSelection(index);
                } else {
                    toastMsg("请输入正确物品");
                }
                Send.setText("");
            }
        });

        CLEAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoxName.clear();
                InitBoxName();
                spinner.setAdapter(adapter);
            }
        });

        //选择物品
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        IPSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isConnect) {
                    initSocket();
                }
                else {
                    IPSure.setText("熄灭");
                    SendText = spinner.getSelectedItem().toString();
                    if (BoxName.contains(SendText)) {
                        SendText = "n" + (55-BoxName.indexOf(SendText));
                        sendOrder(SendText);
                    } else {
                        toastMsg("物品不存在！");
                    }
                }
            }
        });

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendText = spinner.getSelectedItem().toString();
                if (BoxName.contains(SendText)) {
                    SendText = "o" + (55-BoxName.indexOf(SendText));
                    //Test.setText(SendText);
                    sendOrder(SendText);
                } else {
                    toastMsg("物品不存在！");
                }
            }
        });
    }













    public void Delete(int Index){
        BoxName.remove(Index);
    }
    //存入List
    public void SavetheData(){
        boolean isinclude = false;
        name = Send.getText().toString();
        String temp = name;
        SendText = spinner.getSelectedItem().toString();
        index = BoxName.indexOf(SendText);
        //Test.setText(index+"heiheihei");
        name = index+1 + "." + Send.getText().toString();
        for (int i = 0; i < BoxName.size(); i++) {
            if (BoxName.get(i).equals((i+1)+"."+temp)){
                isinclude = true;
            }
        }
        if (isinclude){
            toastMsg("物品已存在");
            spinner.setSelection(index);
        } else {
            BoxName.set(index,name);
        }
        spinner.setAdapter(adapter);
    }
    //写入文件
    public void SaveToData(){
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;

        try{
            fileOutputStream = openFileOutput("data", Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            for (int i = 0; i < BoxName.size(); i++) {
                bufferedWriter.write(BoxName.get(i) + '\n');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void LoadToList(){
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        try{
            fileInputStream = openFileInput("data");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                BoxName.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null){
                try{
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }












    //Socket连接

    /*因为Toast是要运行在主线程的   所以需要到主线程哪里去显示toast*/
    private void toastMsg(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void initSocket() {
        if (socket == null && connectThread == null) {
            connectThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    socket = new Socket();
                    try {
                        /*超时时间为2秒*/
                        socket.connect(new InetSocketAddress(ip, 8080), 2000);
                        /*连接成功的话  发送心跳包*/
                        if (socket.isConnected()) {


                            /*因为Toast是要运行在主线程的  这里是子线程  所以需要到主线程哪里去显示toast*/
                            toastMsg("socket已连接");
                            isConnect = true;
                            /*发送心跳数据*/
                            sendBeatData();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                        if (e instanceof SocketTimeoutException) {
                            toastMsg("连接超时，正在重连");

                            releaseSocket();

                        } else if (e instanceof NoRouteToHostException) {
                            toastMsg("该地址不存在，请检查");
                            //stopSelf();

                        } else if (e instanceof ConnectException) {
                            toastMsg("连接异常或被拒绝，请检查");
                            //stopSelf();
                        }
                    }
                }
            });
            /*启动连接线程*/
            connectThread.start();
        }
    }


    /*发送数据*/
    public void sendOrder(final String order) {
        if (socket != null && socket.isConnected()) {
            /*发送指令*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        outputStream = socket.getOutputStream();
                        if (outputStream != null) {
                            outputStream.write((order).getBytes("gbk"));
                            outputStream.flush();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        } else {
            toastMsg("socket连接错误,请重试");
        }
    }


    /*定时发送数据*/
    private void sendBeatData() {
        if (timer == null) {
            timer = new Timer();
        }

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        outputStream = socket.getOutputStream();

                        /*这里的编码方式根据你的需求去改*/
                        outputStream.write(("qute").getBytes("gbk"));
                        outputStream.flush();
                    } catch (Exception e) {
                        /*发送失败说明socket断开了或者出现了其他错误*/
                        toastMsg("连接断开，正在重连");
                        /*重连*/
                        releaseSocket();
                        e.printStackTrace();
                    }
                }
            };
        }

        timer.schedule(task, 0, 2000);
    }


    private void releaseSocket() {

        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }

        if (outputStream != null) {
            try {
                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }

        if (socket != null) {
            try {
                socket.close();

            } catch (IOException e) {
            }
            socket = null;
        }

        if (connectThread != null) {
            connectThread = null;
        }

        /*重新初始化socket*/
        if (isReConnect) {
            if (isConnect) {
                initSocket();
            }
        }

    }









    public void InitBoxName(){
        for (int i = 0; i < 55; i++){
            BoxName.add((i+1)+".");
        }
    }







































    /**
     * 软键盘自动隐藏
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
