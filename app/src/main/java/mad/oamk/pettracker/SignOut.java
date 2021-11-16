package mad.oamk.pettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);




        Button signOutButton = (Button) findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signOut();
            }
        });


    }


    public void signOut(){
        //TODO signout
    }
}