package ru.t1.exceptions;

public class EntityNotFoundException extends CustomException {

    public EntityNotFoundException(String nameOfEntity, Long id) {
        super(ExceptionTypeEnum.ENTITY_NOT_FOUND, String.format("Entity - (%s) with id = (%d) not found", nameOfEntity, id));
    }
}
