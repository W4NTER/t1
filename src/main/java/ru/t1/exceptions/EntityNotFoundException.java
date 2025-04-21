package ru.t1.exceptions;

public class EntityNotFoundException extends CustomException {

    public EntityNotFoundException(String nameOfEntity, Long id) {
        super(EntityNotFoundException.class, String.format("Entity - (%s) with id = (%d) not found", nameOfEntity, id));
    }
}
