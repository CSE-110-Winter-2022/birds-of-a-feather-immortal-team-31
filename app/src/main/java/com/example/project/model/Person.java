package com.example.project.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Person {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "person")
    public String personName;

    public Person(String personName)
    {
        this.personName = personName;
    }

    public String getPersonName()
    {
        return personName;
    }
}
