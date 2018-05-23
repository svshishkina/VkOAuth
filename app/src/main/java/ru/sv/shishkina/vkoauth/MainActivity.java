package ru.sv.shishkina.vkoauth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

import ru.sv.shishkina.vkoauth.model.Photo;
import ru.sv.shishkina.vkoauth.model.User;
import ru.sv.shishkina.vkoauth.network.ApiService;

public class MainActivity extends AppCompatActivity {
    private ImageView ivPhoto;
    private FloatingActionButton fabAuth;
    private RecyclerView rvPhotos;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private PhotosAdapter photosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPhoto = findViewById(R.id.iv_user_photo);
        fabAuth = findViewById(R.id.fab_auth);
        rvPhotos = findViewById(R.id.rv_content);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        initView();

        fabAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiService.AUTH_URL));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String url = intent.getDataString();
        if (null == url) {
            Toast.makeText(MainActivity.this, "Oops!", Toast.LENGTH_SHORT).show();
            return;
        }
        url = url.replace('#', '?');

        final String accessToken = Uri.parse(url).getQueryParameter("access_token");
        final long userId = Long.valueOf(Uri.parse(url).getQueryParameter("user_id"));

        if (null != accessToken) {
            ApiService.getInstance().setAccessToken(accessToken);
            ApiService.getInstance().setUserId(userId);
        }

        fabAuth.setVisibility(View.GONE);
        new GetUserInfoAsyncTask(this).execute();
        new GetUserPhotosAsyncTask(this).execute();
    }

    public void showPhoto(String url) {
        Glide.with(this).load(url).into(ivPhoto);
    }

    public void setTitle(User user) {
        collapsingToolbarLayout.setTitle(String.format(getString(R.string.user_name_format), user.getFirstName(), user.getLastName()));
    }

    public void setPhotos(List<Photo> photos) {
        photosAdapter.setPhotos(photos);
    }

    private void initView() {
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        photosAdapter = new PhotosAdapter(this);

        rvPhotos.addItemDecoration(new PaddingItemDecorator(
                getResources(),
                R.dimen.spacing_tiniest,
                R.dimen.spacing_standart,
                R.dimen.spacing_standart));
        rvPhotos.setLayoutManager(new LinearLayoutManager(this));
        rvPhotos.setAdapter(photosAdapter);
    }

    public static class GetUserInfoAsyncTask extends AsyncTask<Void, Void, User> {
        private WeakReference<Activity> activity;

        public GetUserInfoAsyncTask(Activity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected User doInBackground(Void... voids) {
            return ApiService.getInstance().getUserInfo();
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            ((MainActivity) activity.get()).setTitle(user);
            String photoUrl = user.getPhotoUrl();
            ((MainActivity) activity.get()).showPhoto(photoUrl);
        }
    }

    public static class GetUserPhotosAsyncTask extends AsyncTask<Void, Void, List<Photo>> {
        private WeakReference<Activity> activity;

        public GetUserPhotosAsyncTask(Activity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected List<Photo> doInBackground(Void... voids) {
            return ApiService.getInstance().getUserPhotos();
        }

        @Override
        protected void onPostExecute(List<Photo> photos) {
            super.onPostExecute(photos);
            ((MainActivity) activity.get()).setPhotos(photos);
        }
    }
}
