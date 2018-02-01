package com.araiyusuke.whereami;

import static com.araiyusuke.whereami.Response.failed;

/**
 * Created by araiyuusuke on 2018/01/30.
 */

public class Failed extends ResponseResult {

    public String message;

    public Error type;

    @Override
    public Response getType() {
        return failed;
    }

}
