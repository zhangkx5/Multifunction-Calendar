package com.example.kaixin.mycalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by kaixin on 2018/3/23.
 */

public class MeFragment extends Fragment {

    private LinearLayout personal, synchronize, setting;
    private TextView switch_account, logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        personal = (LinearLayout) view.findViewById(R.id.personal);
        synchronize = (LinearLayout) view.findViewById(R.id.synchronize);
        setting = (LinearLayout) view.findViewById(R.id.setting);
        switch_account = (TextView) view.findViewById(R.id.switch_account);
        logout = (TextView) view.findViewById(R.id.logout);

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
