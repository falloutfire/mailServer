package com.manny.mailServer.Entities

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "fromUser",
    "toUser",
    "theme",
    "body"
)
data class Mail(
    @JsonProperty("fromUser") var fromUser: User,
    @JsonProperty("toUser") var toUser: List<User>,
    @JsonProperty("theme") var theme: String,
    @JsonProperty("body") var body: String
)


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
    "email",
    "password",
    "name"
)
data class User(
    @JsonProperty("email") var email: String,
    @JsonProperty("password") var password: String? = null,
    @JsonProperty("name") var name: String? = null
)