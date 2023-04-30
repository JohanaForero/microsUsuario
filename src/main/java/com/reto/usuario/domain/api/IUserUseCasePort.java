package com.reto.usuario.domain.api;

import com.reto.usuario.domain.dto.AuthCredentials;
import com.reto.usuario.domain.model.UserModel;

public interface IUserUseCasePort {

    void registerUserWithOwnerRole(UserModel userModel);

    String signInUseCase(AuthCredentials authCredentials);

    UserModel findUsuarioByEmail(String email);
}