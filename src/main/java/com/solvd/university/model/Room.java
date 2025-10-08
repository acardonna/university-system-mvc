package com.solvd.university.model;

import java.util.Objects;

public abstract class Room extends Facility {

    protected int capacity;
    protected String roomType;

    protected Room() {}

    protected Room(String name, int capacity, String roomType) {
        super(name);
        this.capacity = capacity;
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return capacity == room.capacity && Objects.equals(name, room.name) && Objects.equals(roomType, room.roomType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, capacity, roomType);
    }
}
