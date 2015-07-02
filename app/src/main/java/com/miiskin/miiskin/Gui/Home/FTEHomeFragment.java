package com.miiskin.miiskin.Gui.Home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.miiskin.miiskin.Data.UserInfo;
import com.miiskin.miiskin.Data.UserManager;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Preferences;
import com.miiskin.miiskin.Storage.Task.SaveUserInfoTask;
import com.miiskin.miiskin.Storage.Task.TaskManager;

import java.util.UUID;

/**
 * Created by Newshka on 23.06.2015.
 */
public class FTEHomeFragment extends Fragment implements TaskManager.DataChangeListener {

    private RadioButton mFemaleRadioButton;
    private RadioButton mMaleRadioButton;
    private ImageView mMaleImageView;
    private ImageView mFemaleImageView;
    private static final String USER_INFO_DATA_TAG = "USER_INFO_DATA_TAG ";
    private static final String TASK_ID = "TASK_ID";
    private String mTaskId;
    private UserInfo mUserInfo;

    public interface FteCompleteListener {
        public void onFteCompleteDonePressed() ;
    }

    public static FTEHomeFragment newInstance() {
        FTEHomeFragment fragment = new FTEHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null)  {
            mUserInfo = (UserInfo)savedInstanceState.getSerializable(USER_INFO_DATA_TAG);
            mTaskId = savedInstanceState.getString(TASK_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(USER_INFO_DATA_TAG, mUserInfo);
        outState.putSerializable(TASK_ID, mTaskId);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFemaleRadioButton.isChecked()) {
            mFemaleImageView.setSelected(true);
        }
        if (mMaleRadioButton.isChecked()) {
            mMaleImageView.setSelected(true);
        }
        TaskManager.getInstance(getActivity()).addDataChangeListener(this);
        updateUi();
    }

    @Override
    public void onPause() {
        super.onPause();
        TaskManager.getInstance(getActivity()).removeDataChangeListener(this);
    }

    @Override
    public void onDataChanged(String dataId) {
        if (dataId.equals(mTaskId)) {
            updateUi();
        }
    }

    private void updateUi() {
        mUserInfo = (UserInfo)TaskManager.getInstance(getActivity().getApplicationContext()).getDataById(mTaskId);
        if (mUserInfo != null && mUserInfo.userId != null) {
            UserManager.getInstance().setUserInfo(mUserInfo);
            FteCompleteListener fteCompleteListener = (FteCompleteListener)getActivity();
            fteCompleteListener.onFteCompleteDonePressed();
        } else {
            mUserInfo = new UserInfo();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fte_home,  container, false);
    }

    private String[] populateYearOfBirth() {
        int from = Preferences.UserInfo.DATE_OF_BIRTH_FROM;
        int to = Preferences.UserInfo.DATE_OF_BIRTH_TO;
        String arrayOfBirth[] = new String[to - from + 1];
        for(int i = 0; i <= to - from; i++) {
            arrayOfBirth[i] = String.valueOf(from + i);
        }
        return arrayOfBirth;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = (Spinner) view.findViewById(R.id.year_of_birth);

        final ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, populateYearOfBirth());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String dateOfBirth = (String) adapter.getItem(position);
                setDateOfBirth(dateOfBirth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMaleRadioButton = (RadioButton)view.findViewById(R.id.male_button);
        mFemaleRadioButton = (RadioButton)view.findViewById(R.id.female_button);
        mFemaleImageView = (ImageView)view.findViewById(R.id.femaleImageView);
        mMaleImageView = (ImageView)view.findViewById(R.id.maleImageView);

        mMaleRadioButton.setOnClickListener(genderClickListener);
        mFemaleRadioButton.setOnClickListener(genderClickListener);

    }

    private  View.OnClickListener genderClickListener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = ((RadioButton) v).isChecked();

            switch(v.getId()) {
                case R.id.male_button:
                    if (checked) {
                        setGender(UserInfo.MALE);
                        mMaleImageView.setSelected(true);
                        mFemaleImageView.setSelected(false);
                        mFemaleRadioButton.setChecked(false);
                    }
                    break;
                case R.id.female_button:
                    if (checked) {
                        setGender(UserInfo.FEMALE);
                        mMaleImageView.setSelected(false);
                        mFemaleImageView.setSelected(true);
                        mMaleRadioButton.setChecked(false);
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_fte_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                saveUserInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setGender(String gender) {
        mUserInfo.gender = gender;
    }

    private void setDateOfBirth(String dateOfBirth) {
        mUserInfo.birth_date = dateOfBirth;
    }

    private void saveUserInfo() {
        mTaskId = UUID.randomUUID().toString();
        TaskManager.getInstance(getActivity().getApplicationContext()).executeTask(new SaveUserInfoTask(getActivity().getApplicationContext(), new Object[] {mUserInfo}), mTaskId);
    }
}
