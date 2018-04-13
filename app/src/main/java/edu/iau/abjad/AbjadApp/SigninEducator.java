package edu.iau.abjad.AbjadApp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninEducator extends AppCompatActivity  implements View.OnClickListener{

    private FirebaseAuth Uath; //pravite?
    private EditText Email, EdPassword;
    private ProgressBar progressBar;
    private ImageView Wuser,Wpas;
    private ImageButton enter;
    private Intent Itn;
    static String id_edu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_educator);

        Uath= FirebaseAuth.getInstance();
        Email= (EditText) findViewById(R.id.Email_e);
        EdPassword=(EditText) findViewById(R.id.password);
        progressBar=(ProgressBar) findViewById(R.id.EprogressBar);
        Wuser=(ImageView) findViewById(R.id.Wuser);
        Wpas=(ImageView) findViewById(R.id.Wpas);

        //adding listeners to the buttons:
        findViewById(R.id.SendButton).setOnClickListener(this);
        findViewById(R.id.ResetPassword).setOnClickListener(this);
        findViewById(R.id.rgs).setOnClickListener(this);
        Itn =new Intent(this,educator_home.class);

        Wpas.setVisibility(View.INVISIBLE);
        Wuser.setVisibility(View.INVISIBLE);
    }




    private void signIn(){
        String email = Email.getText().toString().trim();
        String password = EdPassword.getText().toString().trim();
        int counter=0;

        //input validation:

        if (email.isEmpty()) {
            Email.setError("Email is required");
            Email.requestFocus();
            Wpas.setVisibility(View.VISIBLE);
            counter++;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Please enter a valid email");
            Wpas.setVisibility(View.VISIBLE);
            Email.requestFocus();
            counter++;
        }
        if (password.isEmpty()) {
            EdPassword.setError("Password is required");
            EdPassword.requestFocus();
            Wuser.setVisibility(View.VISIBLE);
            counter++;

        }
        progressBar.setVisibility(View.VISIBLE);

        if(counter==0){
            //core of sign in
            //validate email/password then rerutn the sign in state
            // if worked then finshies this view and shows the Edu home
            Uath.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {

                                id_edu = Uath.getCurrentUser().getUid();
                                finish();
                                startActivity(Itn);
                            }

               /* else if(task.getException() instanceof FirebaseAuthUserCollisionException)
                {
                    Toast.makeText(getApplicationContext(), "Email already exisets!", Toast.LENGTH_SHORT).show();
                }

                //this one must be about the password, that it doesn't match out email
                */


                            else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rgs:
                finish();
                startActivity(new Intent(this,SignUp.class));
                break;

            case R.id.ResetPassword:
                finish();
                startActivity(new Intent(this,ResetPassword.class));
                break;

            case R.id.SendButton:
                signIn();
                break;
        }
    }

}

