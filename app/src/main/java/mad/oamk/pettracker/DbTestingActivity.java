package mad.oamk.pettracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import mad.oamk.pettracker.models.Pet;

public class DbTestingActivity extends AppCompatActivity {

    private FirebaseUser fireuser;
    private TextView user;
    private TextView pettextview;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_testing);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        user = (TextView) findViewById(R.id.user_info);
        pettextview = (TextView) findViewById(R.id.Pet_info);

        fireuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fireuser == null) {
            // Not signed in, launch the Sign In activity
            Intent intent = new Intent(this, LoginActivity.class);
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


        //listen changes to pet dataset
        ValueEventListener petListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                StringBuilder info = new StringBuilder(new String());

                for(DataSnapshot child : snapshot.getChildren()){
                    Pet pet = child.getValue(Pet.class);

                    assert pet != null;
                    String breed = pet.getBreed();
                    String name = pet.getName();
                    String species = pet.getSpecies();
                    String birthd = pet.getDateOfBirth();

                    info.append("\n\n" + "Name :").append(name).append("\nbreed").append(breed).append("\nspecies").append(species).append("\nbirth day").append(birthd);
                }




                pettextview.setText(info.toString());
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabase.child("Pets").child(fireuser.getUid()).child("Pets").addValueEventListener(petListener);
    }



    public void add(){ //adding new pet

        String userID = fireuser.getUid();
        DatabaseReference petsref = mDatabase.child("Pets").child(userID).child("Pets").push();
        //Pet constructor
        //String name, String dateOfBirth, String species, String breed)
        Pet pet = new Pet("Mustfi", "15-01-2000", "Dog", "Golden Retriever");
        petsref.setValue(pet);

    }


    public void delete(){
        //DELETES the newest value in pets database
        DatabaseReference petsref = mDatabase.child("Pets").child(fireuser.getUid()).child("Pets");
        Query query = petsref.orderByValue().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot latestsnap: snapshot.getChildren()){
                    latestsnap.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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