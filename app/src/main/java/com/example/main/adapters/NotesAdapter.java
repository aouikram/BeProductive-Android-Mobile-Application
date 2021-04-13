package com.example.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.R;
import com.example.main.models.NotesModel;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{

    private ArrayList<NotesModel> notesModelList;
    private Context context;
    private OnRecyclerItemClick onRecyclerItemClick;

    public NotesAdapter(ArrayList<NotesModel> notesModelList, Context context) {
        this.notesModelList = notesModelList;
        this.context = context;
    }

    public interface OnRecyclerItemClick {
        void onClick(int pos);
    }

    public void setOnRecyclerItemClick(OnRecyclerItemClick onRecyclerItemClick) {
        this.onRecyclerItemClick = onRecyclerItemClick;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.notes_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesViewHolder holder, int position) {
        holder.headText.setText(notesModelList.get(position).getHead());

        switch (position) {
            case 1:
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.color2));
                break;
            case 2:
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.color3));
                break;
            case 3:
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.color4));
                break;

            case 4:
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.color5));
                break;
            case 5:
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.color6));
                break;
            case 6:
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.color7));
                break;
            case 7:
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.color8));
                break;
            case 8:
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.color9));
                break;
            case 9:
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.color10));
                break;
                case 0:

            default:
                holder.view.setBackgroundColor(context.getResources().getColor(R.color.color1));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notesModelList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {

        private TextView headText;
        private View view;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            headText = itemView.findViewById(R.id.head1);
            view = itemView.findViewById(R.id.view1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRecyclerItemClick != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            onRecyclerItemClick.onClick(pos);
                        }
                    }
                }
            });
        }
    }
}
