package com.whizz.fileManagementService.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsePacket<T> {
    private int statusCode;
    public boolean success;
    private String message;
    private T data;

    public ResponsePacket(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        success = statusCode <= 0;
    }
}
