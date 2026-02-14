package com.airline.portal.domain;

public class Pilot extends Person {

    public Pilot(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "com.airline.portal.domain.Pilot";
    }
}
