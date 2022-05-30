package com.example.encyclopedia;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.entity.Topic;
import com.example.encyclopedia.view_model.ArticleViewModel;
import com.example.encyclopedia.view_model.TopicViewModel;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicHolder> {

    private List<Topic> topics = new ArrayList<>();
    private RecyclerView recyclerView;
    private AppCompatActivity context;
    private ArticleViewModel articleViewModel;
    private TopicViewModel topicViewModel;


    public TopicAdapter(RecyclerView recyclerView, AppCompatActivity context,
                        ArticleViewModel articleViewModel, TopicViewModel topicViewModel) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.articleViewModel = articleViewModel;
        this.topicViewModel = topicViewModel;
    }

    @NonNull
    @Override
    public TopicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.topic_item, parent, false);

        return new TopicHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicHolder holder, int position) {
        Topic topic = topics.get(position);
        holder.titleTextView.setText(topic.name);
        holder.secondaryTextView.setText(topic.name);

        String filePath = "/sdcard/EncyclopediaMedia/" + topic.name;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        holder.imageView.setImageBitmap(bitmap);

        if (EncyclopediaRoomDatabase.loggedInUser != null && "admin".equals(EncyclopediaRoomDatabase.loggedInUser.role)) {
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else{
            holder.deleteButton.setVisibility(View.GONE);
        }

        holder.deleteButton.setOnClickListener(v -> {
            topicViewModel.delete(topic);
        });

        holder.itemView.setOnClickListener(v -> {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ArticleAdapter articleAdapter = new ArticleAdapter(context);
            String topicName = topic.name;
            recyclerView.setAdapter(articleAdapter);
            int topicId = topicViewModel.getByName(topicName).id;
            articleViewModel.getByTopicId(topicId).observe(context,
                    articles -> articleAdapter.setArticles(articles));

        });
    }


    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    public class TopicHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView secondaryTextView;
        private ImageView imageView;
        private Button deleteButton;

        public TopicHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.topicItem_titleText);
            secondaryTextView = itemView.findViewById(R.id.topicItem_secondText);
            imageView = itemView.findViewById(R.id.topicItem_image);
            deleteButton = itemView.findViewById(R.id.topicItem_deleteButton);
        }

    }

}
