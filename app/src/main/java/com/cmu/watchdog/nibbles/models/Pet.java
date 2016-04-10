package com.cmu.watchdog.nibbles.models;

/**
 * Created by alicesypark on 4/9/16.
 */
public class Pet {
    private String name;
    private String gender;
    private String type;
    private String breed;
    private int age;
    private int id;

    public Pet(String name, String gender, String type, String breed, int age, int id) {
        this.name = name;
        this.gender = gender;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
