package com.jason.blackdoglab.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jason.blackdoglab.R;
import com.jason.blackdoglab.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView mBgMainNote;
    private LinearLayout mLlNote;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
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
        mBgMainNote = view.findViewById(R.id.bg_main_note);
        mLlNote = view.findViewById(R.id.ll_note);
        addTextBox("陪伴者也需要人陪伴","哈囉ㄚㄚㄚㄚㄚㄚㄚㄚㄚㄚ",true);
        addTextBox("陪伴者也需要人陪伴","哈囉ㄚㄚㄚㄚㄚㄚㄚㄚㄚㄚ",false);
        addTextBox("陪伴者也需要人陪伴","哈囉ㄚㄚㄚㄚㄚㄚㄚㄚㄚㄚ",false);
        addTextBox("陪伴者也需要人陪伴","哈囉ㄚㄚㄚㄚㄚㄚㄚㄚㄚㄚ",false);
        addTextBox("陪伴者也需要人陪伴","哈囉ㄚㄚㄚㄚㄚㄚㄚㄚㄚㄚ",false);
    }

    @Override
    protected ImageView getBgImgView() {
        return mBgMainNote;
    }

    @Override
    protected int getBgDrawableID() {
        return R.drawable.bg_note;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO initial view here
        //initial view
        //initial listener
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    private void addTextBox(String title, String content,boolean isFirstBox) {
        //container
        LinearLayout ctLinearLayout = new LinearLayout(getContext());
        ctLinearLayout.setOrientation(LinearLayout.VERTICAL);
        ctLinearLayout.setBackgroundResource(R.drawable.bg_note_box);
        LinearLayout.LayoutParams ctLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
        int containerMP = (int) Utils.convertDpToPixel(getContext(), 20);
        if(isFirstBox)
            ctLayoutParams.setMargins(containerMP, containerMP, containerMP, containerMP);
        else
            ctLayoutParams.setMargins(containerMP, 0, containerMP, containerMP);
        ctLinearLayout.setLayoutParams(ctLayoutParams);
        ctLinearLayout.setElevation((int) Utils.convertDpToPixel(getContext(), 10));
        ctLinearLayout.setPadding(containerMP, containerMP, containerMP, containerMP);

        //title
        int titleMarginBottom = (int) Utils.convertDpToPixel(getContext(), 10);
        TextView titleText = new TextView(getContext());
        titleText.setTextColor(getResources().getColor(Utils.getAttrID(getContext(), R.attr.colorPrimary, Utils.RESOURCE_ID)));
        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        titleLayoutParams.setMargins(0, 0, 0, titleMarginBottom);
        titleText.setLayoutParams(titleLayoutParams);
        titleText.setTextSize(20);
        titleText.setLetterSpacing(0.1f);
        titleText.setText(title);

        //text
        TextView contentText = new TextView(getContext());
        contentText.setTextColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams contentLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        contentText.setLayoutParams(contentLayoutParams);
        contentText.setTextSize(16);
        contentText.setLetterSpacing(0.1f);
        contentText.setText(content);
        //set in container
        ctLinearLayout.addView(titleText);
        ctLinearLayout.addView(contentText);
        //set in wrapper
        mLlNote.addView(ctLinearLayout);
    }
}