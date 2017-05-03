package it341.happening;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class YelpLocationDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yelp_location_details);

        final YelpLocation location = (YelpLocation)getIntent().getParcelableExtra("location");
        TextView title = (TextView)findViewById(R.id.title);
        TextView rating = (TextView)findViewById(R.id.rating);
        TextView phone = (TextView)findViewById(R.id.phone);
        TextView open = (TextView)findViewById(R.id.openHours);
        TextView numFriends = (TextView)findViewById(R.id.numFriends);
        Button yelpBtn = (Button)findViewById(R.id.linkBtn);
        final Button addFav = (Button)findViewById(R.id.btn_AddToFav);
        final Button checkIn = (Button)findViewById(R.id.btn_checkIn);
        Button viewOnMap = (Button)findViewById(R.id.btn_viewOnMap);
        Button viewFriends = (Button)findViewById(R.id.btn_viewFriends);

        title.setText(location.name);
        rating.setText("Rating: " + String.valueOf(location.rating));
        phone.setText("Phone: " + location.phone);
        numFriends.setText("Friends: " + String.valueOf(0));
        open.setText(location.isClosed == 1 ? "closed" : "open");

        if(BookmarkManager.getInstance().contains(location)) {
            addFav.setText("Remove Favorite");
        }

        if(AppInfo.getInstance().checkedIn && AppInfo.getInstance().checkedInLocation.equals(location)) {
            checkIn.setText("Check Out");
        }

        yelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(location.url));
                startActivity(browserIntent);
            }
        });

        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppInfo.getInstance().authenticatedUser) {
                    if(addFav.getText().equals("Remove Favorite")) {
                        BookmarkManager.getInstance().removeBookmark(location);
                        addFav.setText("Add Favorite");
                    } else {
                        BookmarkManager.getInstance().addBookmark(location);
                        addFav.setText("Remove Favorite");
                    }
                }
            }
        });

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppInfo.getInstance().authenticatedUser) {
                    AppInfo.getInstance().checkedInLocation = location;
                    if(AppInfo.getInstance().checkedIn && AppInfo.getInstance().checkedInLocation.equals(location)) {
                        checkIn.setText("Check In");
                        AppInfo.getInstance().checkedInLocation = null;
                        AppInfo.getInstance().checkedIn = false;
                    } else {
                        checkIn.setText("Check Out");
                        AppInfo.getInstance().checkedInLocation = location;
                        AppInfo.getInstance().checkedIn = true;
                    }
                }
            }
        });

        viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YelpLocationDetailsActivity.this, MapActivity.class);
                ArrayList<YelpLocation> locations = new ArrayList<YelpLocation>();
                locations.add(location);
                intent.putParcelableArrayListExtra("locations",locations);
                startActivity(intent);
            }
        });

        viewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
