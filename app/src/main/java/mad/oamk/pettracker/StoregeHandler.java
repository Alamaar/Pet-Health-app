package mad.oamk.pettracker;

import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StoregeHandler {
    //TODO singleton? ei välttis


        public interface StorageObjectListener {
            public void onUploadReady(String title);
            //public void onDataLoaded(SomeData data);
        }


        //https://guides.codepath.com/android/Creating-Custom-Listeners
        // Member variable was defined earlier
        private StorageObjectListener listener;

        // Constructor where listener events are ignored
        public StoregeHandler() {
            // set null or default listener or accept as argument to constructor
            this.listener = null;
            loadDataAsync();
        }

        //TODO tänne lisätä myös tietyn refrensin alta palauttaa listan kuvien urleista? tai listan jommin kummin.


        //koodissa esimerkki
        // StorageHandler storage = new.....
        //storage.addimage()  //parametrejä mihin. storagerefrense.
        //storage.setCustomObjectListener(new storage.StorageObjectListener()
       /*     @Override
            public void onObjectReady(String title) {
                Glide....
                storage.getImagerul ???
                storage.
            }
    });*/


    public void addImage() {
        mGetContent.launch("image/*");



    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {

                    if (uri == null){
                        return;
                    }
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
                                    Glide.with(AddPetActivity.this).load(uri).into(imageView);
                                    imageUrl = uri.toString();
                                }
                            });
                        }
                    });
                }
            });





    //tässä tehä juttuja
        public void loadDataAsync() {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get("https://mycustomapi.com/data/get.json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // Networking is finished loading data, data is processed
                    SomeData data = SomeData.processData(response.get("data"));
                    // Do some other stuff as needed....
                    // Now let's trigger the event
                    if (listener != null)
                        listener.onDataLoaded(data); // <---- fire listener here
                }
            });
        }
    }
}
