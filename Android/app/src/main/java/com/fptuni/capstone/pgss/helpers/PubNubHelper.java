package com.fptuni.capstone.pgss.helpers;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TrungTNM on 2/20/2017.
 */

public class PubNubHelper {

    private static PubNubHelper helper;

    private static final String SUBSCRIBE_KEY = "sub-c-ed7a8b02-ed34-11e6-a504-02ee2ddab7fe";
    private static final String PUBLISH_KEY = "pub-c-85b2050b-5425-4964-972f-90910aa358ca";
    private static final List<String> CHANNELS_LIST = Arrays.asList("debug", "realtime map");

    private PubNubHelper() {

    }

    public static PubNubHelper getInstance() {
        if (helper == null) {
            helper = new PubNubHelper();
        }

        return helper;
    }

    public PubNub getPubNub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(PUBLISH_KEY);
        pnConfiguration.setSecure(false);

        PubNub pubNub = new PubNub(pnConfiguration);
        pubNub.subscribe()
                .channels(CHANNELS_LIST)
                .execute();

        return pubNub;
    }

}
