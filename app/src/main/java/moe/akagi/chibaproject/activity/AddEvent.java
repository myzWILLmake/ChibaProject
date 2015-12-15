package moe.akagi.chibaproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import moe.akagi.chibaproject.dialog.DateDialogAdapter;
import moe.akagi.chibaproject.dialog.DatePickerUtil;
import moe.akagi.chibaproject.dialog.LocationDialogAdapter;
import moe.akagi.chibaproject.dialog.LocationDialogUtil;
import moe.akagi.chibaproject.dialog.TimeDialogAdapter;
import moe.akagi.chibaproject.dialog.TimePickerUtil;
import moe.akagi.chibaproject.MyApplication;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.datatype.Location;
import moe.akagi.chibaproject.datatype.Person;
import moe.akagi.chibaproject.datatype.Time;
import moe.akagi.chibaproject.datatype.User;

/**
 * Created by yunze on 12/7/15.
 */
public class AddEvent extends AppCompatActivity implements DateDialogAdapter, TimeDialogAdapter, LocationDialogAdapter {

    User user;
    Time date;
    Time time;
    Location location;

    private ListView friendsListView;

    public List<String> selectedFriends = null;
    public List<String> friends = null;

    private List<HashMap<String, Object>> itemList = null;
    private FriendAdapter adapter;

    private int backPressed = 0;

    private static class FriendAdapter extends BaseAdapter {

        private class ViewHolder {
            public CircleImageView circleImageView = null;
            public TextView textview = null;
            public AppCompatCheckBox checkbox= null;
            public int personId = -1;
        }

        public static HashMap<Integer, Boolean> isSelected;
        private Context context = null;
        private LayoutInflater inflater = null;
        private List<HashMap<String, Object>> list = null;
        private String keyString[] = null;
        private int resId = 0;
        private String nickname = null;
        private int[] idValue = null;
        private int resource = 0;

        public FriendAdapter(Context context, List<HashMap<String, Object>> list,
                             int resource, String[] from, int[] to) {
            this.context = context;
            this.list = list;
            this.resource = resource;
            keyString = new String[from.length];
            idValue = new int[to.length];
            System.arraycopy(from, 0, keyString, 0, from.length);
            System.arraycopy(to, 0, idValue, 0, to.length);
            inflater = LayoutInflater.from(context);
            init();
        }

        private void init() {
            isSelected = new HashMap<Integer, Boolean>();
            for (int i=0; i<list.size(); i++) {
                isSelected.put(i, false);
            }
        }

        public List<String> selectedIds() {
            List<String> result;
            result = new ArrayList<>();
            for (int i=0; i<isSelected.size(); i++) {
                if (isSelected.get(i)) {
                    int pid = (int)list.get(i).get(keyString[3]);
                    String pidStr = Integer.toString(pid);
                    result.add(pidStr);
                }
            }
            return result;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.circleImageView = (CircleImageView) convertView.findViewById(idValue[0]);
                holder.textview = (TextView) convertView.findViewById(idValue[1]);
                holder.checkbox = (AppCompatCheckBox) convertView.findViewById(idValue[2]);
                holder.checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatCheckBox checkbox = holder.checkbox;
                        if (checkbox.isChecked()) {
                            isSelected.put(position, true);
                        } else {
                            isSelected.put(position, false);
                        }
                        Log.d("OK", "After state:" + checkbox.isChecked() + " position: " + position + " isSelected? " + isSelected.get(position));
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            HashMap<String, Object> map = list.get(position);
            if (map != null) {
                String phone = (String) map.get(keyString[0]);
                String resName = "profile_image_" + phone;
                resId = context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
                nickname = (String) map.get(keyString[1]);
                int pid = (int) map.get(keyString[3]);

                holder.circleImageView.setImageResource(resId);
                holder.textview.setText(nickname);
                holder.personId = pid;
            }
            holder.checkbox.setChecked(isSelected.get(position));
            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);

        initLayout();

        user = MyApplication.user;
        Date dateTmp = new Date(0);
        date = new Time(dateTmp);
        time = new Time(dateTmp);
        location = new Location();

        date.setYear(-1);
        time.setHour(-1);
        location.setInfo(null);

        selectedFriends = new ArrayList<String>();
        friendsListView = (ListView) findViewById(R.id.add_event_friend_list);

        initFriendItems();

