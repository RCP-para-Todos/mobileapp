package com.jk.rcp.main.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jk.rcp.R;

public class DetailFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "DetailFragment";

    MapView mapView;
    GoogleMap googleMap;
    TextView dateTimeTv;
    TextView detailTv;
    TextView distanceTv;
    TextView depthTv;
    TextView mainInfoTv;
    LatLng epicenter;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.activity_detail, container, false);
        Bundle bundle = this.getArguments();

        String mainInfo = bundle.getString("mainInfo");
        String distance = bundle.getString("distance");
        String depth = bundle.getString("depth");
        String latitude = bundle.getString("latitude");
        String longitude = bundle.getString("longitude");
        String detail = bundle.getString("detail");
        String dateTime = bundle.getString("dateTime");

        mapView = layout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        dateTimeTv = layout.findViewById(R.id.dateTimeTextView);
        mainInfoTv = layout.findViewById(R.id.mainInfoTextView);
        detailTv = layout.findViewById(R.id.detailTextView);
        depthTv = layout.findViewById(R.id.depthTextView);
        distanceTv = layout.findViewById(R.id.distanceTextView);

        mainInfoTv.setText(mainInfo + " (" + latitude + ", " + longitude + ")");
        dateTimeTv.setText(dateTime);
        detailTv.setText(detail);
        depthTv.setText(depth);
        distanceTv.setText(distance);
        epicenter = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));

        this.toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Detalle del sismo");

        return layout;
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        if (googleMap != null) {
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(epicenter, 10.0f));
            googleMap.addMarker(new MarkerOptions()
                    .position(epicenter).title("Epicentro")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .draggable(false).visible(true));
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

