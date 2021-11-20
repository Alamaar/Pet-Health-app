package mad.oamk.pettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mad.oamk.pettracker.models.Pet;

public class DbTestingActivity extends AppCompatActivity {

    private FirebaseUser fireuser;
    private TextView user;
    private TextView pet;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_testing);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        user = (TextView) findViewById(R.id.user_info);
        pet = (TextView) findViewById(R.id.Pet_info);

        fireuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fireuser == null) {
            // Not signed in, launch the Sign In activity
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
        else {
            userinfo();
        }


        Button add_Pet = (Button) findViewById(R.id.add_pet);
        add_Pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });



            Button delete_pet = (Button) findViewById(R.id.delete_pet);
        delete_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
            });
    }



    public void add(){
        String userID = fireuser.getUid();
        DatabaseReference petsref = mDatabase.child("Pets").child(userID).child("Pets").push();
        //String name, String dateOfBirth, String species, String breed)
        Pet pet = new Pet("Mustfi", "15-01-2000", "Dog", "Golden Retriever");
        petsref.setValue(pet);

    }


    public void delete(){

    }

    public  void userinfo(){

        if (fireuser != null) {
            // Name, email address, and profile photo Url
            String name = fireuser.getDisplayName();
            String email = fireuser.getEmail();
            //Uri photoUrl = fireuser.getPhotoUrl();
            //String Url = photoUrl.toString();
            String uid = fireuser.getUid();

            String userinfo = "User Name :" + name +
                    "\nemail:" + email +
                    "\nUser ID" + uid;

            user.setText(userinfo);
        }

    }
}