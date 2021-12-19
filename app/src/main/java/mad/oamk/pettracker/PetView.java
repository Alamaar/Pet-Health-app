package mad.oamk.pettracker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.Objects;

import mad.oamk.pettracker.adapters.PetViewButtonAdapter;
import mad.oamk.pettracker.customdata.CustomDataCreateActivity;
import mad.oamk.pettracker.customdata.CustomDataViewActivity;
import mad.oamk.pettracker.models.Pet;

public class PetView extends BaseActivity {

    private static final int MENU_EDIT = 21;
    private static final int MENU_DELETE = 43;
    private TextView nametextview;
    private TextView speciestextview;
    private TextView breedtextview;
    private TextView dateOfBirthtextview;

    private DatabaseReference petIdReference;

    private ValueEventListener petListener;

    private LinkedHashMap<String,Object> buttonsMap = new LinkedHashMap<>();
    private LinkedHashMap<String,Object> defaultButtonsMap = new LinkedHashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_view);



        //Get pets id from appdata singleton
        petId = AppData.getInstance().getPetId();

        //Set reference to pets id
        petIdReference = FirebaseDatabase.getInstance().getReference().child("Pets").child(user.getUid()).child("Pets").child(petId);

        //Set default buttons and what activity they go
        defaultButtonsMap.put(getString(R.string.weight), WeightActivity.class);
        defaultButtonsMap.put(getString(R.string.activity), Activities_activity.class);
        defaultButtonsMap.put(getString(R.string.feeding), Feeding_activity.class);
        defaultButtonsMap.put(getString(R.string.pictures), ImagesActivity.class);

        setButtonRecyclerView();


        //Get pets info
        petListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //get pet to pet class
                Pet pet = snapshot.getValue(Pet.class);
                //set values
                assert pet != null;
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


        // Add custom data field
        ImageButton buttonCustom = (ImageButton) findViewById(R.id.buttonCustom);
        buttonCustom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCustom();
            }
        });

        // Create custom data field buttons
        setButtonRecyclerView();


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
        String name = pet.getName();
        //Set title to pets name
        Objects.requireNonNull(getSupportActionBar()).setTitle(name);
        nametextview.setText(name);
        speciestextview.setText(pet.getSpecies());
        breedtextview.setText(pet.getBreed());
        dateOfBirthtextview.setText(pet.getDateOfBirth());
        String image = pet.getPhotoUrl();
        if(image != null){
            ImageView imageView = (ImageView) findViewById(R.id.petViewImage);
            Glide.with(this).load(image).into(imageView);
        }
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
                    Toast.makeText(PetView.this, getString(R.string.succesfully_deleted), Toast.LENGTH_SHORT).show();
                    PetView.this.finish();
                }
                else {
                    Toast.makeText(PetView.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, MENU_EDIT, Menu.NONE, getString(R.string.menu_action_update)).setIcon(R.drawable.ic_action_update).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, MENU_DELETE, Menu.NONE, getString(R.string.menu_action_delete)).setIcon(R.drawable.ic_action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case MENU_EDIT:
                Intent intent = new Intent(PetView.this, UpdatePetActivity.class);
                intent.putExtra("PetId", petId);
                startActivity(intent);
                break;
            case MENU_DELETE:
                delete();
                break;
        }
        return false;
    }

}

