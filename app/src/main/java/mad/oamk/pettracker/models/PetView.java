package mad.oamk.pettracker.models;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mad.oamk.pettracker.R;

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


    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_view);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        nametextview = (TextView) findViewById(R.id.name);
        speciestextview = (TextView) findViewById(R.id.species);
        breedtextview = (TextView) findViewById(R.id.breed);
        dateOfBirthtextview = (TextView) findViewById(R.id.dateOfBirth);
        tagtextview = (TextView) findViewById(R.id.tag);

        Button btnweight = (Button) findViewById(R.id.weight);
        btnweight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToWeight();
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


        ValueEventListener petListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                StringBuilder info = new StringBuilder(new String());

                for (DataSnapshot child : snapshot.getChildren()) {
                    Pet pet = child.getValue(Pet.class);

                    assert pet != null;
                    String name = pet.getName();
                    String species = pet.getSpecies();
                    String breed = pet.getBreed();
                    String dateOfBirth = pet.getDateOfBirth();
                    //String tag = pet.getTag();

                    info.append("\n\n" + "Name: ").append(name).append("\nSpecies: ").append(species).append("\nbreed: ")
                            .append(breed).append("\ndateOfBirth: ").append(dateOfBirth);//.append("\ntag: ").append(tag);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    public void moveToWeight() {
        Intent intent = new Intent(this, WeightActivity.class);
        startActivity(intent);
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
