package com.swiggy.wallet.Expection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorDetails> InsufficientBalanceExceptionHandler(InsufficientBalanceException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ErrorDetails> WalletNotFoundExceptionHandler(WalletNotFoundException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ErrorDetails> InvalidAmountExceptionHandler(InvalidAmountException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OperationNotPossible.class)
    public ResponseEntity<ErrorDetails> OperationNotPossibleHandler(OperationNotPossible exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> UserNotFoundExceptionHandler(UserNotFoundException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getBindingResult().getFieldError().getDefaultMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectWalletException.class)
    public ResponseEntity<ErrorDetails> IncorrectWalletExceptionHandler(IncorrectWalletException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectUserException.class)
    public ResponseEntity<ErrorDetails> IncorrectUserExceptionHandler(IncorrectUserException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetails> BadCredentialsExceptionHandler(BadCredentialsException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> UserAlreadyExistsExceptionHandler(UserAlreadyExistsException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SelfTransferException.class)
    public ResponseEntity<ErrorDetails> SelfTransferExceptionHandler(SelfTransferException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> HttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyMismatchException.class)
    public ResponseEntity<ErrorDetails> CurrencyMismatchExceptionHandler(CurrencyMismatchException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
