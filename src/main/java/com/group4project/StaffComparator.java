package com.group4project;

import java.util.Comparator;

public class StaffComparator implements Comparator<Staff> {
    @Override
    public int compare(Staff o1, Staff o2) {
        return o1.getId().compareTo(o2.getId());
    }
}

class CompareStaffStatus implements Comparator<Staff> {

    @Override
    public int compare(Staff o1, Staff o2) {
        return o1.getStatus().compareTo(o2.getStatus());
    }
}
