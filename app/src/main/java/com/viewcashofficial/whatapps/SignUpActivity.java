package com.viewcashofficial.whatapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.viewcashofficial.whatapps.Models.Users;
import com.viewcashofficial.whatapps.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {


    ActivitySignUpBinding binding;
    private FirebaseAuth mauth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        mauth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        getSupportActionBar().hide();

        progressDialog =new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We' re creating your account.");

       binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!binding.txtUsername.getText().toString().isEmpty()&& ! binding.txtPassword.getText().toString().isEmpty())
               {
                   progressDialog.show();
                   mauth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString())
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           progressDialog.dismiss();
                           if (task.isSuccessful())
                           {
                               Users users=new Users(binding.txtUsername.getText().toString(),binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString());
                               String id=task.getResult().getUser().getUid();
                               database.getReference().child("Users").child(id).setValue(users);

                               Toast.makeText(SignUpActivity.this, "Sign UP Successfull ", Toast.LENGTH_SHORT).show();
                           }

                           else {
                               Toast.makeText(SignUpActivity.this,task.getException().toString(), Toast.LENGTH_SHORT).show();


                           }




                       }
                   });

               }
               else 
               {
                   Toast.makeText(SignUpActivity.this, "Enter Credentilas", Toast.LENGTH_SHORT).show();
               }

           }
       });

       binding.txtAlreadyhaveAccount.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
               startActivity(intent);

           }
       });





    }
}