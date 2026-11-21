package tn.esprit.autoloc.web.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.autoloc.web.dto.ErreurDTO;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Erreurs métier levées par nos services (404, 409, 400)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErreurDTO> gererResponseStatus(ResponseStatusException ex,
                                                         HttpServletRequest request) {
        return construire(ex.getStatusCode(), ex.getReason(), request);
    }

    // JSON mal formé, valeur d'enum inconnue, date ou nombre mal écrit
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErreurDTO> gererCorpsIllisible(HttpMessageNotReadableException ex,
                                                          HttpServletRequest request) {
        log.warn("Corps de requête illisible sur {} : {}", request.getRequestURI(), ex.getMessage());
        return construire(HttpStatus.BAD_REQUEST,
                "Corps de la requête illisible : JSON mal formé ou valeur invalide (enum, date, nombre)",
                request);
    }

    // Paramètre de mauvais type, par exemple /api/vehicules/abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErreurDTO> gererTypeParametre(MethodArgumentTypeMismatchException ex,
                                                         HttpServletRequest request) {
        String message = "Paramètre '" + ex.getName() + "' invalide : '" + ex.getValue() + "'";
        return construire(HttpStatus.BAD_REQUEST, message, request);
    }

    // Dernier recours
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurDTO> gererAutres(Exception ex, HttpServletRequest request) {
        if (ex instanceof ErrorResponse errorResponse) {
            // Exception Spring qui connaît déjà son statut (404 URL inconnue, 405, 400 paramètre manquant...)
            return construire(errorResponse.getStatusCode(),
                    errorResponse.getBody().getDetail(), request);
        }
        log.error("Erreur inattendue sur {} {}", request.getMethod(), request.getRequestURI(), ex);
        return construire(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne du serveur", request);
    }

    private ResponseEntity<ErreurDTO> construire(HttpStatusCode status, String message,
                                                 HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.resolve(status.value());
        String erreur = httpStatus != null ? httpStatus.getReasonPhrase() : "Erreur";
        ErreurDTO corps = new ErreurDTO(
                status.value(),
                erreur,
                message != null ? message : erreur,
                LocalDateTime.now(),
                request.getRequestURI(),
                List.of());
        return ResponseEntity.status(status).body(corps);
    }
}
