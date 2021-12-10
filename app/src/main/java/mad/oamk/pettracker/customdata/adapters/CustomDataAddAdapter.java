package mad.oamk.pettracker.customdata.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mad.oamk.pettracker.R;

public class CustomDataAddAdapter extends RecyclerView.Adapter<CustomDataAddAdapter.ViewHolder> {

    //kenttä nimi ja edit text

    public List<String> getFields() {
        return fields;
    }

    private List<String> fields;
    private List<String> editTextFields;

    public List<String> getEditTextFields() {
        return editTextFields;
    }


    public CustomDataAddAdapter(List<String> fields) {
        this.fields = fields;
        this.editTextFields = new ArrayList<String>(fields);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private EditText editText;
        private TextView headerTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            editText = (EditText) itemView.findViewById(R.id.textInputData);
            headerTextView = (TextView) itemView.findViewById(R.id.textFieldName);
        }


    }
    @NonNull
    @Override
    public CustomDataAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_data_add, parent, false);

        return new CustomDataAddAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomDataAddAdapter.ViewHolder holder, int position) {


        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int position = holder.getAdapterPosition();

                editTextFields.remove(position);
                editTextFields.add(position,charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.headerTextView.setText(fields.get(holder.getAdapterPosition()));
    }




    @Override
    public int getItemCount() {
        return fields.size();
    }
}


