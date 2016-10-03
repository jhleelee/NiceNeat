//package jacklee_entertainment.niceneat.utils;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Filter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import jacklee_entertainment.niceneat.R;
//
//public class ContactlistAdapter extends ArrayAdapter<ContactListEntry> {
//    private Context context;
//    private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
//    private List<ContactListEntry> filtered;
//    private List<ContactListEntry> values;
//
//    public ContactlistAdapter(Context context, int textViewResourceId, List<ContactListEntry> objects) {
//        super(context, textViewResourceId, objects);
//        this.context = context;
//        this.values = objects;
//        filtered = objects;
//    }
//
//    @Override
//    public int getCount() {
//        return filtered.size();
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) getContext()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View rowView = inflater.inflate(R.layout.item_listview_chatfragment_contactlist_friend, parent, false);
//        ImageView profilePicImg = (ImageView) rowView.findViewById(R.id.img_contactlist);
//        TextView displayNameTv = (TextView) rowView.findViewById(R.id.tv_contactlist_displayname);
//
//        displayNameTv.setText(filtered.get(position).getDisplayName());
//        if (filtered.get(position).getEntryType() == ContactListEntry.EntryType.CONTACT_ENTRY) {
//            // set transparent color for the profile image
//            profilePicImg.setVisibility(View.GONE);
//        }
//        else {
//            int tintColor = R.color.cyan_middle;
//
//            if (filtered.get(position).getPhone() != null) {
//                String ntnlcodeAndPhonenumber = filtered.get(position).getNationalCode() +
//                        "000000" + filtered.get(position).getPhone();
//
//                if (ntnlcodeAndPhonenumber.endsWith("6")) {  // 7n + 6
//                    tintColor = R.color.purple_middle;
//                }
//                else if (ntnlcodeAndPhonenumber.endsWith("5")) {  // 7n + 5
//                    tintColor = R.color.orange_middle;
//                }
//                else if (ntnlcodeAndPhonenumber.endsWith("4")) {  // 7n + 4
//                    tintColor = R.color.yellow_middle;
//                }
//                else if (ntnlcodeAndPhonenumber.endsWith("3")) {  // 7n + 3
//                    tintColor = R.color.blue_middle;
//                }
//                else if (ntnlcodeAndPhonenumber.endsWith("2")) {  // 7n + 2
//                    tintColor = R.color.green_middle;
//                }
//                else if (ntnlcodeAndPhonenumber.endsWith("1")) {  // 7n + 1
//                    tintColor = R.color.red_middle;
//                }
//            }
//
//            Bitmap res = ImageUtils.tintWithColor(R.drawable.ic_person_white, tintColor, getContext());
//
//            profilePicImg.setImageBitmap(res);
//            //profilePicImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_person_white));
//
//            if (filtered.get(position).getLastRequestAt() != null) {
//                long currentTime = System.currentTimeMillis();
//                long userLastRequestAtTime = filtered.get(position).getLastRequestAt().getTime();
//
//                // if user didn't do anything last 5 minutes (5*60*1000 milliseconds)
//                if((currentTime - userLastRequestAtTime) > 5*60*1000) {
//                    lastConnectedTv.setText(context.getString(R.string.last_online) + " "
//                            + format.format(filtered.get(position).getLastRequestAt()));
//                }
//                else {
//                    lastConnectedTv.setText(context.getString(R.string.online));
//                }
//            }
//        }
//
//        return rowView;
//    }
//
//
//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                filtered = (List<ContactListEntry>) results.values;
//                notifyDataSetChanged();
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                List<ContactListEntry> filteredResults = getFilteredResults(constraint);
//
//                FilterResults results = new FilterResults();
//                results.values = filteredResults;
//
//                return results;
//            }
//        };
//    }
//
//    private List<ContactListEntry> getFilteredResults(CharSequence constraint) {
//        ArrayList<ContactListEntry> filtered = new ArrayList<ContactListEntry>();
//
//        for (ContactListEntry entry: values) {
//            if ((entry.getDisplayName() != null && entry.getDisplayName().toLowerCase()
//                    .contains(constraint.toString().toLowerCase()))
//                    || (entry.getPhone() != null && entry.getPhone().toLowerCase()
//                    .contains(constraint.toString().toLowerCase()))) {
//                filtered.add(entry);
//            }
//        }
//
//        return filtered;
//    }
//
//    public void setFiltered(List<ContactListEntry> filtered) {
//        this.filtered = filtered;
//    }
//
//    public List<ContactListEntry> getFiltered() {
//        return this.filtered;
//    }
//}
