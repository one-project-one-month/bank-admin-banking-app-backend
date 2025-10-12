package com.corporatebanking.gateway.dto.nickname;

public record NicknameResponseDto(
	    Long id,
	    Long fromAccount,
	    Long toAccount,
		String nickname,
	    String createdAt,
	    String updatedAt,
	    Long createdBy,
	    Long updatedBy
) {}
