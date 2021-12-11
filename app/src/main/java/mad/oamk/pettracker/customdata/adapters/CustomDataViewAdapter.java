package mad.oamk.pettracker.customdata.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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


    //TODO on lonk press remove

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
        //set the field names which to populate later
        //fetches data on reference and loops thrue and saves the keys to field list
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

        return new CustomDataViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        //From customdata id get the id referring to viewholder in this position
        DatabaseReference idReference  = reference.child(customDataIDs.get(holder.getAdapterPosition()));

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

                    //For how many filds create new layout custom_data_view_list_field that has two text fields for field and files data
                    //Every loo that layout file is then added to holders linearlayout.
                    for(int i = 0; i < fields.size(); i++){

                        View layout = LayoutInflater.from(holder.itemView.getContext())
                                .inflate(R.layout.custom_data_view_list_field, (ViewGroup) holder.itemView.getParent(),false);

                        TextView field = layout.findViewById(R.id.textViewField);
                        TextView fieldData = (TextView) layout.findViewById(R.id.textViewFieldData);

                        fieldData.setText( data.get(fields.get(i)).toString());
                        field.setText(fields.get(i));

                        ((LinearLayout) holder.linearLayout).addView(layout);

                    }
                    //Resize layout
                    holder.linearLayout.requestLayout();

                    }

             }
        });
        //Create dialog on long press to confirm removing the data from given position.
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                idReference.removeValue();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                });

                AlertDialog dialog = builder.create();


                dialog.show();
                return true;
            };
        });




    }

    @Override
    public int getItemCount() {
        return customDataIDs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View linearLayout;
        private LinearLayout fieldLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           linearLayout =  itemView.findViewById(R.id.linearLayout);

        }
    }


}
