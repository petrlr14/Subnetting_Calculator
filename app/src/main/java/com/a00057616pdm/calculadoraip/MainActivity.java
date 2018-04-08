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

    //Declarando variables a utilizar
    HashMap<Integer, String> netMaskbyBits;
    String[] inputStringArray={"","","","",""};
    EditText inputET1;
    EditText inputET2;
    EditText inputET3;
    EditText inputET4;
    EditText inputETMask;
    TextView textViewCantidadIP;
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

    //Metodo donde se inicializan todos los objetos y se agreagan listener a los editText
    private void init(){
        inputET1=findViewById(R.id.editTextCampo1);
        inputET2=findViewById(R.id.editTextCampo2);
        inputET3=findViewById(R.id.editTextCampo3);
        inputET4=findViewById(R.id.editTextCampo4);
        inputETMask=findViewById(R.id.editTextMask);
        textViewCantidadIP=findViewById(R.id.textViewIPDisponibles);
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

    /*Metodo para agreagar un Listener de textChange, segun objeto mandado como parametro
      el index del parametro es para identificar de que edittext se trata
    */
    private void settingListener(final EditText etInput, final int index){
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            //Despues de que se cambie algo en el edittext se ejecutaran validaciones y calculos
            public void afterTextChanged(Editable s) {
                try{
                    if(etInput.getId()==R.id.editTextMask){//se identifica si el edittext es el de la mask, porque su comportamiento es un poco distinto
                        if(Integer.parseInt(etInput.getText().toString())>32){//se evalua si ha ingresado un numero mayor al permitido
                            Toast toast=Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_number_mask_toast), Toast.LENGTH_LONG);
                            toast.show();//se crea un mensaje de donde se indica que su numero es invalido
                            etInput.setTextColor(getResources().getColor(R.color.badNumber));//se cambia el color del texto para u
                            setBadNumber();//se borran todos los resultados
                        }else{
                            inputStringArray[index]=etInput.getText().toString();//si el numero es correcto se pasa el valor a un array
                            etInput.setTextColor(getResources().getColor(R.color.correctNumber));//si el numero es correcto el color cambia a verde
                            if(!inputStringArray[0].equals("")&&!inputStringArray[1].equals("")&&!inputStringArray[2].equals("")&&!inputStringArray[3].equals("")&&!inputStringArray[4].equals("")){
                                setText();//se agregan los valores calculados
                            }else{
                                setBadNumber();
                            }
                        }
                    }else{
                        if(Integer.parseInt(etInput.getText().toString())>255||Integer.parseInt(etInput.getText().toString())<0){
                            Toast toast=Toast.makeText(getApplicationContext(), getResources().getString(R.string.bad_number_not_mask_toast), Toast.LENGTH_LONG);
                            toast.show();
                            etInput.setTextColor(getResources().getColor(R.color.badNumber));
                            setBadNumber();
                        }else{
                            inputStringArray[index]=etInput.getText().toString();
                            etInput.setTextColor(getResources().getColor(R.color.correctNumber));
                            if(!inputStringArray[0].equals("")&&!inputStringArray[1].equals("")&&!inputStringArray[2].equals("")&&!inputStringArray[3].equals("")&&!inputStringArray[4].equals("")){
                                setText();
                            }else{
                                setBadNumber();
                            }
                        }
                    }
                }catch(Exception e){
                    setBadNumber();//si hay un campo vacío (unica forma de entrar el catch) se eliminan todos los datos
                }
            }
        });
    }

    private String gettingIPDirection(){//se unen cada campo y se forma un string con la direccio ip
        String y="";
        for(int i=0; i<4; i++){
            if(i!=3){
                y+=inputStringArray[i]+getResources().getString(R.string.dot);
            }else{
                y+=inputStringArray[i];
            }
        }
        return y;
    }

    private String gettingIPAvailabe(){//se obtine la cantidad de direcciones disponibles
        int x=32-Integer.parseInt(inputETMask.getText().toString());
        int ipAvailable=Math.abs((int)Math.pow(2, x)-2);
        return String.valueOf(ipAvailable);
    }

    private String gettingBroadcast(){//se obtine broadcast
        String[] ipAddressSplit=gettingIPDirection().split("\\.");//se separa la direccion ip por punto
        String[] maskAddressSplit=netMaskbyBits.get(Integer.parseInt(inputETMask.getText().toString())).split("\\.");//se separa la mascara de red por punto
        String broadcastAddressSplit=""; //variable para guardar la mascara invertida
        int[] ipAddressInt=new int[4];//array con los valores en formato int
        int[] maskAddressInt=new int[4];
        int[] broadcastAddress=new int[4];
        for(int i=0; i<4; i++){//se pasan las direcciones a enteros
            ipAddressInt[i]=Integer.parseInt(ipAddressSplit[i]);
            maskAddressInt[i]=Integer.parseInt(maskAddressSplit[i]);
            broadcastAddress[i]=(0xff&(~maskAddressInt[i])|ipAddressInt[i]);//se obtiene broadcast en enteros
            if(i!=3){//se pasa en formato string para poder mostrar la direccion
                broadcastAddressSplit+=broadcastAddress[i]+".";
            }else{
                broadcastAddressSplit+=broadcastAddress[i];
            }
        }
        return broadcastAddressSplit;
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

    //metodo donde se borra todos los datos a mostrar
    private void setBadNumber(){
        textViewCantidadIP.setText("");
        textViewNetID.setText("");
        textViewBroadcast.setText("");
        textViewParteDeHost.setText("");
        textViewParteDeRed.setText("");
    }
    //metodo donde se setean los datos obetenidos
    private void setText(){
        textViewCantidadIP.setText(gettingIPAvailabe());
        textViewNetID.setText(gettingNetworkAddress());
        textViewBroadcast.setText(gettingBroadcast());
        textViewParteDeHost.setText(gettingHostSide());
        textViewParteDeRed.setText(gettingNetworkAddress());
    }
    //metodo para tener en un hashmap los valores de la mascara de red segun cantidad de bits
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
