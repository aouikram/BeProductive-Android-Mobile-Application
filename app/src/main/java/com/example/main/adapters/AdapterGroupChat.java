package com.example.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.R;
import com.example.main.models.ModelGroupChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import android.text.format.DateFormat;

public class AdapterGroupChat extends RecyclerView.Adapter<AdapterGroupChat.HolderGroupChat>{
    private static final int MSG_TYPE_LEFT = 0 ;
    private static final int MSG_TYPE_right = 1 ;
    private Context context;
    private ArrayList<ModelGroupChat> modelGroupChatList;
    private FirebaseAuth firebaseAuth;

    public AdapterGroupChat(Context context) {
        this.context = context;
    }
    public AdapterGroupChat(Context context, ArrayList<ModelGroupChat> modelGroupChatList) {
        this.context = context;
        this.modelGroupChatList = modelGroupChatList;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderGroupChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      if(viewType== MSG_TYPE_right)
      {
          View view = LayoutInflater.from(context).inflate(R.layout.row_groupchat_right , parent , false);
          return new HolderGroupChat(view);
      }
      else {
          View view = LayoutInflater.from(context).inflate(R.layout.row_groupchat_left , parent , false);
          return new HolderGroupChat(view);
      }

    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroupChat holder, int position) {
     ModelGroupChat model = modelGroupChatList.get(position);
     String message = model.getMessage();
     String timestamp = model.getTimestamp();
     String senderUid = model.getSender();
     String type = model.getType();

     if(type.equals("text")) {
        holder.messageIv.setVisibility(View.GONE);
         holder.messageTv.setVisibility(View.VISIBLE);
         holder.timeTv.setVisibility(View.VISIBLE);
         holder.nameTv.setVisibility(View.VISIBLE);
     }else {
         holder.messageIv.setVisibility(View.VISIBLE);
         holder.messageTv.setVisibility(View.GONE);
         holder.timeTv.setVisibility(View.GONE);
         holder.nameTv.setVisibility(View.GONE);
         Picasso.get().load(message).placeholder(R.drawable.ic_image_black_foreground).into(holder.messageIv);
     }

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String dateTime= (String) DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();

     holder.messageTv.setText(message);
     holder.timeTv.setText(dateTime);
     setUserName(model , holder);
    }

    private void setUserName(ModelGroupChat model, HolderGroupChat holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("AllUsers");
        ref.orderByChild("uid").equalTo(model.getSender()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String name= ""+ds.child("fullname").getValue();
                    holder.nameTv.setText(name);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelGroupChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(modelGroupChatList.get(position).getSender().equals(firebaseAuth.getUid())){
            return MSG_TYPE_right;
        }else{
        return MSG_TYPE_LEFT ; }
    }

    class  HolderGroupChat extends RecyclerView.ViewHolder  {
       private TextView nameTv , messageTv , timeTv;
       private ImageView messageIv;
       public HolderGroupChat(@NonNull View itemView) {
           super(itemView);
           nameTv = itemView.findViewById(R.id.nameTv);
           messageTv = itemView.findViewById(R.id.messageTv);
           timeTv = itemView.findViewById(R.id.timeTv);
           messageIv=itemView.findViewById(R.id.messageIv);
       }

    }
}
