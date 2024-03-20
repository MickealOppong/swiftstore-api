package com.swiftstore.model.User;

import lombok.Builder;

@Builder
public record JwtResponse(String token,String accessToken)  {
}
