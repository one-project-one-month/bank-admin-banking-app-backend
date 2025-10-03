package com.corporatebanking.gateway.controller.users;

@RestController
@RequestMapping("/api/v1/user")
public class CreateFaqController {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @PostMapping
    public ResponseEntity<UserCreateResponeDTO> createUser(@RequestBody UserCreateRequestDTO userCreateRequestDTO){

        UserCreateRequest userCreateRequest = UserCreateRequest.newBuilder()
                .setFullName(userCreateRequestDTO.fullName())
                .setDateOfBirth(userCreateRequestDTO.dateOfBirth())
                .setGenderId(userCreateRequestDTO.genderId())
                .setEmail(userCreateRequestDTO.email())
                .build();

        UserCreateResponse userCreateResponse = userServiceBlockingStub.createUser(userCreateRequest);
        return ResponseEntity.ok(convertToDTO(createFaqResponse));
    }

    private UserCreateResponeDTO convertToDTO(UserCreateResponse userResponse){

        return new UserCreateResponeDTO(
                userResponse.getId(),
                userResponse.getFullName(),
                userResponse.getDateOfBirth(),
                userResponse.getGenderId(),
                userResponse.getEmail(),
                userResponse.getCreatedAt(),
                userResponse.getUpdatedAt(),
                userResponse.getCreatedBy(),
                userResponse.getUpdatedBy()
        );
    }

    @GetMapping
    public ResponseEntity<GetAllUserRsponseDTO> getAllUsers(){

        GetAllUsersRequest getAllUsersRequest = GetAllUsersRequest.newBuilder().build();
        GetAllUserResponse getAllUserResponse = userServiceBlockingStub.getAllUsers(request);

        List<UserCreateResponseDTO> users = grpcResponse.getUsersList()
                .stream()
                .map(userResponse -> new UserCreateResponseDTO(
                        userResponse.getId(),
                        userResponse.getFullName(),
                        userResponse.getDateOfBirth(),
                        userResponse.getGenderId(),
                        userResponse.getEmail(),
                        userResponse.getCreatedAt(),
                        userResponse.getUpdatedAt(),
                        userResponse.getCreatedBy(),
                        userResponse.getUpdatedBy()
                ))
                .toList();

        GetAllUserRsponseDTO responseDto = new GetAllUserRsponseDTO(users);

        return ResponseEntity.ok(responseDto);
    }
}