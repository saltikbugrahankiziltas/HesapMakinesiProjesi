package com.example.proje1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.mariuszgromada.math.mxparser.*;

public class MainActivity extends AppCompatActivity {
    ImageView img1;
    EditText display;
    RelativeLayout r1;

    Button history;
    boolean enSonEsitBasildi=true;
    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img1=findViewById(R.id.ibu_logo);
        r1=findViewById(R.id.r1);
        display=findViewById(R.id.editText);
        display.setShowSoftInputOnFocus(false);//26ve27. satırda editText kısmına tıklanıldığında klavyenin açılmasını önleyecek kodlar yazıldı..
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getString(R.id.editText).equals(display.getText().toString())){
                    display.setText("");
                }
            }
        });

        Animation anim1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.logo);
        Animation anim2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.panel);
        img1.startAnimation(anim1);
        r1.startAnimation(anim2);
        history=findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intt= new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intt);
            }
        });
    }

    public void anyButton(View view){
        if(enSonEsitBasildi){
            display.setText("");
            enSonEsitBasildi=false;
        }
        switch(view.getId()){
            case R.id.AC        : display.setText(""); break;
            case R.id.parantez  :addBrackets();break;
            case R.id.yuzde     :updateDisplay("%");break;
            case R.id.bolum     :updateDisplay("/");break;
            case R.id.yedi      :updateDisplay("7");break;
            case R.id.sekiz     :updateDisplay("8");break;
            case R.id.dokuz     :updateDisplay("9");break;
            case R.id.carpim    :updateDisplay("x");break;
            case R.id.dort      :updateDisplay("4");break;
            case R.id.bes       :updateDisplay("5");break;
            case R.id.altı      :updateDisplay("6");break;
            case R.id.cikarma   :updateDisplay("-");break;
            case R.id.bir       :updateDisplay("1");break;
            case R.id.iki       :updateDisplay("2");break;
            case R.id.uc        :updateDisplay("3");break;
            case R.id.toplama   :updateDisplay("+");break;
            case R.id.sifir     :updateDisplay("0");break;
            case R.id.virgul    :updateDisplay(",");break;
            case R.id.esittir   :calculateTheResult();break;

            
        }

    }

    @SuppressLint("ResourceType")
    private void calculateTheResult() {
        String textDisplay = display.getText().toString();
        String retextDisplay = textDisplay.replaceAll("%","/");
        retextDisplay = textDisplay.replaceAll("x","*");
        Expression ifade=new Expression(retextDisplay);
        String result= String.valueOf(ifade.calculate()).toString();
        if (!result.equals("NaN")) {
            display.setText(result);
            display.setSelection(result.length());
        }
        else {
            showToast("Hatalı Giriş Yaptınız");
        }
        enSonEsitBasildi=true;

    }

    private void showToast(String text) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_layout));
        Toast toast = new Toast(getApplicationContext());
        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText(text);

        toast.setGravity(Gravity.CENTER,0,-200);

        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }



    @SuppressLint("ResourceType")
    private void updateDisplay(String addCharToDisplay) {
        int cursorPos=display.getSelectionStart();
        if (getString(R.id.editText).equals(display.getText().toString())){
            display.setText(addCharToDisplay);
        } else{
            String oldDisplay = display.getText().toString();
            String leftSideOfDisplay  = oldDisplay.substring(0,cursorPos);
            String rightSideOfDisplay =  oldDisplay.substring(cursorPos);
            String newText= leftSideOfDisplay+addCharToDisplay+rightSideOfDisplay;
            display.setText(newText);
            display.setSelection(cursorPos+1);
        }
    }

    private void addBrackets() {
        String textDisplay = display.getText().toString();
        int cursorPos= display.getSelectionStart();
        int countBrackets=0;
        for(int i=0;i<textDisplay.length();i++){
            if(textDisplay.substring(i,i+1).equalsIgnoreCase("("))countBrackets++;
            if(textDisplay.substring(i,i+1).equalsIgnoreCase(")"))countBrackets--;
        }
        String lastCharOfTextDisplay;
        lastCharOfTextDisplay = textDisplay.substring(textDisplay.length()-1);
        if(countBrackets==0 || lastCharOfTextDisplay.equals("(")) updateDisplay("(");
        else if(countBrackets>0 && !lastCharOfTextDisplay.equals(")")) updateDisplay(")");
    }
}