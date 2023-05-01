package com.example.lucianodsepulveda.apppasajero.model;

import android.app.Fragment;
import android.os.Bundle;

import com.android.volley.RequestQueue;

public class BaseVolleyFragment extends Fragment {
    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.volley = VolleyS.getInstance(getActivity().getApplicationContext());
        this.fRequestQueue = volley.getRequestQueue();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fRequestQueue != null) {
            fRequestQueue.cancelAll(this);
        }
    }
}
