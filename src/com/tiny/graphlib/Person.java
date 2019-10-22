package com.tiny.graphlib;

import java.util.Random;

public class Person {

    private static Random randomGen = new Random();

    private String name;
    private int age;

    public Person(String name) {
        this.name = name;
        this.age = randomGen.nextInt(100);
    }

    @Override
    public String toString() {
        return name;
    }
}
