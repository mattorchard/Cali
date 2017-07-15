package com.example.uottawa.cali;

public enum AssignmentTypes {
    LAB_REPORT(R.string.lab_report_assignment_type, R.drawable.ic_attach),
    PAPER(R.string.paper_assignment_type, R.drawable.ic_attach),
    GROUP_WORK(R.string.group_work_assignment_type, R.drawable.ic_attach),
    PROBLEM_SET(R.string.problem_set_assignment_type, R.drawable.ic_attach),
    DELIVERABLE(R.string.deliverable_assignment_type, R.drawable.ic_attach);

    int nameID;
    int imageID;

    AssignmentTypes(int nameID, int imageID) {
        this.nameID = nameID;
        this.imageID = imageID;
    }

    public int getNameID() {
        return nameID;
    }

    public int getImageID() {
        return imageID;
    }
}
