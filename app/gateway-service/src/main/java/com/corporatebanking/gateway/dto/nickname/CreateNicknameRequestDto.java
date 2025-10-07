package com.corporatebanking.gateway.dto.nickname;

public record CreateNicknameRequestDto(
		Long fromAccount,Long toAccount,String nickname,Long createdBy
) {}
