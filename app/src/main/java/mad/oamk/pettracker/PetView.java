package mad.oamk.pettracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mad.oamk.pettracker.models.Pet;

public class PetView extends AppCompatActivity {

    private TextView nametextview;
    private TextView speciestextview;
    private TextView breedtextview;
    private TextView dateOfBirthtextview;
    private TextView tagtextview;
    private Button btnweight;
    private Button btnhealthness;
    private Button btnactivities;
    private Button btnfeed;
    private Button btnimages;
    private Button btnother;


    private DatabaseReference petIdRefrence;

    private String petId;

    private FirebaseUser user;

    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_view);

        //Get pet id from intent
        Intent intent = getIntent();
        petId = intent.getStringExtra("PetId");

        //Get user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        //Set refrence to pet
        petIdRefrence = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets").child(petId);

        // Event lisener to pet id to lissen all changes
        ValueEventListener petListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                pet = snapshot.getValue(Pet.class);

                setValues();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        petIdRefrence.addValueEventListener(petListener);

        nametextview = (TextView) findViewById(R.id.name);
        speciestextview = (TextView) findViewById(R.id.species);
        breedtextview = (TextView) findViewById(R.id.breed);
        dateOfBirthtextview = (TextView) findViewById(R.id.dateOfBirth);
        tagtextview = (TextView) findViewById(R.id.tag);

        Button btnweight = (Button) findViewById(R.id.weight);
        btnweight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //moveToWeight();
            }
        });


        /*Button btnhealthness = (Button) findViewById(R.id.healthness);
        btnhealthness.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToHealthness();
            }
        });
        Button btnactivities = (Button) findViewById(R.id.activities);
        btnactivities.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToActivities();
            }
        });
        Button btnfeed = (Button) findViewById(R.id.feed);
        btnfeed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToFeed();
            }
        });
        Button btnimages = (Button) findViewById(R.id.images);
        btnimages.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToImages();
            }
        });
        Button btnother = (Button) findViewById(R.id.other);
        btnother.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToOther();
            }
        });*/



    }

    private void setValues() {
        //Set values
        nametextview.setText(pet.getName());
        //TODO loput tiedot
    }

    public void moveToWeight() {
        //Intent intent = new Intent(this, WeightActivity.class);
        //startActivity(intent);
    }
}
/*
    public void moveToHealthness() {
        Intent intent = new Intent(this, HealthnessActivity.class);
        startActivity(intent);
    }

    public void moveToActivities() {
        Intent intent = new Intent(this, ActivitiesActivity.class);
        startActivity(intent);
    }

    public void moveToFeed() {
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }

    public void moveToImages() {
        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
    }

    public void moveToOther() {
        Intent intent = new Intent(this, OtherActivity.class);
        startActivity(intent);
    }
}
*/
