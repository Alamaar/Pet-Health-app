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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import mad.oamk.pettracker.models.Pet;
/*
//Esimerkki taulukko rakenteesta
{
  "Pets" : {                                         Taulu
    "6QUlswFOzifILxFGn9LX9Xn9l6x2" : {               Omistaja id
      "Pets" : {                                     Pets taulu
        "-MpCeyKSYXBpqeaQvzWT" : {                   Lemmikin id
          "Weight" : {                               Paino taulu
            "-MpCezjouj-TVKS1VCB9" : {               Tietyn paino tapahtuman id
              "date" : "25-02-2020",                    tieto
              "weight" : "25"                           tieto
            }
          },
          "breed" : "Golden Retriever",                 tieto
          "dateOfBirth" : "15-01-2000",
          "name" : "Mustfi",
          "species" : "Dog"
        },
        "-MpCeyzNzPiveuS5coKT" : {                     Toinen lemmikki
          "breed" : "Golden Retriever",
          "dateOfBirth" : "15-01-2000",
          "name" : "Mustfi",
          "species" : "Dog"
        }
      }


 */


public class DbTestingActivity extends AppCompatActivity {

    private FirebaseUser fireuser;
    private TextView user;
    private TextView pettextview;

    private DatabaseReference mDatabase;
    //eri lemmikkien avain lista
    private Map<Long, String> keyList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_testing);

        //databasen refrenssi "sijainti"
        mDatabase = FirebaseDatabase.getInstance().getReference();

        user = (TextView) findViewById(R.id.user_info);
        pettextview = (TextView) findViewById(R.id.Pet_info);

        //haetaan kirjautunut käyttäjä ja varmistetaan että on käyttäjä on kirjautunut jos ei niin sitten siirytään login activiteettin
        fireuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fireuser == null) {
            // Not signed in, launch the Sign In activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {  //user info funktio alempana
            userinfo();
        }


        Button add_Pet = (Button) findViewById(R.id.add_pet);
        add_Pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        Button add_weight = (Button) findViewById(R.id.add_weight);
        add_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addweight();
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
        //tehdään kuuntelija, joka hakee lapsi taulut pet luokkaan ja siitä sitten tiedot teksikenttään
        //snapshot sisältää kaikki taulut ja ala taulut mihin kuuntelia on asetetttu.

        ValueEventListener petListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Long i = 0L;

                StringBuilder info = new StringBuilder(new String());

                for(DataSnapshot child : snapshot.getChildren()){
                    Pet pet = child.getValue(Pet.class);
                    i++;

                    assert pet != null;
                    String key = child.getKey();
                    keyList.put( i , key);
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
        //asetetaan äskön tehty kuuntelia käyttäjän lemmikki taulukkon. eli ainakun tapahtuu muutoksia se suorittaa kuuntelijan
        mDatabase.child("Pets").child(fireuser.getUid()).child("Pets").addValueEventListener(petListener);


    }



    public void add(){ //adding new pet

        String userID = fireuser.getUid();
        //refrensi eli sijainti
        //.push() luo uuden uniikin avaimen ja palautaa sen sijainnin sen jälkeen set valuella lyödään lemmikki luokka sinne.
        DatabaseReference petsref = mDatabase.child("Pets").child(userID).child("Pets").push();
        //Pet constructor
        //String name, String dateOfBirth, String species, String breed)
        Pet pet = new Pet("Mustfi", "15-01-2000", "Dog", "Golden Retriever");
        petsref.setValue(pet);

    }


    public void delete(){
        //DELETES the newest value in pets database
        //poistaa viimeisimmän lemmikin, ei tarvetta todennäköisesti sovelluksessa kun siellä voidaan poistaa suoraan lemmikin id:llä
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
        // käyttäjän tiedot firebaseuserluokasta.
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

    public void addweight(){

        String petId = keyList.get(1L); // id of pet to add the weigtht

        // refrensi mihin sijaintiin pusketaan.
        DatabaseReference petsref = mDatabase.child("Pets").child(fireuser.getUid()).child("Pets").child(petId);

        //tehdään map key value pareilla
        Map<String, Object> weight = new HashMap<>();
        weight.put("date", "25.02.2020");  // "dd.mm.yyyy"
        weight.put("weight", "25");

        //push luodaan uniikki id ja saadaan sen sijainti ja sen jälkeen asetetaan arvo.
        DatabaseReference ref = petsref.child("Weight").push();
        ref.setValue(weight);

    }
}