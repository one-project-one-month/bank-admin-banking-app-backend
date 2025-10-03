package com.corporatebanking.user.features.userService.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.corporatebanking.user.features.userService.models.User;
import com.corporatebanking.user.features.userService.repository.jdbc.UserJdbcRepository;
import com.corporatebanking.userService.grpc.GetAllUserResponse;
import com.corporatebanking.userService.grpc.GetAllUsersRequest;
import com.corporatebanking.userService.grpc.UserCreateRequest;
import com.corporatebanking.userService.grpc.UserCreateResponse;
import com.corporatebanking.userService.grpc.UserServiceGrpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserJdbcRepository userJdbcRepository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public UserServiceImpl(UserJdbcRepository userJdbcRepository) {
        this.userJdbcRepository = userJdbcRepository;
    }

    @Override
    public void createUser(UserCreateRequest request, StreamObserver<UserCreateResponse> responseObserver) {

        LocalDate localDate = LocalDate.parse(request.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Date dateOfBirth = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        User user = new User(
                    null,
                    request.getFullName(),
                    dateOfBirth,
                    request.getGenderId(),
                    request.getEmail(),
                    null,
                    null,
                    1001L, // dummy user id
                    null
            );

        User result = userJdbcRepository.save(user);

        UserCreateResponse.Builder responseBuilder = UserCreateResponse.newBuilder()
                .setId(result.id())
                .setFullName(result.fullName())
                .setEmail(result.email())
                .setGenderId(result.genderId());

        if (result.dateOfBirth() != null) {
            responseBuilder.setDateOfBirth(dateFormat.format(result.dateOfBirth()));
        }

        if (result.createdAt() != null) {
            responseBuilder.setCreatedAt(result.createdAt().toString());
        }

        if (result.updatedAt() != null) {
            responseBuilder.setUpdatedAt(result.updatedAt().toString());
        }

        if (result.createdBy() != null) {
            responseBuilder.setCreatedBy(result.createdBy());
        }

        if (result.updatedBy() != null) {
            responseBuilder.setUpdatedBy(result.updatedBy());
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();

    }

    @Override
    public void getAllUsers(GetAllUsersRequest request, StreamObserver<GetAllUserResponse> responseObserver) {
        try {
            List<User> users = userJdbcRepository.getAllUsers();

            GetAllUserResponse.Builder responseBuilder = GetAllUserResponse.newBuilder();

            for (User user : users) {
                UserCreateResponse.Builder userBuilder = UserCreateResponse.newBuilder()
                        .setId(user.id() != null ? user.id() : 0)
                        .setFullName(user.fullName() != null ? user.fullName() : "")
                        .setEmail(user.email() != null ? user.email() : "")
                        .setGenderId(user.genderId());

                if (user.dateOfBirth() != null) {
                    userBuilder.setDateOfBirth(dateFormat.format(user.dateOfBirth()));
                }
                if (user.createdAt() != null) {
                    userBuilder.setCreatedAt(user.createdAt().toString());
                }
                if (user.updatedAt() != null) {
                    userBuilder.setUpdatedAt(user.updatedAt().toString());
                }
                if (user.createdBy() != null) {
                    userBuilder.setCreatedBy(user.createdBy());
                }
                if (user.updatedBy() != null) {
                    userBuilder.setUpdatedBy(user.updatedBy());
                }

                responseBuilder.addUsers(userBuilder.build());
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Error fetching users: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}
