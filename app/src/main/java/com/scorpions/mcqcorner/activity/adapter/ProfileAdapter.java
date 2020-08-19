package com.scorpions.mcqcorner.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.scorpions.mcqcorner.R;
import com.scorpions.mcqcorner.model.ProfileModel;

import java.util.ArrayList;

public class ProfileAdapter extends BaseAdapter {
    Context context;
    ArrayList<ProfileModel> arrayList;

    public ProfileAdapter(Context context, ArrayList<ProfileModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_edit_profile,null);

        EditText edtUserName = view.findViewById(R.id.aSetting_edtUsername);
        EditText edtWebsite = view.findViewById(R.id.aSetting_edtWebsite);
        EditText edtMobileNo = view.findViewById(R.id.aSetting_edtMobileNo);
        EditText edtEmail = view.findViewById(R.id.aSetting_edtEmail);

        ProfileModel profileModel = arrayList.get(i);

        edtUserName.setText(profileModel.getUsername());
        edtWebsite.setText(profileModel.getWebsite());
        edtMobileNo.setText(profileModel.getMobileno());
        edtEmail.setText(profileModel.getEmail());

        return view;
    }
}
