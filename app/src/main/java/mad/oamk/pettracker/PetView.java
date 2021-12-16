package mad.oamk.pettracker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;

import mad.oamk.pettracker.customdata.CustomDataCreateActivity;
import mad.oamk.pettracker.customdata.CustomDataViewActivity;
import mad.oamk.pettracker.models.Pet;

public class PetView extends BaseActivity {

    private TextView nametextview;
    private TextView speciestextview;
    private TextView breedtextview;
    private TextView dateOfBirthtextview;

    private DatabaseReference petIdReference;

    private ValueEventListener petListener;

    private Pet pet;

    private LinkedHashMap<String,Object> buttonsMap = new LinkedHashMap<>();
    private LinkedHashMap<String,Object> defaultButtonsMap = new LinkedHashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_view);

        //Get pets id from appdata singleton
        petId = AppData.getInstance().getPetId();

        //Set refrence to pets id
        petIdReference = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets").child(petId);

        //Set default buttons and what activity they go
        defaultButtonsMap.put("Paino", WeightActivity.class);
        defaultButtonsMap.put("Terveystiedot", null);
        defaultButtonsMap.put("Ulkoilu", Activities_activity.class);
        defaultButtonsMap.put("Ruokinta", Feeding_activity.class);
        defaultButtonsMap.put("Kuvat", null);

        setButtonRecyclerView();


        //Get pets info
        petListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get pet to pet class
                pet = snapshot.getValue(Pet.class);
                //set values
                setValues(pet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        petIdReference.addListenerForSingleValueEvent(petListener);

        nametextview = (TextView) findViewById(R.id.name);
        speciestextview = (TextView) findViewById(R.id.species);
        breedtextview = (TextView) findViewById(R.id.breed);
        dateOfBirthtextview = (TextView) findViewById(R.id.dateOfBirth);
        //tagtextview = (TextView) findViewById(R.id.tag);



        ImageButton buttonCustom = (ImageButton) findViewById(R.id.buttonCustom);
        buttonCustom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCustom();
            }
        });


        setButtonRecyclerView();

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
                intent.putExtra("PetId", petId);
                startActivity(intent);
            }
        });
    }



    private void setButtonRecyclerView() {
        //set up recycler view and petviewbuttonadapter
        RecyclerView buttonRecyclerView = findViewById(R.id.buttonRecyclerView);

        buttonsMap = new LinkedHashMap<>(defaultButtonsMap);

        PetViewButtonAdapter adapter = new PetViewButtonAdapter(this, buttonsMap);

        buttonRecyclerView.setAdapter(adapter);
        buttonRecyclerView.setLayoutManager(new GridLayoutManager(this,2));



        // set listener for pets custom data list and put theese new values to buttons map.
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

        petIdReference.child("CustomData").addValueEventListener(customDataEntryListener);
    }

    private void setValues(Pet pet) {
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

    public void moveToCustom() {
        Intent intentcustom = new Intent(this, CustomDataCreateActivity.class);
        //intentcustom.putExtra("PetId",petId);
        startActivity(intentcustom);
    }



    public void delete(){
        petIdReference.removeEventListener(petListener);
        // Poistaa tietyn lemmikin ID:n perusteella:
        DatabaseReference petsref = petIdReference;
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

