package com.evn.room;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends
        RecyclerView.Adapter<UserAdapter.ViewHolder> {
    public static final String TAG = "UserAdapter";

    public interface UserActionListener {
        public void onRequestDelete(User user);
    }
    UserActionListener userActionListener;

    public void setUserActionListener(UserActionListener userActionListener) {
        this.userActionListener = userActionListener;
    }

    private List<User> list = new ArrayList<>();

    public void addUsers(List<User> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUId;
        public TextView tvFirstName;
        public TextView tvLastName;

        public ViewHolder(View itemView) {
            super(itemView);

            tvUId = itemView.findViewById(R.id.uid);
            tvFirstName = itemView.findViewById(R.id.firstName);
            tvLastName = itemView.findViewById(R.id.lastName);

            itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, list.get(getAdapterPosition()).uid + "");
                    delete(list.get(getAdapterPosition()));
                }
            });
        }
    }

    void delete(User user) {
        if (userActionListener != null) {
            userActionListener.onRequestDelete(user);
        }
    }

    public void removeRowByUser(User user) {
        list.remove(user);
        notifyItemRemoved(list.indexOf(user));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.adapter_user, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = list.get(position);

        holder.tvUId.setText("#" + String.valueOf(user.uid));
        holder.tvFirstName.setText(user.firstName);
        holder.tvLastName.setText(user.lastName);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}
