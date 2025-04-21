package ru.t1.service.util.validator;

import org.springframework.stereotype.Component;
import ru.t1.exceptions.InvalidEmailException;

import java.util.regex.Pattern;

@Component
public class UserValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    //Пускай будет возможность пустого email (в смысле null), но неправильные отсеим
    public void validateEmail(String email) {
        if (email != null && !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException(email);
        }
    }
}
