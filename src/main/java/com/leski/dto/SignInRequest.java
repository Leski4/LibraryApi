package com.leski.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию")
public class SignInRequest {

    private String username;

    private String password;
}