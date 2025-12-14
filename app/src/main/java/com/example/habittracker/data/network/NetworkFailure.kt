package com.example.habittracker.data.network

import com.example.habittracker.data.remote.models.NetworkError

sealed class NetworkFailure(val networkError: NetworkError) {

    class Connection(networkError: NetworkError) : NetworkFailure(networkError)

    class Unexpected(networkError: NetworkError) : NetworkFailure(networkError)

    class Timeout(networkError: NetworkError) : NetworkFailure(networkError)

    class Client(networkError: NetworkError) : NetworkFailure(networkError)

    class Server(networkError: NetworkError) : NetworkFailure(networkError)

    class Connectivity(networkError: NetworkError) : NetworkFailure(networkError)

    class Unauthorized(networkError: NetworkError) : NetworkFailure(networkError)
}