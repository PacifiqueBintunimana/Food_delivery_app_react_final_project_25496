package com.auca.onlineFoodDeliberyApp.controller;

import com.auca.onlineFoodDeliberyApp.model.User;
import com.auca.onlineFoodDeliberyApp.model.UserDTO;
import com.auca.onlineFoodDeliberyApp.service.UserService;
//import com.auca.onlineFoodDeliberyApp.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin")
public class AdminUserController {

    @Autowired
    private UserService userService;

//    // Get dashboard statistics
//    @GetMapping("/dashboard-data")
//    public ResponseEntity<Map<String, Object>> getDashboardData() {
//        Map<String, Object> dashboardData = userService.getDashboardStatistics();
//        return ResponseEntity.ok(dashboardData);
//    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Create new user
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        User newUser = userService.createUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Update user
    @PostMapping("/users/update")
    public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Search users
    @GetMapping("/search/results")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {
        List<User> users = userService.searchUsers(username, email);
        return ResponseEntity.ok(users);
    }
//    // Add these new endpoints to your existing controller
//
//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }
//
//    @GetMapping("/users/stats")
//    public ResponseEntity<Map<String, Object>> getUserStats() {
//        Map<String, Object> stats = userService.getUserStatistics();
//        return ResponseEntity.ok(stats);
//    }
//
//    @GetMapping("/users/roles/distribution")
//    public ResponseEntity<Map<String, Integer>> getRoleDistribution() {
//        Map<String, Integer> distribution = userService.getRoleDistribution();
//        return ResponseEntity.ok(distribution);
//    }
//
//    @PutMapping("/users/{id}/status")
//    public ResponseEntity<User> updateUserStatus(
//            @PathVariable Long id,
//            @RequestParam String status
//    ) {
//        User user = userService.updateUserStatus(id, status);
//        return ResponseEntity.ok(user);
//    }
//
//    @PostMapping("/users/bulk")
//    public ResponseEntity<List<User>> createBulkUsers(@RequestBody List<UserDTO> userDTOs) {
//        List<User> users = userService.createBulkUsers(userDTOs);
//        return new ResponseEntity<>(users, HttpStatus.CREATED);
//    }

    // Upload users from file
    @PostMapping("/upload/users")
    public ResponseEntity<String> uploadUsers(@RequestParam("file") MultipartFile file) {
        try {
            userService.processUserUpload(file);
            return ResponseEntity.ok("Users uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }

    // Download users in CSV format
    @GetMapping("/download/users")
    public ResponseEntity<?> downloadUsers(
            @RequestParam String format,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String dateRange) {

        List<User> users = userService.getUsersByFilterAndDate(filter, dateRange);

        if ("csv".equalsIgnoreCase(format)) {
            return generateCsvResponse(users);
        } else if ("pdf".equalsIgnoreCase(format)) {
            return generatePdfResponse(users);
        }

        return ResponseEntity.badRequest().body("Unsupported format");
    }

    private ResponseEntity<ByteArrayResource> generateCsvResponse(List<User> users) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream),
                    CSVFormat.DEFAULT.withHeader("ID", "Username", "Email", "Role", "Status"));

            for (User user : users) {
                csvPrinter.printRecord(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getStatus()
                );
            }
            csvPrinter.flush();

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate CSV file", e);
        }
    }

    private ResponseEntity<ByteArrayResource> generatePdfResponse(List<User> users) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, outputStream);

            document.open();
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            // Add headers
            Stream.of("ID", "Username", "Email", "Role", "Status")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(headerTitle));
                        table.addCell(header);
                    });

            // Add data rows
            for (User user : users) {
                table.addCell(user.getId().toString());
                table.addCell(user.getUsername());
                table.addCell(user.getEmail());
                table.addCell(user.getRole().toString());
                table.addCell(user.getStatus());
            }

            document.add(table);
            document.close();

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF file", e);
        }
    }
}
