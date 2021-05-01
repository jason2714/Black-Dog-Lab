package com.jason.blackdoglab.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jason.blackdoglab.FileController;
import com.jason.blackdoglab.Player;
import com.jason.blackdoglab.R;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;

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
    private ImageView mImgPortrait, mBgMainUser;
    private TextView mTvUserName, mTvUserIdentity;
    private ImageView[] mImgPoints;
    private SeekBar mSbSoundVolume;
    private AudioManager audioManager;
    private VolumeReceiver volumeReceiver;
    private int[] dotDrawables = {R.drawable.icon_point_accompany, R.drawable.icon_point_energy, R.drawable.icon_point_exp};
    private int[] imgPointDrawables = {R.drawable.icon_accompany, R.drawable.icon_energy, R.drawable.icon_exp};
    private LinearLayout[] mLlPoints;
    private int pointPos = -1;
    private String[] pointTextArray;
    private int[] pointTextColorIDs = {R.color.brown1, R.color.blue1, R.color.green1};
    private String[] fcName = {"Accompany", "Energy", "EXP"};
    private LinearLayout mLlPointLog;

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
        //initial point info
        mImgPoints = new ImageView[]{view.findViewById(R.id.img_accompany),
                view.findViewById(R.id.img_energy), view.findViewById(R.id.img_exp)};
        mLlPoints = new LinearLayout[]{view.findViewById(R.id.ll_accompany),
                view.findViewById(R.id.ll_energy), view.findViewById(R.id.ll_exp)};
        for (int i = 0; i < mImgPoints.length; i++)
            setImageDrawableFit(mImgPoints[i], imgPointDrawables[i]);
        pointTextArray = getResources().getStringArray(R.array.user_points_array);
        //point log
        mLlPointLog = view.findViewById(R.id.ll_point_log);
        //set sound broadcast receiver
        Utils.setLog(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + "");
        Utils.setLog(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) + "");
        mSbSoundVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        setImageDrawableFit(mImgPortrait, player.getCharacterDrawable());
        mTvUserName.setText(player.getPlayerName());
    }

    @Override
    protected ImageView getBgImgView() {
        return mBgMainUser;
    }

    @Override
    protected int getBgDrawableID() {
        return Utils.getAttrID(getContext(), R.attr.bg_user, Utils.RESOURCE_ID);
    }

    @Override
    protected void initListener() {
        super.initListener();
        //initial listener
        for (ImageView imageView : mImgPoints) {
            imageView.setOnClickListener(this);
        }
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
        new Thread(() -> updatePoint(0)).start();
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

    @Override
    public void onClick(View v) {
        int newPointPos = -1;
        switch (v.getId()) {
            case R.id.img_accompany:
                newPointPos = 0;
                break;
            case R.id.img_energy:
                newPointPos = 1;
                break;
            case R.id.img_exp:
                newPointPos = 2;
                break;
            default:
                break;
        }
        updatePoint(newPointPos);
    }

    private void updatePoint(int newPointPos) {
        Utils.setLog(newPointPos + "-" + pointPos);
        if (newPointPos == -1 || newPointPos == pointPos)
            return;
        Utils.setLog("------------------------------------------------");
        //dot
        ImageView dot = new ImageView(getContext());
        int dotSize = getResources().getDimensionPixelOffset(R.dimen.user_point_dot_size);
        int dotMarginVertical = getResources().getDimensionPixelOffset(R.dimen.user_dot_margin_vertical);
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(dotSize, dotSize);
        dotParams.setMargins(0, dotMarginVertical, 0, dotMarginVertical);
        //text
        TextView pointText = new TextView(getContext());
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pointText.setTextSize(16);
        pointText.setLetterSpacing(0.15f);
        pointText.setTypeface(pointText.getTypeface(), Typeface.BOLD);
        pointText.setTextColor(getResources().getColor(pointTextColorIDs[newPointPos]));
        pointText.setText(pointTextArray[newPointPos]);
        //add to LinearLayout
        mHandler.post(() -> {
            mLlPoints[newPointPos].addView(dot, 1, dotParams);
            mLlPoints[newPointPos].addView(pointText, 2, textParams);
            setImageDrawableFit(dot, dotDrawables[newPointPos]);
        });
        Utils.setLog("old pos = " + pointPos + ",new pos = " + newPointPos);
        //set file point data
        FileController fileController = new FileController(getContext(), fcName[newPointPos]);
        if (fileController.fileExist()) {
            try {
                loadFileLog(fileController.readFileSplit());
            } catch (IOException e) {
                e.printStackTrace();
                Utils.setLog(e.getMessage());
            }
        } else {
            try {
                fileController.write("");
                Utils.setLog("Create File Success");
            } catch (IOException e) {
                e.printStackTrace();
                Utils.setLog(e.getMessage());
            }
        }
        //remove old dot&text
        if (pointPos != -1)
            mLlPoints[pointPos].removeViews(1, 2);
        pointPos = newPointPos;
    }

    private void loadFileLog(String[] fileLogs) {
        if (fileLogs[0].equals("")) {
            Utils.setLog("User File Is Empty");
            return;
        } else {
            for (String log : fileLogs) {
            }
        }
    }

}