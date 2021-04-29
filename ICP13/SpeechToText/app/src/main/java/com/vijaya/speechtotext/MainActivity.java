package com.vijaya.speechtotext;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.jar.Attributes;
import java.util.prefs.Preferences;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv;
    private ImageButton mSpeakBtn;
    TextToSpeech tts;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public static final String MyPREFERENCES = "myprefs";
    public static final String value = "key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        preferences=getSharedPreferences("Maneesha", Context.MODE_PRIVATE);


        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.US);
                    tts.speak("Hello", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        editor = preferences.edit();

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(result != null && result.size()>0){
                        mVoiceInputTv.append(Html.fromHtml("<p style=\"color:black;\">User : "+result.get(0)+"</p>"));

                        if(result.get(0).equalsIgnoreCase("hello")) {
                            tts.speak("What is your name", TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append(Html.fromHtml("<p style=\"color:grey;\">Speaker : What is your name ?</p>"));
                        }else if(result.get(0).contains("name")){
                            String name;
                            String nameis="my name is ";
                            if (result.get(0).contains(nameis)){
                                Integer length = nameis.length();
                                name = result.get(0).substring(length);

                            }
                            else {
                                name = result.get(0).substring(result.get(0).lastIndexOf(' ') + 1);
                            }
                            editor.putString("name", name).apply();
                            tts.speak("Hello, "+name,
                                    TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append(Html.fromHtml("<p style=\"color:grey;\">Speaker : Hello, "+name+"</p>"));
                        }else if(result.get(0).contains("not feeling well")){
                            tts.speak("I can understand. Please tell me your symptoms in short",
                                    TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append(Html.fromHtml("<p style=\"color:grey;\">Speaker : I can understand. Please tell your symptoms in short</p>"));
                        }else if(result.get(0).contains("thank you")){
                            tts.speak("Thank you too, "+preferences.getString("name","")+" Take care.", TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append(Html.fromHtml("<p style=\"color:grey;\">Speaker : Thank you too, "+preferences.getString("name","")+" Take care.</p>"));
                        }else if(result.get(0).contains("what time is it")){
                            SimpleDateFormat sdfDate =new SimpleDateFormat("HH:mm");//dd/MM/yyyy
                            Date now = new Date();
                            String[] strDate = sdfDate.format(now).split(":");
                            if(strDate[1].contains("00"))strDate[1] = "o'clock";
                            tts.speak("The time is : "+sdfDate.format(now), TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append(Html.fromHtml("<p style=\"color:grey;\">Speaker : The time is : "+sdfDate.format(now)+"</p>"));
                        }else if(result.get(0).contains("medicine")){
                            tts.speak("I think you have fever. Please take this medicine.",
                                    TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append(Html.fromHtml("<p style=\"color:grey;\">Speaker : I think you have fever. Please take this medicine.</p>"));
                        } else {
                            tts.speak("Sorry, I cant help you with that", TextToSpeech.QUEUE_FLUSH, null);
                            mVoiceInputTv.append(Html.fromHtml("<p style=\"color:grey;\">Speaker : Sorry, I cant help you with that</p>"));
                        }
                    }
                }
                break;
            }
        }
    }
}