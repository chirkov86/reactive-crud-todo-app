package com.quicktutorialz.nio;

import com.mawashi.nio.jetty.ReactiveJ;
import com.quicktutorialz.nio.endpoints.v1.ToDoEndpoints;
import com.quicktutorialz.nio.endpoints.v2.ToDoEndpointsV2;

public class MainApplication {

    public static void main(String[] args) throws Exception {
        new ReactiveJ().port(8383)
                .endpoints(new ToDoEndpoints())
                .endpoints(new ToDoEndpointsV2())
                .start();
    }
}
