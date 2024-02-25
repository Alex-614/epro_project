package de.thbingen.epro.project.okrservice.exceptions;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Central Exception Handler for all Controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> validationException(MethodArgumentNotValidException  ex) {
        FieldError error = ex.getFieldError();
        if (error == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An unexpected Error occured!");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("'" + error.getField() + "' " + error.getDefaultMessage());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> notReadableException(HttpMessageNotReadableException  ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("An unexpected Error occured, could not read HttpMessage!\n" + 
                                                                ex.getMessage());
    }
    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> psqlException(PSQLException  ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("An unexpected Error occured, invalid Request/Arguments!");
    }
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> authenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException  ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid Session!");
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> usernameNotFoundException(UsernameNotFoundException  ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid Session!");
    }




    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> userAlreadyExistsException(UserAlreadyExistsException  ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists!");
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

    @ExceptionHandler(MaxObjectivesReachedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> maxObjectivesReachedException(MaxObjectivesReachedException  ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Maximal objectives reached!");
    }
    @ExceptionHandler(MaxKeyResultsReachedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> maxKeyResultsReachedException(MaxKeyResultsReachedException  ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Maximal key results reached!");
    }

    /*@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> exception(Exception  ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error!");
    }*/
}