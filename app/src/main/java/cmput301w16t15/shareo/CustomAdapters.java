package cmput301w16t15.shareo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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
            if (s == Thing.Status.AVAILABLE || s == Thing.Status.BIDDED) {
                iv.setImageResource(R.drawable.green_circle);
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

            ImageView mDeleteBtn = (ImageView) v.findViewById(R.id.delete);
            mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(v);
                }
            });
            return v;
        }

        private void buttonClicked(View v) {
            try {
                t.new Deleter().delete();
            } catch (NullIDException e) {
                Log.d(TAG, "Failed to delete a thing from listView");
            }

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

            ImageView mDeleteBtn = (ImageView) v.findViewById(R.id.delete);
            mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClicked(v);
                }
            });
            return v;
        }

        private void buttonClicked(View v) {
            try {
                b.new Deleter().delete();
            } catch (NullIDException e) {
                Log.d(TAG, "Failed to delete a bid from listView");
            }
        }
    }
}
