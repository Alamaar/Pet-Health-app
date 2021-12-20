package mad.oamk.pettracker;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import mad.oamk.pettracker.models.Pet;

public class AddPetActivity extends AppCompatActivity {

    private FirebaseUser fireuser;
    private DatabaseReference mDatabase;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ImageView imageView;

    private String imageUrl;

    private Uri uriimage;

    private DatabaseReference petsReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Tarkistetaan onko käyttäjä edelleen kirjautunut:
        fireuser = FirebaseAuth.getInstance().getCurrentUser();
        if (fireuser == null) {
            // Not signed in, launch the Sign In activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        //Haetaan refrenssit
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child(fireuser.getUid()).child("images");

        // Nappia painamalla lisätään uusi lemmikki:
        Button add_pet = (Button) findViewById(R.id.btnAdd);
        add_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(add()){
                    if(uriimage != null) {
                        uploadImage(uriimage);
                    }

                    Intent intent = new Intent(AddPetActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });
    }

    public boolean add() { // Adding new pet

        EditText editText1 = (EditText) findViewById(R.id.txtInput1); // name
        String name = editText1.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.txtInput2); // species
        String species = editText2.getText().toString();
        EditText editText3 = (EditText) findViewById(R.id.txtInput3); // breed
        String breed = editText3.getText().toString();
        EditText editText4 = (EditText) findViewById(R.id.txtInput4); // date of birth
        String date_of_birth = editText4.getText().toString();

        // Kaikkien kenttien pitää olla täytettynä:
        if(editText1.getText().length() == 0  || editText2.getText().length() == 0 || editText3.getText().length() == 0 || editText4.getText().length() == 0) {
            Toast toast = Toast.makeText(this, getString(R.string.fill_in_the_required_fields),Toast.LENGTH_LONG);
            toast.show();
            return false;
        }

        // Kun kaikki tiedot on täytetty lisätään lemmikki firebaseen:
        else {
            String userID = fireuser.getUid();
            // Reference eli sijainti
            // .push() luo uuden uniikin avaimen ja palautaa sijainnin sen jälkeen set valuella lyödään lemmikki luokka sinne.
            petsReference = mDatabase.child("Pets").child(userID).child("Pets").push();
            // Pet constructor
            Pet pet = new Pet(name, date_of_birth, species, breed);

            petsReference.setValue(pet);

            Toast toast = Toast.makeText(this, getString(R.string.new_pet_added),Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
    }

    public void addImage() {
        mGetContent.launch("image/*");



    }

    public void uploadImage(Uri uri){
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        StorageReference imageUploadRefrence = storageReference.child("images/"+uri.getLastPathSegment());
        //TODO kuvat pitäs erotella jos lemmikin kuvat menee sitten images/lemmikki id / kuvat....jpg...
        //Profiilikuvat sitten userId/images alle. tietojen muokkakseen tarvii kuvan vaihtamisen. eli poistamisen ja uudelleen lataaminen.

        UploadTask uploadTask = imageUploadRefrence.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imageUrl = uri.toString();
                        Map<String, Object> imageurl = new HashMap<>();
                        imageurl.put("photoUrl", imageUrl);
                        petsReference.updateChildren(imageurl);



                    }
                });
            }
        });
    }



    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {

                    if (uri == null){
                        return;
                    }
                    uriimage = uri;
                    Glide.with(AddPetActivity.this).load(uri).into(imageView);

                }
            });




}