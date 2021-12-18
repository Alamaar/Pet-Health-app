package mad.oamk.pettracker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Map;

import mad.oamk.pettracker.R;

public class FeedingAdapter extends RecyclerView.Adapter<FeedingAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> dataIDs;
    private DatabaseReference databaseReference;

    public FeedingAdapter(Context context, ArrayList<String> dataIDs, DatabaseReference dataReference) {
        this.dataIDs = dataIDs;
        this.context = context;
        this.databaseReference = dataReference;
    }

    @NonNull
    @Override
    public FeedingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeding_list, parent, false);

        return new FeedingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedingAdapter.ViewHolder holder, int position) {

        position = holder.getAdapterPosition();
        //From customdata id get the id referring to viewholder in this position
        DatabaseReference idReference  = databaseReference.child((dataIDs.get(holder.getAdapterPosition())));


        //Get data in reference
        idReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    //Get data in reference
                    Map<String,Object> data =  (Map<String, Object>) task.getResult().getValue();

                    String date = (String) data.get("date"); //Päivämäärä
                    String time = (String) data.get("time"); // Kellonaika
                    String food = (String) data.get("food"); // Ruoka
                    holder.textview1.setText(date);
                    holder.textview2.setText(time);
                    holder.textview3.setText(food);


                }
            }

        });

    }





    @Override
    public int getItemCount() {
        if(dataIDs == null){
            return 0;
        }
        return dataIDs.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textview1; //Päivämäärä
        private TextView textview2; //Kellonaika
        private TextView textview3; //Ruoka

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textview1 = itemView.findViewById(R.id.foodtextview1); //Päivämäärä
            textview2 = itemView.findViewById(R.id.foodtextview2); //Kellonaika
            textview3 = itemView.findViewById(R.id.foodtextview3); //Ruoka

        }
    }
}


