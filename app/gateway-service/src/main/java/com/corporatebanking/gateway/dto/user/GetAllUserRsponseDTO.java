package com.corporatebanking.gateway.dto.user;

public record GetAllUserRsponseDTO(
        List<UserCreateResponseDTO> users
) {}