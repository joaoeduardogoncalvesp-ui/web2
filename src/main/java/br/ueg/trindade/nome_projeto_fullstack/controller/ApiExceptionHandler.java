package br.ueg.trindade.nome_projeto_fullstack.controller;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(ResponseStatusException.class)
 public ResponseEntity<?> negocio(ResponseStatusException e) { return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", e.getReason()==null ? "Operação inválida" : e.getReason())); }
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<?> validacao(MethodArgumentNotValidException e) { return ResponseEntity.badRequest().body(Map.of("message", "Confira os campos obrigatórios e o formato dos dados.")); }
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<?> duplicado(DataIntegrityViolationException e) { return ResponseEntity.status(409).body(Map.of("message", "Registro duplicado ou vinculado a outro cadastro.")); }
}
