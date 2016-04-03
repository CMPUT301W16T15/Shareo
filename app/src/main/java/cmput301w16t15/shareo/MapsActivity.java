package cmput301w16t15.shareo;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public final static String LOCATION_KEY = "maps";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }
    public void onZoom(View view){
        if (view.getId()==R.id.BzoomIn){
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if (view.getId()==R.id.BzoomOut){
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = getIntent();
        String location = intent.getStringExtra(LOCATION_KEY);
        TextView textView = new TextView(this);
        textView.setText(location);
        Log.v("TAG", location);
        if (location != null || location.equals("")){
            new Task(new Geocoder(this)).execute(location);
        }
    }

    private class Task extends AsyncTask<String, Void, List<Address>> {

        private final Geocoder mGeo;

        public Task(Geocoder g) {
            this.mGeo = g;
        }

        @Override
        protected List<Address> doInBackground(String... params) {
            List<Address> a = null;
            try {
                a = mGeo.getFromLocationName(params[0], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return a;
        }

        @Override
        public void onPostExecute(List<Address> o) {
            if (o != null) { // ddin't find address
                Address address = o.get(0);
                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Meeting Place"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }
}
