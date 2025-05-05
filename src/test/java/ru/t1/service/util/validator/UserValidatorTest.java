package ru.t1.service.util.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.exceptions.InvalidEmailException;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    private UserValidator validator = new UserValidator();


    @Test
    @DisplayName("тест на проверку email без специальных знаков")
    void testThatValidateSimpleEmailReturnedSucceed() {
        assertDoesNotThrow(() -> validator.validateEmail("ryanGosling@mail.ru"));
        assertDoesNotThrow(() -> validator.validateEmail("AllaPugacheva@yandex.ru"));
    }

    //Понимаю, что это функционал,
    // который может вызывать вопросы и придуман "чтобы был",
    // но раз уж не убрал, тестировать надо
    @Test
    @DisplayName("тест проверяющий возможность email быть null")
    void testThatValidateNullEmailReturnedSucceed() {
        assertDoesNotThrow(() -> validator.validateEmail(null));
    }

    @Test
    @DisplayName("тест на проверку email со специальными знаками")
    void testThatValidateEmailWithSpecialCharactersReturnedSucceed() {
        assertDoesNotThrow(() -> validator.validateEmail("ryan_gosling@mail.ru"));
        assertDoesNotThrow(() -> validator.validateEmail("AllaPugacheva._-@yandex.ru"));
    }

    @Test
    @DisplayName("тест невалидного email")
    void testThatValidateEmailThrowingException() {
        assertThrows(InvalidEmailException.class,
                () -> validator.validateEmail("?????@mail.ru"));
        assertThrows(InvalidEmailException.class,
                () -> validator.validateEmail("ryan_gosling.mail.ru"));
        assertThrows(InvalidEmailException.class,
                () -> validator.validateEmail("ryan_gosling///@mail.ru"));

    }
}