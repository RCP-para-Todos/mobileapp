//package com.jk.rcp.main.activities;
//
//
//import android.Manifest;
//import android.app.ProgressDialog;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.jk.rcp.R;
//import com.jk.rcp.main.data.adapter.EarthquakeListAdapter;
//import com.jk.rcp.main.data.model.inpresList.Earthquake;
//import com.jk.rcp.main.data.model.inpresList.Feed;
//import com.jk.rcp.main.data.remoteXML.APIService;
//import com.jk.rcp.main.data.remoteXML.ApiUtils;
//import com.jk.rcp.main.utils.Constants;
//import com.jk.rcp.main.utils.DeviceUtils;
//import com.jk.rcp.main.utils.EventManager;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
//public class OfficialHistoryContentFragment extends Fragment {
//    private static final String TAG = "OfficialHistory";
//
//    private ListView earthquakeList;
//    private APIService mAPIService;
//    private EarthquakeListAdapter earthquakeListAdapter;
//    private FusedLocationProviderClient fusedLocationClient;
//    private Location lastKnownLocation;
//
//    public static OfficialHistoryContentFragment newInstance() {
//        OfficialHistoryContentFragment frag = new OfficialHistoryContentFragment();
//        return frag;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
//            Bundle savedInstanceState) {
//        // Pongo por default ese contenido
//        View layout = inflater.inflate(R.layout.activity_official_history, container, false);
//
//        earthquakeList = layout.findViewById(R.id.earthquakeList);
//        earthquakeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//
//                //Preparo los parametros para enviar al fragment de detalle
//                String depth = ((TextView) view.findViewById(R.id.depth)).getText().toString();
//                String mainInfo = ((TextView) view.findViewById(R.id.resume)).getText().toString();
//                String distance = ((TextView) view.findViewById(R.id.distance)).getText().toString();
//                String detail = ((TextView) view.findViewById(R.id.placeReference)).getText().toString();
//                String dateTime = ((TextView) view.findViewById(R.id.datetime)).getText().toString();
//                String latitude = ((TextView) view.findViewById(R.id.latitude)).getText().toString();
//                String longitude = ((TextView) view.findViewById(R.id.longitude)).getText().toString();
//
//                DetailFragment nextFrag = new DetailFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("depth", depth);
//                bundle.putString("mainInfo", mainInfo);
//                bundle.putString("distance", distance);
//                bundle.putString("detail", detail);
//                bundle.putString("dateTime", dateTime);
//                bundle.putString("latitude", latitude);
//                bundle.putString("longitude", longitude);
//                nextFrag.setArguments(bundle);
//
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.home_content, nextFrag, "detailFragment")
//                        .addToBackStack("official-history")
//                        .commit();
//            }
//        });
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
//
//        mAPIService = ApiUtils.getAPIService();
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_COARSE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null) {
//                                lastKnownLocation = location;
//                                EventManager.registerEvent(Constants.GPS_ACQUIRED);
//                            }
//                        }
//                    });
//        }
//        if (DeviceUtils.isDeviceOnline(getActivity())) {
//            getDataFromINPRES();
//        } else {
//            Toast.makeText(getActivity(), "No hay internet", Toast.LENGTH_SHORT).show();
//        }
//        return layout;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    private void getDataFromINPRES() {
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Obteniendo datos...");
//        progressDialog.show();
//        EventManager.registerEvent(Constants.GET_INPRES_DATA_ACTIVATED);
//        mAPIService.getEarthquakeData().enqueue(new Callback<Feed>() {
//            @Override
//            public void onResponse(Call<Feed> call, Response<Feed> response) {
//                if (response.isSuccessful()) {
//                    Feed feed = response.body();
//
//                    for (Earthquake item : feed.getEarthquakeList()) {
//                        String[] split = item.getTitle().split(" -- ");
//                        item.setDate(split[1]);
//                        item.setTime(split[2]);
//                        item.setLatitude(split[3]);
//                        item.setLongitude(split[4]);
//                        item.setMagnitude(split[5]);
//                        item.setDepth(split[6]);
//                        item.setProvince(split[7]);
//
//                        String[] splitReference = item.getDescription().split(" La magnitud");
//                        item.setPlaceReference(splitReference[0]);
//                    }
//
//                    earthquakeListAdapter = new EarthquakeListAdapter(getActivity(), feed.getEarthquakeList(), lastKnownLocation);
//                    earthquakeList.setAdapter(earthquakeListAdapter);
//                    progressDialog.dismiss();
//                } else {
//                    Log.i("XML ERROR", response.errorBody().toString());
//                }
//                EventManager.registerEvent(Constants.GET_INPRES_DATA_FINISHED);
//            }
//
//            @Override
//            public void onFailure(Call<Feed> call, Throwable t) {
//                Log.e(TAG, "Error al enviar el request.");
//                EventManager.registerEvent(Constants.GET_INPRES_DATA_FINISHED);
//            }
//        });
//    }
//}
//
