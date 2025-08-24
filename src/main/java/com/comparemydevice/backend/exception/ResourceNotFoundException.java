// src/main/java/com/comparemydevice/backend/exception/ResourceNotFoundException.java
package com.comparemydevice.backend.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String msg) { super(msg); }
}