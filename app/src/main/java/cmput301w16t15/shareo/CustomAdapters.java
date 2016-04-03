package cmput301w16t15.shareo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import mvc.Bid;
import mvc.Thing;
import mvc.exceptions.NullIDException;

/**
 * holding the adapters used in many listviews throughout
 */
public class CustomAdapters {

    /**
     * Show details:
     * Name, desc, owner, status
     */
    public static class ThingWithStatusAdapter extends ArrayAdapter<Thing> {

        private final Context context;
        private final List<Thing> things;

        public ThingWithStatusAdapter(Context context, int resource, List<Thing> objects) {
            super(context, resource, objects);
            this.context = context;
            this.things = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.detailed_thing_row, parent, false);

            Thing t = things.get(position);

            TextView name = (TextView) v.findViewById(R.id.name);
            name.setText(t.getName());

            ImageView iv = (ImageView) v.findViewById(R.id.status);
            Thing.Status s = t.getStatus();
            if (s == Thing.Status.AVAILABLE) {
                iv.setImageResource(R.drawable.green_circle);
            } else if (s == Thing.Status.BIDDED){
                iv.setImageResource(R.drawable.yellow_circle);
            } else {
                iv.setImageResource(R.drawable.red_circle);
            }

            TextView desc = (TextView) v.findViewById(R.id.desc);
            String descText = t.getDescription();
            if (descText.length() > 50) {
                descText = descText.substring(0, 50) + "...";
            }
            desc.setText(descText);

            TextView owner = (TextView) v.findViewById(R.id.owner);
            owner.setText(t.getOwnerID());

            return v;
        }
    }

    /**
     * Show basics:
     * Name, desc, owner
     */
    public static class BasicThingAdapter extends ArrayAdapter<Thing> {
        private static String TAG = "ListViewThings";
        private final Context context;
        private final List<Thing> things;
        private Thing t;

        public BasicThingAdapter(Context context, int resource, List<Thing> objects) {
            super(context, resource, objects);
            this.context = context;
            this.things = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.minimal_thing_row, parent, false);

            t = things.get(position);

            TextView name = (TextView) v.findViewById(R.id.name);
            name.setText(t.getName());

            TextView desc = (TextView) v.findViewById(R.id.desc);
            String descText = t.getDescription();
            if (descText.length() > 50) {
                descText = descText.substring(0, 50) + "...";
            }
            desc.setText(descText);

            TextView owner = (TextView) v.findViewById(R.id.owner);
            owner.setText(t.getOwnerID());
            return v;
        }
    }

    /**
     * basic for bids instead of things
     * name, desc, owner
     */
    public static class BasicBidAdapter extends ArrayAdapter<Bid> {
        private static String TAG = "ListViewBids";
        private Bid b;
        private final Context context;
        private final List<Bid> bids;

        public BasicBidAdapter(Context context, int resource, List<Bid> objects) {
            super(context, resource, objects);
            this.context = context;
            this.bids = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.minimal_thing_row, parent, false);

            b = bids.get(position);

            TextView name = (TextView) v.findViewById(R.id.name);
            name.setText(b.getThing().getName());

            TextView desc = (TextView) v.findViewById(R.id.desc);
            String descText = b.getThing().getDescription();
            if (descText.length() > 50) {
                descText = descText.substring(0, 50) + "...";
            }
            desc.setText(descText);

            TextView owner = (TextView) v.findViewById(R.id.owner);
            owner.setText(b.getThing().getOwnerID());
            return v;
        }
    }

    /**
     * Show Bid, with user and amount.
     * Have accept/decline buttons.
     */
    public static class AcceptDeclineBidAdapter extends ArrayAdapter<Bid> {
        private static String TAG = "ListViewBidsAcceptDecline";
        public final static String LOCATION_KEY = "maps";
        private final Context context;
        private final List<Bid> bids;
        private Bid b;

        public AcceptDeclineBidAdapter(Context context, int resource, List<Bid> objects) {
            super(context, resource, objects);
            this.context = context;
            this.bids = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.bid_accept_decline_row, parent, false);

            b = bids.get(position);

            try {
                new PopulateDataTask().execute(b).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            TextView username = (TextView) v.findViewById(R.id.bid_accept_decline_username);
            username.setText(b.getBidder().getName());

            TextView rate = (TextView) v.findViewById(R.id.bid_accept_decline_rate);
            int rateVal = b.getBidAmount();
            String rateString =  String.format("$%d.%02d/day", (rateVal / 100), (rateVal % 100));
            rate.setText(rateString);


            Button mAcceptButton = (Button) v.findViewById(R.id.bid_accept_button);
            Button mDeclineButton = (Button) v.findViewById(R.id.bid_decline_button);

            // make sure the buttons know which
            // bid they manipulate.
             mAcceptButton.setTag(b);
            mDeclineButton.setTag(b);

            mAcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked accept");
                    accept((Bid) v.getTag());
                    notifyDataSetChanged();
                    final Dialog dialog = new Dialog(context);
                    dialog.setTitle("Setting a meeting place");
                    dialog.setContentView(R.layout.setlocation);
                    dialog.show();
                    final EditText editText = (EditText)dialog.findViewById(R.id.locationshow);
                    Button showMaps = (Button)dialog.findViewById(R.id.ShowMaps);
                    Button setMaps = (Button)dialog.findViewById(R.id.SetMap);

                    //store the location and show it on maps
                    showMaps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String location = editText.getText().toString();
                            Intent intent = new Intent(context,MapsActivity.class);
                            intent.putExtra(LOCATION_KEY,location);
                            context.startActivity(intent);

                            dialog.cancel();
                        }
                    });
                    //only store the location
                    setMaps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            dialog.cancel();
                        }
                    });
                }
            });

            mDeclineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Clicked decline");
                    decline((Bid)v.getTag());
                    notifyDataSetChanged();
                }
            });

            return v;
        }

        private void accept(Bid b) {
            try {
                new AcceptBidTask().execute(b).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        private void decline(Bid b) {
            try {
                new DeclineBidTask().execute(b).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        private class PopulateDataTask extends AsyncTask<Bid, Void, Void> {

            @Override
            protected Void doInBackground(Bid... bid) {
                bid[0].getBidder().getName();
                return null;
            }
        }

        private class AcceptBidTask extends AsyncTask<Bid, Void, Void> {

            @Override
            protected Void doInBackground(Bid... bid) {
                bid[0].getThing().borrow(b);

                return null;
            }
        }

        private class DeclineBidTask extends AsyncTask<Bid, Void, Void> {

            @Override
            protected Void doInBackground(Bid... bid) {
                try {
                    bid[0].new Deleter().delete();
                } catch (NullIDException e) {
                    e.printStackTrace();
                }

                return null;
            }

        }
    }

}
