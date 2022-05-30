package com.example.encyclopedia.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encyclopedia.ArticleAdapter;
import com.example.encyclopedia.R;
import com.example.encyclopedia.TopicAdapter;
import com.example.encyclopedia.UserAdapter;
import com.example.encyclopedia.db.EncyclopediaRoomDatabase;
import com.example.encyclopedia.view_model.ArticleViewModel;
import com.example.encyclopedia.view_model.TopicViewModel;
import com.example.encyclopedia.view_model.UserViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private MaterialToolbar materialToolbar;
    private TextView headerTextView;
    private NavigationView navigationView;
    private FloatingActionButton fab;

    private UserViewModel userViewModel;
    private TopicViewModel topicViewModel;
    private ArticleViewModel articleViewModel;

    private TopicAdapter topicAdapter;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        materialToolbar = findViewById(R.id.topAppBar);
        navigationView = findViewById(R.id.navigationView);

        headerTextView = navigationView.getHeaderView(0).findViewById(R.id.header_usernameText);
        fab = findViewById(R.id.floatingActionButtonAddNewArticle);
        recyclerView = findViewById(R.id.contextMain_listOfArticles);

        articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        topicViewModel = new ViewModelProvider(this).get(TopicViewModel.class);

        setupMenu();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void setupMenu() {

        materialToolbar.setNavigationOnClickListener(v -> drawerLayout.open());
        navigationView.setNavigationItemSelectedListener(this);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewArticleActivity.class);
            startActivity(intent);
        });

        setupMenuLayout();
    }

    private void setupMenuLayout() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        topicAdapter = new TopicAdapter(recyclerView, this, articleViewModel, topicViewModel);
        recyclerView.setAdapter(topicAdapter);
        topicViewModel.getAll().observe(this,
                topics -> topicAdapter.setTopics(topics));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_logOut) {
            if (EncyclopediaRoomDatabase.loggedInUser != null) {
                userViewModel.updateLogInStatus(EncyclopediaRoomDatabase.loggedInUser.uid, 0);
                EncyclopediaRoomDatabase.loggedInUser = null;
                setupMenuLayout();
                updateViewDueToIsLoggedIn();
            } else {
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
            }
        } else if (itemId == R.id.menu_item_addNewTopic) {
            Intent intent = new Intent(this, NewTopicActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_item_encyclopedia) {
            setupMenuLayout();
        } else if (itemId == R.id.menu_item_checkUsers) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            UserAdapter userAdapter = new UserAdapter(recyclerView, this, articleViewModel, userViewModel);
            recyclerView.setAdapter(userAdapter);
            userViewModel.getAll().observe(this, userAdapter::setUsers);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (recyclerView.getLayoutManager().getClass() != GridLayoutManager.class) {
            setupMenuLayout();
        }
    }

    @Override
    public void onResume() {
        updateViewDueToIsLoggedIn();
        super.onResume();
    }

    private void updateViewDueToIsLoggedIn() {
        userViewModel.getLoggedIn().observe(this, user -> {
            EncyclopediaRoomDatabase.loggedInUser = user;
            if (EncyclopediaRoomDatabase.loggedInUser == null) {

                navigationView.getMenu().findItem(R.id.menu_item_logOut).setTitle(R.string.mainActivity_logInButton);
                navigationView.getMenu().findItem(R.id.menu_item_addNewTopic).setVisible(false);
                navigationView.getMenu().findItem(R.id.menu_item_checkUsers).setVisible(false);

                fab.setVisibility(View.INVISIBLE);

                headerTextView.setText(R.string.mainActivity_statusNotLoggedIn);

            } else {
                navigationView.getMenu().findItem(R.id.menu_item_logOut).setTitle(R.string.mainActivity_logOutButton);
                if (user.role.equals("admin")) {
                    navigationView.getMenu().findItem(R.id.menu_item_addNewTopic).setVisible(true);
                    navigationView.getMenu().findItem(R.id.menu_item_checkUsers).setVisible(true);
                } else {
                    navigationView.getMenu().findItem(R.id.menu_item_addNewTopic).setVisible(false);
                    navigationView.getMenu().findItem(R.id.menu_item_checkUsers).setVisible(false);
                }

                fab.setVisibility(View.VISIBLE);

                headerTextView.setText(EncyclopediaRoomDatabase.loggedInUser.username);
            }
        });
    }
}