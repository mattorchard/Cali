package com.example.uottawa.cali;

public enum AssignmentTypes {
    UNSET(R.string.unset_assignment_type, R.drawable.ic_blank),
    LAB_REPORT(R.string.lab_report_assignment_type, R.drawable.ic_at_lab_report),
    PAPER(R.string.paper_assignment_type, R.drawable.ic_at_paper),
    GROUP_WORK(R.string.group_work_assignment_type, R.drawable.ic_at_group_work),
    PROBLEM_SET(R.string.problem_set_assignment_type, R.drawable.ic_at_problem_set),
    project(R.string.project_assignment_type, R.drawable.ic_at_project),
    STUDY(R.string.study_assignment_type, R.drawable.ic_at_study);

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
