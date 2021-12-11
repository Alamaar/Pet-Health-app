package mad.oamk.pettracker.customdata.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mad.oamk.pettracker.R;

public class CustomDataViewAdapter  extends RecyclerView.Adapter<CustomDataViewAdapter.ViewHolder> {


    //TODO
        //on lonk press remove

    private  DatabaseReference reference;
    private  List<String> customDataIDs;
    private  Context context;

    private  List<String> fields;

    public List<String> getFields() {
        return fields;
    }

    public CustomDataViewAdapter(Context context, List<String> customDataIDs, DatabaseReference reference){
        this.context = context;
        this.customDataIDs = customDataIDs;
        this.reference = reference;
        setFields();
    }

    private void setFields() {
        //set the field names whic to populate later
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Map<String, Object> data = new HashMap<>();

                for(DataSnapshot child : snapshot.getChildren()) {
                    DataSnapshot chid = child;

                    data = (Map<String, Object>) child.getValue();

                }

                fields = new ArrayList<>(data.keySet());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_data_view_list, parent, false);
        View view2 = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_data_view_list_field, parent, false);

        return new CustomDataViewAdapter.ViewHolder(view,view2);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        DatabaseReference idReference  = reference.child(customDataIDs.get(holder.getAdapterPosition()));

        idReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {

                    Map<String,Object> data =  (Map<String, Object>) task.getResult().getValue();

                    for(int i = 0; i < fields.size(); i++){

                        View layout = LayoutInflater.from(holder.itemView.getContext())
                                .inflate(R.layout.custom_data_view_list_field, (ViewGroup) holder.itemView.getParent(),false);

                        TextView field = layout.findViewById(R.id.textViewField);
                        TextView fieldData = (TextView) layout.findViewById(R.id.textViewFieldData);

                        fieldData.setText( data.get(fields.get(i)).toString());
                        field.setText(fields.get(i));



                        ((LinearLayout) holder.linearLayout).addView(layout);

                    }
                    holder.linearLayout.requestLayout();

                    }

             }
        });




    }

    @Override
    public int getItemCount() {
        return customDataIDs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View linearLayout;
        private LinearLayout fieldLayout;

        public ViewHolder(@NonNull View itemView, View itemview2) {
            super(itemView);

           linearLayout =  itemView.findViewById(R.id.linearLayout);
           fieldLayout =   itemview2.findViewById(R.id.itemViewLayout);


        }
    }


}
