package com.milushifa.miplayer.ui.fragment.tfragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.milushifa.miplayer.R;
import com.milushifa.miplayer.adapter.AlbumAdapter;
import com.milushifa.miplayer.adapter.TrackAdapter;
import com.milushifa.miplayer.media.loader.AlbumLoader;
import com.milushifa.miplayer.media.loader.TracksLoader;
import com.milushifa.miplayer.media.model.ModelType;
import com.milushifa.miplayer.ui.fragment.tfragment.backstack.BackStack;
import com.milushifa.miplayer.ui.fragment.tfragment.backstack.FragmentTransmitter;


public class ExpanderFragment extends Fragment {
    private ImageView trackAlbumArtExpander;
    private TextView titleExpander, detailsExpander;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TrackAdapter mTrackAdapter;

    private static String mModelType;
    private static long mModelId;

    private FragmentTransmitter mFragmentTransmitter;

    public ExpanderFragment(FragmentTransmitter mFragmentTransmitter) {
        this.mFragmentTransmitter = mFragmentTransmitter;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View expanderFragmentView = inflater.inflate(R.layout.fragment_expander, container, false);
        initComponents(expanderFragmentView);

        mModelType = getArguments().getString(ModelType.MODEL_TYPE);
        mModelId = getArguments().getLong(ModelType.MODEL_ID);

        return expanderFragmentView;
    }
    private void initComponents(View rootView){
        trackAlbumArtExpander = rootView.findViewById(R.id.trackAlbumArtExpander);
        titleExpander = rootView.findViewById(R.id.titleExpander);
        detailsExpander = rootView.findViewById(R.id.detailsExpander);

        // Temporary
        mToolbar = rootView.findViewById(R.id.toolBarExpander);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        mRecyclerView = rootView.findViewById(R.id.recyclerViewExpander);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        new LoadTracks().execute();
    }


    public class LoadTracks extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if (getActivity() != null) {
                switch(mModelType){
                    case ModelType.ALBUM:
                        mTrackAdapter = new TrackAdapter(getContext(), new TracksLoader().getAllSongsFromAlbum(getContext(), mModelId), mFragmentTransmitter);
                        break;
                    case ModelType.ARTIST:
                        mTrackAdapter = new TrackAdapter(getContext(), new TracksLoader().getAllSongsByArtist(getContext(), mModelId), mFragmentTransmitter);
                        break;
                    case ModelType.GENRE:
                        mTrackAdapter = new TrackAdapter(getContext(), new TracksLoader().getAllSongsByGenre(getContext(), mModelId), mFragmentTransmitter);
                        break;
                }
            }
            return "Execute";
        }

        @Override
        protected void onPostExecute(String s) {
            if (getActivity() != null) {
                mRecyclerView.setAdapter(mTrackAdapter);
            }
        }
    }
}
