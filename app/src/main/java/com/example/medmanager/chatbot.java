package com.example.medmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chatbot extends AppCompatActivity {

    private TextView tvChat;
    private EditText etUserInput;
    private boolean isAskingUserInfo = true;
    private boolean check = true;
    private String userName = "";
    private int userAge = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        tvChat = findViewById(R.id.tvChat);
//        ListView list = findViewById(R.id.chatlsit);
//        List<String> chat = new ArrayList<String>();
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,chat);
//        list.setAdapter(arrayAdapter);


        etUserInput = findViewById(R.id.etUserInput);
        displayChatMessage("Bot: Hi there! I'm your medical chatbot. And you are?");

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = etUserInput.getText().toString().trim();
                if (!TextUtils.isEmpty(userInput)) {
                    if (check) {
                        processUserInfo(userInput);
                    } else {
                        processUserInput(userInput);
                    }
                    etUserInput.setText("");
                }
            }
        });
    }

    private void processUserInfo(String userInput) {
        displayChatMessage("You: " + userInput);
        if (isAskingUserInfo) {
            if (!TextUtils.isEmpty(userInput)) {
                userName = userInput;
                displayChatMessage("Bot: Hi " + userName + "! Nice to meet you. Could you please provide your age?");
                isAskingUserInfo = false;
            } else {
                displayChatMessage("Bot: I'm sorry, I didn't catch that. Could you please provide your name again?");
            }
        } else {
            if (!TextUtils.isEmpty(userInput)) {

                try {
                    userAge = Integer.parseInt(userInput);
                    displayChatMessage("Bot: Thank you for providing your age, " + userName + ". How can i help you?");
                    isAskingUserInfo = false;
                    check = false;
                } catch (NumberFormatException e) {
                    displayChatMessage("Bot: I'm sorry, that doesn't seem to be a valid age. Could you please provide your age again?");
                }
            } else {
                displayChatMessage("Bot: I'm sorry, I didn't catch that. Could you please provide your age again?");
            }
        }
    }
    private void processUserInput(String userInput) {
        // Implement your symptom checking logic here
        // You can use conditionals or call an API for symptom analysis

        String botResponse = generateBotResponse(userInput);
        displayChatMessage("You: " + userInput);
        displayChatMessage("Bot: " + botResponse);
    }

    private String generateBotResponse(String userInput) {
        // Implement your chatbot response generation logic here
        String botResponse;

        if (TextUtils.isEmpty(userName) || userAge == 0 ) {
            botResponse = "Bot: Before I can assist you, I need some more information. Could you please provide your name, age?";
            isAskingUserInfo = true;
        } else {
            // Define a list of predefined responses for different symptoms
            HashMap<String, String> symptomResponses = new HashMap<>();
            symptomResponses.put("headache", "Headaches can be caused by various factors such as stress, tension, or migraines. It's important to identify the underlying cause and consider seeking medical advice if the headaches persist.");
            symptomResponses.put("cough", "Coughing can be a symptom of many conditions, including cold, flu, or allergies. If you experience persistent coughing or other concerning symptoms, it's recommended to consult a healthcare professional.");
            symptomResponses.put("fever", "Fever can be a sign of infection or other underlying health issues. It's advisable to monitor your temperature, stay hydrated, and consult a doctor if the fever persists or is accompanied by other severe symptoms.");
            symptomResponses.put("fatigue", "Fatigue can have various causes, including lack of sleep, stress, or medical conditions. It's important to identify the underlying cause and make necessary lifestyle changes or seek medical advice if the fatigue persists.");
            symptomResponses.put("stomach pain", "Stomach pain can be caused by multiple factors such as indigestion, gastritis, or gastrointestinal issues. If you experience severe or persistent stomach pain, it's recommended to consult a healthcare professional for proper evaluation and advice.");
            symptomResponses.put("sore throat", "Sore throat can be a symptom of viral or bacterial infections, allergies, or acid reflux. Resting, drinking warm fluids, and gargling with saltwater can help alleviate the symptoms. Consult a doctor if the sore throat worsens or persists for more than a few days.");
            symptomResponses.put("shortness of breath", "Shortness of breath can be a sign of various respiratory or cardiovascular conditions. It's essential to seek immediate medical attention if you experience sudden or severe shortness of breath.");
            symptomResponses.put("back pain", "Back pain can be caused by muscle strain, poor posture, or underlying spinal issues. Applying heat or cold packs and gentle stretching may provide relief. If the pain persists or is accompanied by other symptoms, it's advisable to consult a healthcare professional.");
            symptomResponses.put("rash", "A rash can be caused by allergies, skin conditions, or infections. If you experience a persistent or severe rash, it's recommended to consult a dermatologist for proper diagnosis and treatment.");
            symptomResponses.put("dizziness", "Dizziness can result from various factors, such as low blood pressure, dehydration, or inner ear problems. If dizziness is frequent or accompanied by other concerning symptoms, it's advisable to consult a healthcare professional.");

            // Check if user input matches any predefined symptom
            for (Map.Entry<String, String> entry : symptomResponses.entrySet()) {
                if (userInput.contains(entry.getKey())) {
                    botResponse = entry.getValue();
                    return botResponse;
                }
            }

            // If no matching symptom is found, provide a generic response
            botResponse = "I'm sorry, I don't have enough information to provide a specific response. It's always recommended to consult a healthcare professional for proper evaluation and advice.";
        }
        return botResponse;
    }


    private void displayChatMessage(String message) {
        tvChat.append(message + "\n\n");
    }


}
