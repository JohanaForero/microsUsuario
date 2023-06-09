package com.reto.usuario.application.handler.implementation;

import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.application.handler.IUserService;
import com.reto.usuario.application.mapper.request.IUserRequestMapper;
import com.reto.usuario.application.mapper.response.IUserResponseMapper;
import com.reto.usuario.domain.api.IUserUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements IUserService {

    private final IUserUseCasePort userUseCasePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;

    @Override
    public void registerUserWithOwnerRole(UserRequestDto userRequestDto) {
        userUseCasePort.registerUserWithOwnerRole(userRequestMapper.toUserModel(userRequestDto));
    }

    @Override
    public void registerUserWithEmployeeRole(UserRequestToCreateEmployeeDto userRequestToCreateEmployeeDto) {
        userUseCasePort.registerUserWithEmployeeRole(userRequestMapper.toUserModelEmployee(userRequestToCreateEmployeeDto));
    }

    @Override
    public UserResponseDto getUserById(Long idUser) {
        return userResponseMapper.toUserResponseDto(userUseCasePort.getUserById(idUser));
    }
}
