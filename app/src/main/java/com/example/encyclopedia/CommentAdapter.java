package com.example.encyclopedia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encyclopedia.activity.TextContentActivity;
import com.example.encyclopedia.entity.Article;
import com.example.encyclopedia.entity.Comment;
import com.example.encyclopedia.view_model.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<Comment> comments = new ArrayList<>();
    UserViewModel userViewModel;
    AppCompatActivity context;

    public CommentAdapter(AppCompatActivity context, UserViewModel userViewModel) {
        this.context = context;
        this.userViewModel = userViewModel;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new CommentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.commentTextTextView.setText(comment.text);
        userViewModel.getById(comment.userId).observe(context, receivedUser -> {
            holder.commentAuthorTextView.setText(receivedUser.username);
        });
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        private TextView commentTextTextView;
        private TextView commentAuthorTextView;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            commentAuthorTextView = itemView.findViewById(R.id.commentItem_commentAuthor);
            commentTextTextView = itemView.findViewById(R.id.commentItem_commentText);
        }
    }

}
