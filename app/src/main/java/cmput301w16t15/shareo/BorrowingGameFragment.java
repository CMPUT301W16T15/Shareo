package cmput301w16t15.shareo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mvc.PhotoModel;
import mvc.Thing;

/**
 * Created by Bradshaw on 2016-04-03.
 */
public class BorrowingGameFragment extends DialogFragment implements OnMapReadyCallback {

    private static String TAG ="Borrowing";

    private Integer mPositionIndex;
    private TextView textViewGameName;
    private TextView textViewDescription;
    private TextView textViewNumberPlayers;
    private TextView textViewCategory;
    private TextView textViewGameOwner;
    private TextView mTextViewAddGame;
    private ImageButton gameImage;
    private MapView mMapView;
    private GoogleMap googleMap;

    private PhotoModel gamePhoto;
    private String gameName;
    private String gameDescription;
    private String numberPlayers;
    private String category;
    private Thing mThing;

    private AlertDialog dialog = null;
    private AlertDialog.Builder builder = null;

    private Button mButtonDeleteImage;

    public BorrowingGameFragment() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.fragment_borrowed_game, null);
        mPositionIndex = getArguments().getInt("pos");
        mTextViewAddGame = (TextView) v.findViewById(R.id.textViewAddGame);
        textViewGameName = (TextView) v.findViewById(R.id.textViewGameName);
        textViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        textViewNumberPlayers = (TextView) v.findViewById(R.id.textViewNumberPlayers);
        textViewCategory = (TextView) v.findViewById(R.id.textViewCategory);
        textViewGameOwner = (TextView) v.findViewById(R.id.textViewOwner);
        gameImage = (ImageButton) v.findViewById(R.id.gamePicture);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mThing = (Thing) getArguments().getSerializable("myThing");
        populateFields();
        builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();

                    }
                });
        dialog = builder.show();

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        return dialog;
    }

    private void populateFields() {
        mTextViewAddGame.setText("Borrowed Game");
        textViewGameName.setText(textViewGameName.getText() + ": " + mThing.getName());
        textViewDescription.setText(textViewDescription.getText() + ": " + mThing.getDescription());
        textViewCategory.setText(textViewCategory.getText() + ": " + mThing.getCategory());
        textViewNumberPlayers.setText(textViewNumberPlayers.getText() + ": " + mThing.getNumberPlayers());
        textViewGameOwner.setText(textViewGameOwner.getText() + ": " + mThing.getOwnerID());
        textViewGameOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileInfo d = new UserProfileInfo();
                Bundle bundle = new Bundle();

                bundle.putString("userId", mThing.getOwnerID());
                d.setArguments(bundle);
                d.show(getActivity().getFragmentManager(), "user_profile");
            }
        });

        gameImage.setClickable(false);

        /**
         * If they have a photo --> Make it visible, else leave it in GONE state.
         */
        // photo is optional field
        if (mThing.getPhotoModel() != null) {
            gameImage.setImageBitmap(mThing.getPhotoModel().getPhoto());
            gamePhoto = mThing.getPhotoModel();
        }
        else
        {
            gamePhoto = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia, and move the camera.
        this.googleMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        this.googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
