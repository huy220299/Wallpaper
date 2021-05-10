package com.example.nvhuy.wallpaper.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.APIService.APIService;
import com.example.nvhuy.wallpaper.APIService.DataService;
import com.example.nvhuy.wallpaper.adapter.CustomerAdapter;
import com.example.nvhuy.wallpaper.model.Category;
import com.example.nvhuy.wallpaper.model.Image;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecentFragment extends Fragment {

    RecyclerView rRecent;
    ArrayList<Image> mImageList;
    ArrayList<Category> listCategory;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    CustomerAdapter customerAdapter;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int page = 0;


    public RecentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_recent, container, false);
        rRecent = viewGroup.findViewById(R.id.rRecent);

        swipeRefreshLayout = viewGroup.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            page=0;

            mImageList.clear();
            customerAdapter.notifyDataSetChanged();
            loadMore(page);
            swipeRefreshLayout.setRefreshing(false);
        });

        progressBar = viewGroup.findViewById(R.id.progressBar);
        listCategory = new ArrayList<>();

        listCategory.add(new Category("6","Super Heroes",R.drawable.heroes_banner));
        listCategory.add(new Category("2","Rate",R.drawable.rate_banner));
        listCategory.add(new Category("2","Minimal Wallpapers",R.drawable.minimal_banner));
        listCategory.add(new Category("12","Abstract Wallpaper",R.drawable.abstract_wallpaper));
        listCategory.add(new Category("9","Dope Wallpaper",R.drawable.dope_banner));
        listCategory.add(new Category("4","Cars",R.drawable.car_banner));
        listCategory.add(new Category("21","Movies & Web Series",R.drawable.movie_banner));
        mImageList = new ArrayList<>();
        customerAdapter = new CustomerAdapter(getContext(), mImageList, listCategory);
        rRecent.setAdapter(customerAdapter);
        rRecent.setHasFixedSize(true);


        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (customerAdapter.getItemViewType(position)) {
                    case CustomerAdapter.IS_HEADER:
                        return 2;
                    case CustomerAdapter.IS_NORMAL:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        rRecent.setLayoutManager(mLayoutManager);
        OverScrollDecoratorHelper.setUpOverScroll(rRecent, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);


        DataService dataservice = APIService.getService();
        Call<List<Image>> callback = dataservice.getRandomImage(String.valueOf(page));
        callback.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                ArrayList<Image> result = (ArrayList<Image>) response.body();
                assert result != null;
                mImageList.addAll(result);
                Log.v("test12333", "size " + mImageList.size());
                customerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Log.i("_______", new Gson().toJson(t));
            }
        });

        rRecent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            // Do pagination.. i.e. fetch new data
                            progressBar.setVisibility(View.VISIBLE);
                            new CountDownTimer(3000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }
                                public void onFinish() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }.start();
                            loading = true;
                            page++;
                            loadMore(page);
                        }
                    }
                }
            }
        });
        return viewGroup;
    }
    private void loadMore(int index) {
        DataService dataservice = APIService.getService();
        Call<List<Image>> callback = dataservice.getRandomImage(String.valueOf(index));

        callback.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                if (response.isSuccessful()) {

                    ArrayList<Image> result = (ArrayList<Image>) response.body();
                    if (result.size() > 0) {
                        //add loaded data
                        mImageList.addAll(result);
                    } else {
                        //result size 0 means there is no more data available at server
                        Toast.makeText(getContext(), "No More Data Available", Toast.LENGTH_LONG).show();
                    }
                    customerAdapter.notifyDataSetChanged();
                } else {
                    Log.e("TAG", " Load More Response Error " + String.valueOf(response.code()));
                }
            }
            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Log.e("TAG", " Load More Response Error " + t.getMessage());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void downloadDefaults(String filename, String downloadUrlOfImage, String fileType) {
        try {
            File direct = new File("/defaultWallpaper");
            if (!direct.exists()) {
                direct.mkdirs();
            }
            DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                    .setDestinationInExternalFilesDir(getContext(), "/defaultWallpaper", File.separator + filename + fileType);
            dm.enqueue(request);
        } catch (Exception e) {

            Log.e("!!!", e.getMessage());
        }
    }
}