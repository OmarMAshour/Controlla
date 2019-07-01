package Services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.controlla.controlla.MainActivity;

import java.util.ArrayList;

public class GoogleMaps {
    Context mContext;

    public GoogleMaps(Context mContext) {
        this.mContext = mContext;

    }

     public String GetCurrentAddress() {
        ArrayList<String> arr = new ArrayList<String>();
        GPSTracker gps = new GPSTracker(mContext);
        arr = gps.getLocation(mContext);
        String address = gps.getAddressLine(mContext, Double.valueOf(arr.get(0)), Double.valueOf(arr.get(1)));
        return address;
    }


}
