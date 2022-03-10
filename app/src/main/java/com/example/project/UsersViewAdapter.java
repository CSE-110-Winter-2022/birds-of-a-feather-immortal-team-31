package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.FavoriteUserDatabase;
import com.example.project.model.User;

import java.io.Serializable;
import java.util.List;

public class UsersViewAdapter extends RecyclerView.Adapter<UsersViewAdapter.ViewHolder>
{
    //Context to initialize the FavoriteStudentDatabase
    private Context context;
    private List<User> users;
    //Database that stores favorite students
    private FavoriteUserDatabase db;

    public UsersViewAdapter(Context context,List<User> users) {
        super();
        this.context = context;
        this.users = users;
        db = FavoriteUserDatabase.singleton(this.context);
    }

    @NonNull
    @Override
    public UsersViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_main_user_row, parent, false);

        return new UsersViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewAdapter.ViewHolder holder, int position) {
        holder.setUser(this.users.get(position));
    }

    public User getUserAtIndex(int i) {
        return users.get(i);
    }

    //Get users from this adapter
    public List<User> getUsers(){return this.users;}

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public void setFellowUsers(List<User> fellowUsers) {
        this.users = fellowUsers;
    }

    public class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        private final TextView userView;
        private User user;
        private final ImageButton ButtonStar = (ImageButton) itemView.findViewById(R.id.star);

        ViewHolder(View itemView) {
            super(itemView);
            this.userView = itemView.findViewById(R.id.users_row);
            itemView.setOnClickListener(this);

            ButtonStar.setOnClickListener(new View.OnClickListener() {

                //Onclick: change the favorite state of users and update the change on favorite students database
                @Override
                public void onClick(View view) {
                    if (ViewHolder.this.user.getStar()){
                        ViewHolder.this.user.changeStar();
                        ViewHolder.this.setUser(ViewHolder.this.user);
                        UsersViewAdapter.this.db.usersDao().delete(ViewHolder.this.user);
                    }else{
                        ViewHolder.this.user.changeStar();
                        ViewHolder.this.setUser(ViewHolder.this.user);
                        UsersViewAdapter.this.db.usersDao().insert(ViewHolder.this.user);
                    }
                }
            });
        }

        public void setUser(User user){
            this.user = user;
            this.userView.setText(user.getName());
            if (this.user.getStar()){
                ButtonStar.setImageResource(android.R.drawable.btn_star_big_on);
            }else{
                ButtonStar.setImageResource(android.R.drawable.btn_star_big_off);
            }
        }


        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, PersonDetailActivity.class);
            intent.putExtra("user_name", this.user.getName());
            intent.putExtra("user_photoURL", this.user.getPhotoURL());
            intent.putExtra("user_courses", (Serializable) this.user.getCourses());
            intent.putExtra("user_id", String.valueOf(this.user.getId()));
            context.startActivity(intent);
        }
    }
}
