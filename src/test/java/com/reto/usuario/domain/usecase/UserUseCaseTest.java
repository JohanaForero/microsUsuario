package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.exceptions.EmailExistsException;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.exceptions.EmailNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserUseCaseTest {

    @InjectMocks
    UserUseCase userUseCase;

    @Mock
    IUserPersistenceDomainPort userPersistenceDomainPort;

    @Mock
    IRolPersistenceDomainPort rolPersistenceDomainPort;


    @Test
    void registerUserWithOwnerRole() {
        when(rolPersistenceDomainPort.findByName("PROPIETARIO")).thenReturn(FactoryUserModelTest.rolModel());
        when(userPersistenceDomainPort.saveUser(any())).thenReturn(FactoryUserModelTest.userModelWithoutRole());
        userUseCase.registerUserWithOwnerRole(FactoryUserModelTest.userModel());
        verify(userPersistenceDomainPort).saveUser(any(UserModel.class));
    }

    @Test
    void findUsuarioByCorreo() {
        when(userPersistenceDomainPort.findByEmail("test-email@gmail.com")).thenReturn(FactoryUserModelTest.userModel());
        UserModel userModel = userUseCase.findUserByEmail("test-email@gmail.com");
        verify(userPersistenceDomainPort).findByEmail(any(String.class));
        Assertions.assertEquals(FactoryUserModelTest.userModel().getEmail(), userModel.getEmail());
    }

    @Test
    void throwEmptyFieldsExceptionWhenRegisterUser() {
        UserModel userModelWithFieldsEmpty =
                FactoryUserModelTest.userModelFieldsEmpty();
        Assertions.assertThrows(
                EmptyFieldsException.class,
                () -> { userUseCase.registerUserWithOwnerRole(userModelWithFieldsEmpty); }
        );
    }

    @Test
    void throwEmailStructureInvalidExceptionWhenRegisterUser() {
        UserModel userModelEmailStructureInvalid =
                FactoryUserModelTest.userModelEmailStructureInvalid();
        Assertions.assertThrows(
                InvalidEmailFormatException.class,
                () -> { userUseCase.registerUserWithOwnerRole(userModelEmailStructureInvalid); }
        );
    }

    @Test
    void throwEmailExistsExceptionWhenRegisterUser() {
        UserModel userModel1 = FactoryUserModelTest.userModel();
        when(userPersistenceDomainPort.saveUser(userModel1)).thenReturn(userModel1);
        userUseCase.registerUserWithOwnerRole(userModel1);

        UserModel userModel2 = FactoryUserModelTest.userModel();
        when(userPersistenceDomainPort.saveUser(userModel2)).thenThrow(new EmailExistsException("Email already exists"));

    }

    @Test
    void throwEmailNotFoundExceptionWhenFindEmail() {
        Assertions.assertThrows(
                EmailNotFoundException.class,
                () -> { userUseCase.findUserByEmail("email-not-exists@gmail.com"); }
        );
    }

    @Test
    void throwInvalidCellPhoneFormatExceptionWhenRegisterUser() {
        UserModel userModelCellPhoneInvalid =
                FactoryUserModelTest.userModelCellPhoneInvalid();
        Assertions.assertThrows(
                InvalidCellPhoneFormatException.class,
                () -> { userUseCase.registerUserWithOwnerRole(userModelCellPhoneInvalid); }
        );
    }
}