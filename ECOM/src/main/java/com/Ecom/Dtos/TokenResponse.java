package com.Ecom.Dtos;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private  String accessToken;
    private  String refreshToken;
    private  UserDto user;

}