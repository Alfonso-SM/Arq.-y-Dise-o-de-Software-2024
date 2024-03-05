package com.equipo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.equipo.SignUp.UserHelperClass;
import com.equipo.SignUp.VerifyOTP;
import com.equipo.dto.ClinicHistory;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RecordHistory extends AppCompatActivity {

    TextInputLayout fullname, age, civilStatus, nationality,dob,diseases,notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fullname = findViewById(R.id.fullname);
        age = findViewById(R.id.age);
        civilStatus = findViewById(R.id.civil_status);
        nationality = findViewById(R.id.nationality);
        dob = findViewById(R.id.dob);
        diseases = findViewById(R.id.diseases);
        notes = findViewById(R.id.notes);
    }
    public void saveMedicalRecord(View view) {
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();
        String phoneNo = userDetails.getOrDefault(SessionManager.KEY_PHONENO, "");

        if(!phoneNo.isEmpty()) {
            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
            DatabaseReference reference = rootNode.getReference("Users");
            ClinicHistory clinicHistory = new ClinicHistory();
            clinicHistory.setFullname(getTextInputLayoutValue(fullname));
            clinicHistory.setAge(Integer.parseInt(getTextInputLayoutValue(age)));
            clinicHistory.setCivilStatus(getTextInputLayoutValue(civilStatus));
            clinicHistory.setNationality(getTextInputLayoutValue(nationality));
            clinicHistory.setDob(getTextInputLayoutValue(dob));
            clinicHistory.setDob(getTextInputLayoutValue(diseases));
            clinicHistory.setNotes(getTextInputLayoutValue(notes));
            reference.child(phoneNo)
                    .child("clinic_history")
                    //.push()
                    .setValue(clinicHistory);
        }
    }

    private String getTextInputLayoutValue(TextInputLayout component) {

        return component.getEditText().getText().toString().trim();
    }

}
