package org.example.bigdisplayproject.feature.display.util

enum class NetworkError : Error {
    WRONG_FORMAT,
    NOT_FOUND,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN;
}