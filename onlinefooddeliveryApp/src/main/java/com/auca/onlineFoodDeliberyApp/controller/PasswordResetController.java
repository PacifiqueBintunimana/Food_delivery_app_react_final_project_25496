package com.auca.onlineFoodDeliberyApp.controller;

import com.auca.onlineFoodDeliberyApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth/forgotPassword")
    public ResponseEntity<?> handleForgotPassword(@RequestParam("email") String email) {
        Map<String, String> response = new HashMap<>();

        if (!userService.doesEmailExist(email)) {
            response.put("error", "Email address not found.");
            return ResponseEntity.badRequest().body(response);
        }

        userService.deleteExistingResetTokenByEmail(email);
        boolean emailSent = userService.sendPasswordResetEmail(email);

        if (emailSent) {
            response.put("message", "A reset link has been sent to your email.");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Failed to send email. Please try again.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/auth/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam("token") String token) {
        Map<String, String> response = new HashMap<>();

        if (token == null) {
            response.put("error", "Token is missing.");
            return ResponseEntity.badRequest().body(response);
        }

        boolean isValidToken = userService.validatePasswordResetToken(token);
        if (!isValidToken) {
            response.put("error", "Invalid or expired token.");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("message", "Token is valid");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/resetPassword/{token}")
    public ResponseEntity<?> handlePasswordReset(@PathVariable String token, @RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");
        String confirmNewPassword = request.get("confirmNewPassword"); // Remove space
        Map<String, String> response = new HashMap<>();

        if (!newPassword.equals(confirmNewPassword)) {
            response.put("error", "Passwords do not match. Please try again.");
            return ResponseEntity.badRequest().body(response);
        }

        boolean isResetSuccessful = userService.resetUserPassword(token, newPassword);

        if (isResetSuccessful) {
            response.put("message", "Your password has been successfully reset. You can now log in.");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Failed to reset password. Please try again.");
            return ResponseEntity.badRequest().body(response);
        }
    }

}
