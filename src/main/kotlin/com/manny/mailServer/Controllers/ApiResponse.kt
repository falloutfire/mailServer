package com.manny.mailServer.Controllers

import org.springframework.http.HttpStatus

class ApiResponse(
    var status: HttpStatus,
    var message: String
) {

    constructor(
        status: HttpStatus,
        message: String, result: Any
    ) : this(status, message)
}