package com.hotel.exception;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException() {
        super("Room not found.");
    }

    public RoomNotFoundException(String message) {
        super(message);
    }

}
