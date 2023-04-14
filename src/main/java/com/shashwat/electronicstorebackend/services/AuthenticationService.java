package com.shashwat.electronicstorebackend.services;

import com.shashwat.electronicstorebackend.dtos.AuthenticationRequestDto;
import com.shashwat.electronicstorebackend.dtos.AuthenticationResponseDto;

public interface AuthenticationService {

	AuthenticationResponseDto authenticate(AuthenticationRequestDto authenticationRequestDto);
}