        Button timeButton = (Button) findViewById(R.id.add_event_edit_time);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time.getHour() == -1) {
                    TimePickerUtil timePickerUtil = new TimePickerUtil(AddEvent.this, AddEvent.this, -1 , 0);
                    timePickerUtil.timePickDialog(time);
                } else {
                    TimePickerUtil timePickerUtil = new TimePickerUtil(AddEvent.this, AddEvent.this, time.getHour(), time.getMinute());
                    timePickerUtil.timePickDialog(time);
                }
            }
        });

        Button dateButton = (Button) findViewById(R.id.add_event_edit_date);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.getYear() == -1) {
                    DatePickerUtil datePickerUtil = new DatePickerUtil(AddEvent.this, AddEvent.this, -1, 0, 0);
                    datePickerUtil.datePickDialog(date);
                } else {
                    DatePickerUtil datePickerUtil = new DatePickerUtil(AddEvent.this, AddEvent.this,date.getYear(), date.getMonth(), date.getDay());
                    datePickerUtil.datePickDialog(date);
                }
            }
        });

        Button locationButton = (Button) findViewById(R.id.add_event_edit_location);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.getInfo() == null) {
                    LocationDialogUtil locationDialogUtil = new LocationDialogUtil(AddEvent.this, AddEvent.this, "");
                    locationDialogUtil.locationDialog(location);
                } else {
                    LocationDialogUtil locationDialogUtil = new LocationDialogUtil(AddEvent.this, AddEvent.this, location.getInfo());
                    locationDialogUtil.locationDialog(location);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (backPressed) {
            case 0:
                Toast.makeText(AddEvent.this, "再次按下返回键取消建立活动", Toast.LENGTH_SHORT).show();
                backPressed++;
                break;
            case 1:
                ActivityCollector.removeActivity(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_event_toolbar_menu, menu);
        menu.findItem(R.id.add_event_toolbar_submit).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_check)
                .actionBarSize()
        );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_event_toolbar_submit:
                if (createEvent()) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    ActivityCollector.removeActivity(this);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {
        setContentView(R.layout.add_event_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.add_event_activity_toolbar);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String addEventStr = getResources().getString(R.string.add_event);
            getSupportActionBar().setTitle(addEventStr);
            myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    ActivityCollector.removeActivity(AddEvent.this);
                }
            });
        }
    }

    private void initFriendItems() {
        itemList = new ArrayList<HashMap<String, Object>>();
        friends = API.getFriendsByPersonId(user.getId());
        for (String friend : friends) {
            Person person = API.getPersonByPersonId(Integer.parseInt(friend));
            int pid = person.getId();
            String phone = person.getPhone();
            String nickname = person.getNickname();

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("item_image", phone);
            map.put("item_nickname", nickname);
            map.put("item_checkbox", false);
            map.put("item_id", pid);

            itemList.add(map);
        }
        adapter = new FriendAdapter(this, itemList, R.layout.add_event_friend_listview_item,
                new String[] {"item_image", "item_nickname", "item_checkbox", "item_id"},
                new int[] {R.id.add_event_friend_list_item_image, R.id.add_event_friend_list_item_nickname, R.id.add_event_friend_list_item_checkbox, 0});
        friendsListView.setAdapter(adapter);
    }

    @Override
    public void refreshDateInfo() {
        TextView dateTextView = (TextView) findViewById(R.id.add_event_date);
        String dateStr;
        if (date.getYear() == -1) {
            dateStr = "待定";
        } else {
            dateStr = date.formatDate();
        }
        dateTextView.setText(dateStr);
    }

    @Override
    public void refreshTimeInfo() {
        TextView timeTextView = (TextView) findViewById(R.id.add_event_time);
        String timeStr;
        if (time.getHour() == -1) {
            timeStr = "待定";
        } else {
            timeStr = time.formatTime();
        }
        timeTextView.setText(timeStr);
    }

    @Override
    public void refreshLocationInfo() {
        TextView locationTextView = (TextView) findViewById(R.id.add_event_location);
        String locationStr;
        if (location.getInfo() == null) {
            locationStr = "待定";
        } else {
            locationStr = location.getInfo();
        }
        locationTextView.setText(locationStr);
    }

    private boolean createEvent() {
        EditText titleEditText = (EditText) findViewById(R.id.add_event_title);
        String title = titleEditText.getText().toString();
        if ("".equals(title)) {
            Toast.makeText(this, "请添加活动题目哦~", Toast.LENGTH_SHORT).show();
            return false;
        }
        selectedFriends = adapter.selectedIds();
        int managerId = user.getId();
        List<String> partInPerons = selectedFriends;
        partInPerons.add(Integer.toString(managerId));

        int timeStat = 1;
        Time dateAndTime = new Time(new Date(0));
        if (date.getYear() != -1) {
            dateAndTime.setYear(date.getYear());
            dateAndTime.setMonth(date.getMonth());
            dateAndTime.setDay(date.getDay());
        }
        if (time.getHour() != -1) {
            dateAndTime.setHour(time.getHour());
            dateAndTime.setMinute(time.getMinute());
        } else {
            timeStat = 0;
        }
        long timeLong = dateAndTime.formatLong();
        int eventId = API.insertNewEvent(managerId, title, timeLong, timeStat, location.getInfo());
        API.insertPartInPersons(eventId, selectedFriends);
        API.insertLaunchEvent(managerId, eventId);
        return true;
    }
}
