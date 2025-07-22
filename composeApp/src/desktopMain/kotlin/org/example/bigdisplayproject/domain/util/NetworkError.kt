package org.example.bigdisplayproject.domain.util

enum class NetworkError : Error {
    WRONG_FORMAT,
    NOT_FOUND,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN;
}