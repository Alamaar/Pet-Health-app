package mad.oamk.pettracker.customdata.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mad.oamk.pettracker.R;

public class CustomDataCreateAdapter extends RecyclerView.Adapter<CustomDataCreateAdapter.ViewHolder> {

    private List<String> contents;

    public List<String> getContents() {
        return contents;
    }

    public CustomDataCreateAdapter(List<String> contents) {
        this.contents = contents;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private EditText editText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = (EditText) itemView.findViewById(R.id.editText);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_data_create_list_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            //add TextWatcher to edit text fields. All changes are then updated to contents.
            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int position = holder.getAdapterPosition();
                    contents.remove(position);
                    contents.add(position,charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }
}
