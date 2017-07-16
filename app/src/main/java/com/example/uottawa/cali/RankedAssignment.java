package com.example.uottawa.cali;

import android.support.annotation.NonNull;

public class RankedAssignment implements Comparable<RankedAssignment>{

    private Assignment assignment;
    private double rank;

    public RankedAssignment(Assignment assignment, double rank) {
        this.assignment = assignment;
        this.rank = rank;
    }

    @Override
    public int compareTo(@NonNull RankedAssignment other) {
        return Double.valueOf(rank).compareTo(other.getRank());
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public double getRank() {
        return rank;
    }
}
