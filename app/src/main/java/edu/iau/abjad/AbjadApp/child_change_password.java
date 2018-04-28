package edu.iau.abjad.AbjadApp;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class child_change_password extends child_menu {
    menu_variables m = new menu_variables();
    FirebaseUser user;
    EditText current_pass, new_pass, confirm_pass;
    String curr, new_p , con_pass;
    firebase_connection r = new firebase_connection();
    String node, email;
    FirebaseAuth Uath;
    Button save_changes;
    int counter =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        m.title = (TextView) findViewById(R.id.interface_title);
        m.title.setText("الرئيسية");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_child_change_password, null, false);

        myDrawerLayout.addView(contentView, 0);

        current_pass = (EditText) findViewById(R.id.current_pass_ch);
        new_pass = (EditText) findViewById(R.id.new_pass_ch);
        confirm_pass = (EditText) findViewById(R.id.confirm_pass_ch);
        save_changes = (Button) findViewById(R.id.save_changes_ch);

        user = FirebaseAuth.getInstance().getCurrentUser();
        node = "Children";


        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curr = current_pass.getText().toString();
                new_p = new_pass.getText().toString();
                con_pass = confirm_pass.getText().toString();

                if(curr.isEmpty()){
                    current_pass.setError("الرجاء إدخال كلمة المرور الحالية");
                    current_pass.requestFocus();
                    counter++;
                }
                if(new_p.isEmpty()){
                    new_pass.setError("الرجاء تعبئة كلمة المرور الجديدة");
                    new_pass.requestFocus();
                    counter++;
                }
                if(con_pass.isEmpty()){
                    confirm_pass.setError("الرجاء إعادة تعبئة كلمة المرور الجديدة");
                    confirm_pass.requestFocus();
                    counter++;
                }
                if(new_p.equals(con_pass) == false){
                    confirm_pass.setError("الكلمتنين غير متطابقتين");
                    confirm_pass.requestFocus();
                    counter++;
                }
                if(counter == 0){
                    Query query = r.ref.child(node).orderByKey().equalTo(Signin.id_child);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                System.out.println("دخل هووون");

                                for(DataSnapshot info : dataSnapshot.getChildren()){
                                    email = info.child("email").getValue().toString();
                                }
                                System.out.println("الايميل"+ email);
                                AuthCredential credential = EmailAuthProvider
                                        .getCredential(email, curr);
                                user.reauthenticate(credential)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    user.updatePassword(new_p).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(),
                                                                        "تم تغيير كلمة السر بنجاح", Toast.LENGTH_SHORT).show();

                                                            } else {
                                                                if(task.getException().getMessage().startsWith("The given password is invalid")){
                                                                    new_pass.setError("كلمة السر يجب أن لا تقل عن 6 خانات");
                                                                    new_pass.requestFocus();

                                                                }
                                                                else {
                                                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                                                }

                                                            }
                                                        }
                                                    });


                                                } else {
                                                    current_pass.setError("الرجاء إدخال كلمة المرور الحالية بشكل صحيح");
                                                    current_pass.requestFocus();

                                                }
                                            }
                                        });
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
}
