package mad.oamk.pettracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import mad.oamk.pettracker.customdata.CustomDataCreateActivity;
import mad.oamk.pettracker.customdata.CustomDataViewActivity;
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

    private LinkedHashMap<String,Object> buttonsMap = new LinkedHashMap<>();
    private LinkedHashMap<String,Object> defaultButtonsMap = new LinkedHashMap<>();


    //TODO recyclerview nappeja näyttämään.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_view);

        //Get pets id from appdata singleton
        petId = AppData.getInstance().getPetId();


        //Get curren user from firebase instance
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Not signed in, launch the Sign In activity
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        //Set refrence to pets id
        petIdRefrence = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets").child(petId);

        //Set default buttons and what activitys they go
        defaultButtonsMap.put("Paino",MainActivity.class);
        defaultButtonsMap.put( "Terveystiedot",null);
        defaultButtonsMap.put("Ulkoilu",null);
        defaultButtonsMap.put("Ruokinta", null);
        defaultButtonsMap.put("Kuvat",null);

        setButtonRecyclerView();


        // Event lisener to pet id to lissen all changes
        ValueEventListener petListener = new ValueEventListener() {
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
        petIdRefrence.removeEventListener(petListener);

        nametextview = (TextView) findViewById(R.id.name);
        speciestextview = (TextView) findViewById(R.id.species);
        breedtextview = (TextView) findViewById(R.id.breed);
        dateOfBirthtextview = (TextView) findViewById(R.id.dateOfBirth);
        tagtextview = (TextView) findViewById(R.id.tag);

        /*Button btnweight = (Button) findViewById(R.id.weight);
        btnweight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //moveToWeight();
            }
        });


         */

        ImageButton buttonCustom = (ImageButton) findViewById(R.id.buttonCustom);
        buttonCustom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCustom();
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

        setButtonRecyclerView();


    }

    private void setButtonRecyclerView() {
        RecyclerView buttonRecyclerView = findViewById(R.id.buttonRecyclerView);


        buttonsMap = new LinkedHashMap<>(defaultButtonsMap);

        PetViewButtonAdapter adapter = new PetViewButtonAdapter(this, buttonsMap);

        buttonRecyclerView.setAdapter(adapter);
        buttonRecyclerView.setLayoutManager(new GridLayoutManager(this,2));


        ValueEventListener customDataEntryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                buttonsMap.clear();
                buttonsMap.putAll(defaultButtonsMap);

                for(DataSnapshot child : snapshot.getChildren()){
                    String dataSetName = child.getKey();

                    buttonsMap.put(dataSetName, CustomDataViewActivity.class);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        petIdRefrence.child("CustomData").addValueEventListener(customDataEntryListener);












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

    public void moveToCustom() {
        Intent intentcustom = new Intent(this, CustomDataCreateActivity.class);
        //intentcustom.putExtra("PetId",petId);
        startActivity(intentcustom);
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
