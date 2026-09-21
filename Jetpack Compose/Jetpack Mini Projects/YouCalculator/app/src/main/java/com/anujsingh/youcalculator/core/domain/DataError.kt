package com.anujsingh.youcalculator.core.domain

sealed interface DataError : Error {
    enum class Local : DataError {
        DISK_FULL,
        NOT_FOUND,
        UNKNOWN
    }
    enum class Network : DataError {
        NO_INTERNET,
        SERVER_ERROR,
        UNKNOWN
    }
}
