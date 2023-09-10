package com.hotel.exception;

public class RoomIsNotAvailableOnThatDateException extends RuntimeException {

    public RoomIsNotAvailableOnThatDateException() {
        super("Room is already booked");
    }

    public RoomIsNotAvailableOnThatDateException(String message) {
        super(message);
    }

}