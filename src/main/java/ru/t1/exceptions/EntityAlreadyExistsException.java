package ru.t1.exceptions;

public class EntityAlreadyExistsException extends CustomException {

    public EntityAlreadyExistsException(String nameOfEntity, Long id) {
        super(ExceptionTypeEnum.ENTITY_ALREADY_EXISTS, String.format("Entity - (%s) with id = (%d) already exists", nameOfEntity, id));
    }
}
