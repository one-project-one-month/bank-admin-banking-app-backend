package com.corporatebanking.gateway.controller.nickname;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corporatebanking.gateway.dto.nickname.CreateNicknameRequestDto;
import com.corporatebanking.gateway.dto.nickname.NicknameResponseDto;
import com.corporatebanking.gateway.dto.nickname.UpdateNicknameRequestDto;
import com.corporatebanking.nickname.grpc.CreateNicknameRequest;
import com.corporatebanking.nickname.grpc.DeleteNicknameRequest;
import com.corporatebanking.nickname.grpc.DeleteNicknameResponse;
import com.corporatebanking.nickname.grpc.GetAllNicknamesRequest;
import com.corporatebanking.nickname.grpc.GetNicknameRequest;
import com.corporatebanking.nickname.grpc.NicknameListResponse;
import com.corporatebanking.nickname.grpc.NicknameResponse;
import com.corporatebanking.nickname.grpc.NicknameServiceGrpc;
import com.corporatebanking.nickname.grpc.UpdateNicknameRequest;

import net.devh.boot.grpc.client.inject.GrpcClient;

@RestController
@RequestMapping("/api/v1/nicknames")
public class NicknameController {

    @GrpcClient("nickname-service")
    private NicknameServiceGrpc.NicknameServiceBlockingStub nicknameServiceStub;
    
    @PostMapping
    public ResponseEntity<NicknameResponseDto> createNickname(@RequestBody CreateNicknameRequestDto requestDto) {
        CreateNicknameRequest request = CreateNicknameRequest.newBuilder()
                .setFromAccount(requestDto.fromAccount())
                .setToAccount(requestDto.toAccount())
                .setNickname(requestDto.nickname())
                .setCreatedBy(requestDto.createdBy())
                .build();
        NicknameResponse response = nicknameServiceStub.createNickname(request);
        return ResponseEntity.ok(toDto(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NicknameResponseDto> getNickname(@PathVariable Long id) {
        GetNicknameRequest request = GetNicknameRequest.newBuilder().setToAccount(id).build();
        NicknameResponse response = nicknameServiceStub.getNickname(request);
        return ResponseEntity.ok(toDto(response));
    }

    @GetMapping
    public ResponseEntity<List<NicknameResponseDto>> getAllNicknames() {
        GetAllNicknamesRequest request = GetAllNicknamesRequest.newBuilder().build();
        NicknameListResponse response = nicknameServiceStub.getAllNicknames(request);
        List<NicknameResponseDto> dtos = response.getNicknamesList().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{toAccountId}")
    public ResponseEntity<NicknameResponseDto> updateNickname(@PathVariable Long toAccountId, @RequestBody UpdateNicknameRequestDto requestDto) {
        UpdateNicknameRequest request = UpdateNicknameRequest.newBuilder()
                .setToAccount(requestDto.toAccount())
                .setNickname(requestDto.nickname())
                .setUpdatedBy(requestDto.updatedBy())
                .build();
        NicknameResponse response = nicknameServiceStub.updateNickname(request);
        return ResponseEntity.ok(toDto(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNickname(@PathVariable Long id) {
        DeleteNicknameRequest request = DeleteNicknameRequest.newBuilder().setToAccount(id).build();
        DeleteNicknameResponse response = nicknameServiceStub.deleteNickname(request);
        return ResponseEntity.ok(response.getMessage());
    }

    private NicknameResponseDto toDto(NicknameResponse response) {
        return new NicknameResponseDto(
                response.getId(),
                response.getFromAccount(),
                response.getToAccount(),
                response.getNickname(),
                response.getCreatedAt(),
                response.getUpdatedAt(),
                response.getCreatedBy(),
                response.getUpdatedBy()
        );
    }
}
