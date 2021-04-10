package com.example.main.adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.R;
import com.example.main.models.ModelGroupChatsList;

import java.util.ArrayList;

public class AdapterGroupChatList extends RecyclerView.Adapter<AdapterGroupChatList.HolderGroupChatList>{
     private Context context ;
     private ArrayList<ModelGroupChatsList> groupChatLists;
   public AdapterGroupChatList(Context context , ArrayList<ModelGroupChatsList> groupChatLists){
       this.context= context ;
       this.groupChatLists = groupChatLists ;
       Log.v("TAG" , "adapter created");
   }
    @NonNull
    @Override
    public HolderGroupChatList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.chat_home , parent , false);
        return new HolderGroupChatList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroupChatList holder, int position) {
            ModelGroupChatsList model = groupChatLists.get(position);
            String groupId= model.getGroupId();
             String groupTitle= model.getGroupTitle();
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // later
           }
       });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HolderGroupChatList extends RecyclerView.ViewHolder{
        private TextView groupTitleTv;
        private TextView nameTv , messageTv, timeTv;

        public HolderGroupChatList(@NonNull View itemView) {
            super(itemView);

            groupTitleTv = itemView.findViewById(R.id.groupTitleTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);

        }
    }
}
