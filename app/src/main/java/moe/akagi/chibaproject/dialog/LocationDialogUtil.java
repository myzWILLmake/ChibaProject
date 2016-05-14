package moe.akagi.chibaproject.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;

import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.datatype.Location;

/**
 * Created by yunze on 12/8/15.
 */
public class LocationDialogUtil {
    private LocationDialogAdapter locationDialogAdapter;
    private Activity activity;
    private String location;
    AlertDialog alertDialog;

    EditText locationEditText;

    public LocationDialogUtil(Activity activity,LocationDialogAdapter locationDialogAdapter, String location) {
        this.locationDialogAdapter = locationDialogAdapter;
        this.activity = activity;
        this.location = location;
    }

    public void initLocation(EditText editText) {
        editText.setText(location);
    }

    public AlertDialog locationDialog(final Location loc) {
        LinearLayout locationLinearLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.add_event_location_dialog, null);
        locationEditText = (EditText) locationLinearLayout.findViewById(R.id.add_event_location_edittext);
        initLocation(locationEditText);
        alertDialog = new AlertDialog.Builder(activity)
                //.setTitle("设置地点")
                .setView(locationLinearLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String location = locationEditText.getText().toString();
                        loc.setName(location);
                        locationDialogAdapter.refreshLocationInfo();
                    }
                })
                .setNegativeButton("待定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loc.setName(null);
                        locationDialogAdapter.refreshLocationInfo();
                    }
                })
                .show();
        return alertDialog;
    }
}

