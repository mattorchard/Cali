package com.example.uottawa.cali;

import java.io.Serializable;

/**
 * Created by Ash on 7/17/2017.
 */

public class NamedUri implements Serializable {

    private String name;
    private String uri;

    public NamedUri(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

}
