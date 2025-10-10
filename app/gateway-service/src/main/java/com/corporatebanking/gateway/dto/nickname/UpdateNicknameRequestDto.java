package com.corporatebanking.gateway.dto.nickname;

public record UpdateNicknameRequestDto(
		Long toAccount,String nickname,Long updatedBy
) {}
