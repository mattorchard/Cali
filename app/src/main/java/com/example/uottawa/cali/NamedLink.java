package com.example.uottawa.cali;

import java.io.Serializable;

/**
 * Created by Ash on 7/17/2017.
 */

public class NamedLink implements Serializable {

    private String name;
    private String url;

    public NamedLink(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public boolean equals(NamedLink other) {
        return
                this.name.equals(other.getName()) &&
                this.url.equals(other.getUrl());
    }

}
