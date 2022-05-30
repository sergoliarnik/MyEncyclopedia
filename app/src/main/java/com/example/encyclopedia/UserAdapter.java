package com.example.encyclopedia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.User;
import com.example.encyclopedia.view_model.ArticleViewModel;
import com.example.encyclopedia.view_model.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private List<User> users = new ArrayList<>();
    ArticleViewModel articleViewModel;
    UserViewModel userViewModel;

    private RecyclerView recyclerView;

    AppCompatActivity context;

    public UserAdapter(RecyclerView recyclerView, AppCompatActivity context,
                       ArticleViewModel articleViewModel,
                       UserViewModel userViewModel) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.articleViewModel = articleViewModel;
        this.userViewModel = userViewModel;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User user = users.get(position);
        holder.nameTextView.setText(user.username);
        articleViewModel.getByUserId(user.uid).observe(context, articles -> {
            holder.countOfArticlesTextView.setText("Count of articles: " + articles.size());
        });
        holder.countOfArticlesTextView.setOnClickListener(v -> {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ArticleAdapter articleAdapter = new ArticleAdapter(context);
            recyclerView.setAdapter(articleAdapter);
            articleViewModel.getByUserId(user.uid).observe(context,
                    articles -> articleAdapter.setArticles(articles));
        });
        holder.aSwitch.setChecked(user.role.equals("admin"));
        holder.aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String role = "default";
            if (isChecked) {
                role = "admin";
            }
            user.role = role;
            userViewModel.update(user);
        });

        holder.deleteUserButton.setOnClickListener(v -> {
            userViewModel.delete(user);
        });

        if (user.uid == EncyclopediaRoomDatabase.loggedInUser.uid) {
            holder.deleteUserButton.setVisibility(View.GONE);
            holder.aSwitch.setClickable(false);
        }
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    class UserHolder extends RecyclerView.ViewHolder {


        private TextView nameTextView;
        private TextView countOfArticlesTextView;
        private Button deleteUserButton;
        private Switch aSwitch;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.userItem_nameText);
            countOfArticlesTextView = itemView.findViewById(R.id.userItem_countOfArticlesText);
            aSwitch = itemView.findViewById(R.id.userItem_isAdminSwitch);
            deleteUserButton = itemView.findViewById(R.id.userItem_deleteUserButton);
        }
    }

}
