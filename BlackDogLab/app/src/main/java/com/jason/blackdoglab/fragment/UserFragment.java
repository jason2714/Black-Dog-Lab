package com.jason.blackdoglab.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jason.blackdoglab.Player;
import com.jason.blackdoglab.R;
import com.jason.blackdoglab.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Player player;
    private ImageView mImgPortrait,mBgMainUser;
    private TextView mTvUserName, mTvUserIdentity;
    private SeekBar mSbSoundVolume;
    private AudioManager audioManager;
    private VolumeReceiver volumeReceiver;

    public UserFragment() {
        player = new Player();
    }

    public UserFragment(Player player) {
        this.player = player;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void initView(View view) {
        mImgPortrait = view.findViewById(R.id.img_portrait);
        mTvUserName = view.findViewById(R.id.tv_user_name);
        mTvUserIdentity = view.findViewById(R.id.tv_user_identity);
        mSbSoundVolume = view.findViewById(R.id.sb_sound_volume);
        audioManager = ((AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE));
        mBgMainUser = view.findViewById(R.id.bg_main_user);

        Utils.setLog(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
        Utils.setLog(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) + "");
        mSbSoundVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        mImgPortrait.setImageResource(player.getCharacterDrawable());
        mTvUserName.setText(player.getPlayerName());
    }

    @Override
    protected ImageView getBgImgView() {
        return mBgMainUser;
    }

    @Override
    protected int getBgDrawableID() {
        return R.drawable.bg_user;
    }

    @Override
    protected void initListener() {
        super.initListener();
        //initial listener
        mSbSoundVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int volume, boolean fromUser) {
                Utils.setLog(volume + "/15");
                //系統音量和媒體音量同時更新
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //註冊broadcast receiver
        volumeReceiver = new VolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        getActivity().registerReceiver(volumeReceiver, filter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initial view

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(volumeReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    private class VolumeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mSbSoundVolume.setProgress(currentVolume);
            }
        }
    }
}