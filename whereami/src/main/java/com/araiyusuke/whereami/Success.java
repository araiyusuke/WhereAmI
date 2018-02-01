package com.araiyusuke.whereami;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by araiyuusuke on 2018/01/30.
 */

public class Success extends ResponseResult {

    public ArrayList<LocationAddress> addresses;
    public Location location;

    @Override
    public Response getType() {
        return Response.success;
    }

}
