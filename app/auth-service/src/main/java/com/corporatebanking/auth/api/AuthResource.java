package com.corporatebanking.auth.api;

import com.corporatebanking.auth.dto.LoginRequest;
import com.corporatebanking.auth.dto.TokenResponse;
import com.corporatebanking.auth.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/auth")
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        String token = authService.authenticate(loginRequest.username(), loginRequest.password());
        if (token != null) {
            return Response.ok(new TokenResponse(token)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of(
                        "error", "Unauthorized",
                        "message", "Incorrect username or password"
                ))
                .build();
    }
}
