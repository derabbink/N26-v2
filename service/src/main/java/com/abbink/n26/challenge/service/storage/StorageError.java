package com.abbink.n26.challenge.service.storage;

public class StorageError extends RuntimeException {

    public StorageError(String message) {
        super(message);
    }
}
