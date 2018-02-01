package com.araiyusuke.whereami;

import android.location.Address;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by araiyuusuke on 2018/01/30.
 */

public class LocationAddress implements Parcelable {
    private Location location;
    private Address address;

    public LocationAddress(Address address) {
        this.address = address;
        location = new Location(LocationAddress.class.getCanonicalName());
        location.setLatitude(address.getLatitude());
        location.setLongitude(address.getLongitude());
    }

    public LocationAddress(Parcel in) {
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.address = in.readParcelable(Address.class.getClassLoader());
    }

    public Location getLocation() {
        return location;
    }

    public Address getAddress() {
        return address;
    }

    public String getFormattedAddress() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            builder.append(address.getAddressLine(i));
        }
        return builder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.address, flags);
    }

    public static final Creator CREATOR = new Creator() {
        public LocationAddress createFromParcel(Parcel in) {
            return new LocationAddress(in);
        }

        public LocationAddress[] newArray(int size) {
            return new LocationAddress[size];
        }
    };
}