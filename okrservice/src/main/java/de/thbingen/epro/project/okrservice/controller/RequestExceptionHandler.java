package de.thbingen.epro.project.okrservice.controller;

import de.thbingen.epro.project.okrservice.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RequestExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> validationException(MethodArgumentNotValidException  ex) {
        FieldError error = ex.getFieldError();
        if (error == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An unexpected Error occured!");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error.getDefaultMessage());
    }



    @ExceptionHandler(UnitAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> unitAlreadyExistsException(UnitAlreadyExistsException  ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Unit not found!");
    }

    @ExceptionHandler(UnitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> unitNotFoundException(UnitNotFoundException  ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unit already exists!");
    }

    //BusinessUnitAlreadyExistsException
    @ExceptionHandler(BusinessUnitAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> businessUnitAlreadyExistsException(BusinessUnitAlreadyExistsException  ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Business unit already exists!");
    }

    @ExceptionHandler(BusinessUnitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> businessUnitNotFoundException(BusinessUnitNotFoundException  ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business unit not found!");
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> companyNotFoundException(CompanyNotFoundException  ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found!");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> userNotFoundException(UserNotFoundException  ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
    }

    @ExceptionHandler(ObjectiveNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> objectiveNotFoundException(ObjectiveNotFoundException  ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Objective not found!");
    }

    @ExceptionHandler(KeyResultNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> keyResultNotFoundException(KeyResultNotFoundException  ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Key result not found!");
    }

    @ExceptionHandler(MaxCompanyObjectivesReachedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> maxCompanyObjectivesReachedException(MaxCompanyObjectivesReachedException  ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Maximal company objectives reached!");
    }

    /*@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> exception(Exception  ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error!");
    }*/
}