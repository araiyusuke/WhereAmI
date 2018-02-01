package com.araiyusuke.whereami;

/**
 * Created by araiyuusuke on 2018/01/30.
 */

public class Unauthorized extends ResponseResult {

    @Override
    public Response getType() {
        return Response.unauthorized;
    }
}

