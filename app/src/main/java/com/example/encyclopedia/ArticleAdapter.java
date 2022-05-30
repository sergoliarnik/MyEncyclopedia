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
import androidx.recyclerview.widget.RecyclerView;

import com.example.encyclopedia.activity.TextContentActivity;
import com.example.encyclopedia.entity.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private List<Article> articles = new ArrayList<>();
    Context context;

    public ArticleAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);
        return new ArticleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        Article article = articles.get(position);
        holder.titleTextView.setText(article.title);
        holder.secondaryTextView.setText(article.text);

        String filePath = context.getString(R.string.articleImagePath) + article.title;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        holder.imageView.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TextContentActivity.class);
            intent.putExtra("articleTitle", article.title);
            v.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setArticles(List<Article> articles){
        this.articles = articles;
        notifyDataSetChanged();
    }

    class ArticleHolder extends RecyclerView.ViewHolder{



        private TextView titleTextView;
        private TextView secondaryTextView;

        private ImageView imageView;

        public ArticleHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.articleItem_titleText);
            secondaryTextView = itemView.findViewById(R.id.articleItem_secondText);

            imageView = itemView.findViewById(R.id.articleItem_image);

        }
    }

}
