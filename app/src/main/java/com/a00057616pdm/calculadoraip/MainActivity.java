package com.a00057616pdm.calculadoraip;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<Integer, String> netMaskbyBits;
    String[] inputStringArray={"","","","",""};
    EditText inputET1;
    EditText inputET2;
    EditText inputET3;
    EditText inputET4;
    EditText inputETMask;
    TextView tx;
    TextView textViewNetID;
    TextView textViewBroadcast;
    TextView textViewParteDeHost;
    TextView textViewParteDeRed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        inputET1=findViewById(R.id.editTextCampo1);
        inputET2=findViewById(R.id.editTextCampo2);
        inputET3=findViewById(R.id.editTextCampo3);
        inputET4=findViewById(R.id.editTextCampo4);
        inputETMask=findViewById(R.id.editTextMask);
        tx=findViewById(R.id.textViewIPDisponibles);
        textViewNetID=findViewById(R.id.textViewNetID);
        textViewBroadcast=findViewById(R.id.textViewBroadcast);
        textViewParteDeHost=findViewById(R.id.textViewParteDeHost);
        textViewParteDeRed=findViewById(R.id.textViewParteDeRed);
        netMaskbyBits=new HashMap<Integer, String>();
        settingNetMask();
        settingListener(inputET1, 0);
        settingListener(inputET2, 1);
        settingListener(inputET3, 2);
        settingListener(inputET4, 3);
        settingListener(inputETMask, 4);

    }

    private void settingListener(final EditText etInput, final int index){
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try{
                    if(etInput.getId()==R.id.editTextMask){
                        if(Integer.parseInt(etInput.getText().toString())>32){
                            Toast toast=Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_number_mask_toast), Toast.LENGTH_LONG);
                            toast.show();
                            etInput.setTextColor(getResources().getColor(R.color.badNumber));
                            tx.setText("");
                            textViewNetID.setText("");
                            textViewBroadcast.setText("");
                            textViewParteDeHost.setText("");
                            textViewParteDeRed.setText("");
                        }else{
                            inputStringArray[index]=etInput.getText().toString();
                            etInput.setTextColor(getResources().getColor(R.color.correctNumber));
                            if(!inputStringArray[0].equals("")&&!inputStringArray[1].equals("")&&!inputStringArray[2].equals("")&&!inputStringArray[3].equals("")&&!inputStringArray[4].equals("")){
                                tx.setText(gettingIPAvailabe());
                                textViewNetID.setText(gettingNetworkAddress());
                                textViewBroadcast.setText(gettingBroadcast());
                                textViewParteDeHost.setText(gettingHostSide());
                                textViewParteDeRed.setText(gettingNetworkAddress());
                            }else{
                                tx.setText("");
                                textViewNetID.setText("");
                                textViewBroadcast.setText("");
                                textViewParteDeHost.setText("");
                                textViewParteDeRed.setText("");
                            }
                        }
                    }else{
                        if(Integer.parseInt(etInput.getText().toString())>255||Integer.parseInt(etInput.getText().toString())<0){
                            Toast toast=Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_number_not_mask_toast), Toast.LENGTH_LONG);
                            toast.show();
                            etInput.setTextColor(getResources().getColor(R.color.badNumber));
                        }else{
                            inputStringArray[index]=etInput.getText().toString();
                            etInput.setTextColor(getResources().getColor(R.color.correctNumber));
                            if(!inputStringArray[0].equals("")&&!inputStringArray[1].equals("")&&!inputStringArray[2].equals("")&&!inputStringArray[3].equals("")&&!inputStringArray[4].equals("")){
                                tx.setText(gettingIPAvailabe());
                                textViewNetID.setText(gettingNetworkAddress());
                                textViewBroadcast.setText(gettingBroadcast());
                                textViewParteDeHost.setText(gettingHostSide());
                                textViewParteDeRed.setText(gettingNetworkAddress());
                            }else{
                                tx.setText("");
                                textViewNetID.setText("");
                                textViewBroadcast.setText("");
                                textViewParteDeHost.setText("");
                                textViewParteDeRed.setText("");
                            }
                        }
                    }
                }catch(Exception e){
                    tx.setText("");
                    textViewNetID.setText("");
                    textViewBroadcast.setText("");
                    textViewParteDeHost.setText("");
                    textViewParteDeRed.setText("");
                }
            }
        });
    }

    private String gettingIPDirection(){
        String y="";
        for(int i=0; i<4; i++){
            if(i!=3){
                y+=inputStringArray[i]+".";
            }else{
                y+=inputStringArray[i];
            }
        }
        return y;
    }

    private String gettingIPAvailabe(){
        int x=32-Integer.parseInt(inputETMask.getText().toString());
        int ipAvailable=Math.abs((int)Math.pow(2, x)-2);
        return String.valueOf(ipAvailable);
    }

    private String gettingBroadcast(){
        String[] ipAddressSplit=gettingIPDirection().split("\\.");
        String[] maskAddressSplit=netMaskbyBits.get(Integer.parseInt(inputETMask.getText().toString())).split("\\.");
        String invertedMaskAddressSplit="";
        int[] ipAddressInt=new int[4];
        int[] maskAddressInt=new int[4];
        int[] invertedMaskAddress=new int[4];
        for(int i=0; i<4; i++){
            ipAddressInt[i]=Integer.parseInt(ipAddressSplit[i]);
            maskAddressInt[i]=Integer.parseInt(maskAddressSplit[i]);
            invertedMaskAddress[i]=(0xff&(~maskAddressInt[i])|ipAddressInt[i]);
            if(i!=3){
                invertedMaskAddressSplit+=invertedMaskAddress[i]+".";
            }else{
                invertedMaskAddressSplit+=invertedMaskAddress[i];
            }
        }
        return invertedMaskAddressSplit;
    }

    private String gettingNetworkAddress(){
        String[] ipAddressSplit=gettingIPDirection().split("\\.");
        String[] maskAddressSplit=netMaskbyBits.get(Integer.parseInt(inputETMask.getText().toString())).split("\\.");
        String networkAddressSplit="";
        int[] ipAddressInt=new int[4];
        int[] maskAddressInt=new int[4];
        int[] networkAddressInt=new int[4];

        for(int i=0; i<4; i++){
            ipAddressInt[i]=Integer.parseInt(ipAddressSplit[i]);
            maskAddressInt[i]=Integer.parseInt(maskAddressSplit[i]);
            networkAddressInt[i]=ipAddressInt[i]&maskAddressInt[i];
            if(i!=3){
                networkAddressSplit+=networkAddressInt[i]+".";
            }else{
                networkAddressSplit+=networkAddressInt[i];
            }
        }
        return networkAddressSplit;
    }


    private String gettingHostSide(){
        String[] ipAddressSplit=gettingIPDirection().split("\\.");
        String[] maskAddressSplit=netMaskbyBits.get(Integer.parseInt(inputETMask.getText().toString())).split("\\.");
        String invertedMaskAddressSplit="";
        int[] ipAddressInt=new int[4];
        int[] maskAddressInt=new int[4];
        int[] invertedMaskAddress=new int[4];
        for(int i=0; i<4; i++){
            ipAddressInt[i]=Integer.parseInt(ipAddressSplit[i]);
            maskAddressInt[i]=Integer.parseInt(maskAddressSplit[i]);
            invertedMaskAddress[i]=((~maskAddressInt[i])&ipAddressInt[i]);
            if(i!=3){
                invertedMaskAddressSplit+=invertedMaskAddress[i]+".";
            }else{
                invertedMaskAddressSplit+=invertedMaskAddress[i];
            }
        }
        return invertedMaskAddressSplit;
    }

    private void setBadNumber(){

    }

    private void settingNetMask() {
        netMaskbyBits.put(0,"0.0.0.0");
        netMaskbyBits.put(1,"128.0.0.0");
        netMaskbyBits.put(2,"192.0.0.0");
        netMaskbyBits.put(3,"224.0.0.0");
        netMaskbyBits.put(4,"240.0.0.0");
        netMaskbyBits.put(5,"248.0.0.0");
        netMaskbyBits.put(6,"252.0.0.0");
        netMaskbyBits.put(7,"254.0.0.0");
        netMaskbyBits.put(8,"255.0.0.0");
        netMaskbyBits.put(9,"255.128.0.0");
        netMaskbyBits.put(10,"255.192.0.0");
        netMaskbyBits.put(11,"255.224.0.0");
        netMaskbyBits.put(12,"255.240.0.0");
        netMaskbyBits.put(13,"255.248.0.0");
        netMaskbyBits.put(14,"255.252.0.0");
        netMaskbyBits.put(15,"255.254.0.0");
        netMaskbyBits.put(16,"255.255.0.0");
        netMaskbyBits.put(17,"255.255.128.0");
        netMaskbyBits.put(18,"255.255.192.0");
        netMaskbyBits.put(19,"255.255.224.0");
        netMaskbyBits.put(20,"255.255.240.0");
        netMaskbyBits.put(21,"255.255.248.0");
        netMaskbyBits.put(22,"255.255.252.0");
        netMaskbyBits.put(23,"255.255.254.0");
        netMaskbyBits.put(24,"255.255.255.0");
        netMaskbyBits.put(25,"255.255.255.128");
        netMaskbyBits.put(26,"255.255.255.192");
        netMaskbyBits.put(27,"255.255.255.224");
        netMaskbyBits.put(28,"255.255.255.240");
        netMaskbyBits.put(29,"255.255.255.248");
        netMaskbyBits.put(30,"255.255.255.252");
        netMaskbyBits.put(31,"255.255.255.254");
        netMaskbyBits.put(32,"255.255.255.255");
    }


}
