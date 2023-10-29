package com.example.ssodemo.model.dto;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityUserMixin {
    @JsonCreator
    public SecurityUserMixin(
            @JsonProperty("id") Long id,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("email") String email,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("phoneNumber") String phoneNumber
    ) {
    }
}
