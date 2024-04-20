package com.equipo.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.equipo.R;

public class ForgetPassword extends AppCompatActivity {
    TextInputLayout phoneNumber;
    CountryCodePicker countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        phoneNumber=findViewById(R.id.Forget_pass_phoneNo);
        countryCode=findViewById(R.id.country_code_forget);

    }

    public void verifyPhoneNumber(View view) {

            if (!validationField()) {
                return;
            }

            // get data
            String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();

            // DataBase

            Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_phoneNumber);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);
                        String _phoneNoComplete = "+" + countryCode.getFullNumber() + _phoneNumber;


                        Intent intent=new Intent(getApplicationContext(),VerifyOTP.class);
                        intent.putExtra("phoneNoComplete", _phoneNoComplete);
                        intent.putExtra("phoneNo",_phoneNumber);
                        intent.putExtra("Forget","updateData");
                        startActivity(intent);
                        finish();


                    } else {
                        Toast.makeText(ForgetPassword.this, "No existe el usuario", Toast.LENGTH_SHORT).show();
                        phoneNumber.setError("Numero celular incorrecto");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ForgetPassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }





    private boolean validationField() {
        String _phoneNumber = phoneNumber.getEditText().getText().toString().trim();


        if (_phoneNumber.isEmpty()) {
            phoneNumber.setError("Phone number can not be empty");
            phoneNumber.requestFocus();
            return false;
        }
        else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }


    }
    public void back5(View view){
        super.onBackPressed();
    }


}