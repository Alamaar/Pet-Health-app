package mad.oamk.pettracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    private ValueEventListener petListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_view);

        //Get pets id from intent
        Intent intent = getIntent();
        petId = intent.getStringExtra("PetId");

        //Get curren user from firebase instance
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        //Set refrence to pets id
        petIdRefrence = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets").child(petId);

        // Event lisener to pet id to lissen all changes
        petListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get pet to pet class
                pet = snapshot.getValue(Pet.class);
                //set values
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

        // Lemmikin poistaminen DELETE napilla:
        ImageButton delete_pet = (ImageButton) findViewById(R.id.btnDeletePet);
        delete_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(); // Tämä metodi löytyy hieman alempaa...
            }
        });

        // Lemmikin tietojen päivitystä varten avataan oma aktiviteetti:
        ImageButton update_pet = (ImageButton) findViewById(R.id.btnUpdatePet);
        update_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetView.this, UpdatePetActivity.class);
                intent.putExtra("PetId",petId);
                startActivity(intent);
            }
        });


    }

    private void setValues() {
        //Set values to the textviews:
        nametextview.setText(pet.getName());
        speciestextview.setText(pet.getSpecies());
        breedtextview.setText(pet.getBreed());
        dateOfBirthtextview.setText(pet.getDateOfBirth());

    }

    public void moveToWeight() {
        //Intent intent = new Intent(this, WeightActivity.class);
        //startActivity(intent);
    }

    public void delete(){
        petIdRefrence.removeEventListener(petListener);
        // Poistaa tietyn lemmikin ID:n perusteella:
        DatabaseReference petsref = petIdRefrence;
        petsref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PetView.this, "Succesfully deleted.", Toast.LENGTH_SHORT).show();
                    PetView.this.finish();
                }
                else {
                    Toast.makeText(PetView.this, "Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
