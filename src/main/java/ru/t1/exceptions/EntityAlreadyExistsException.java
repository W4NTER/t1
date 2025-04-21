package ru.t1.exceptions;

public class EntityAlreadyExistsException extends CustomException {

    public EntityAlreadyExistsException(String nameOfEntity, Long id) {
        super(EntityAlreadyExistsException.class, String.format("Entity - (%s) with id = (%d) already exists", nameOfEntity, id));
    }
}
