package com.paccy.controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.paccy.dto.UserDTO;
import com.paccy.model.User;
import com.paccy.service.serviceimplimentation.UserServiceImplimentation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin")
public class AdminUserController {

    @Autowired
    private UserServiceImplimentation userService;

    // Dashboard Statistics
    @GetMapping("/dashboard-data")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboardData = userService.getDashboardStatistics();
        return ResponseEntity.ok(dashboardData);
    }

    // Get All Users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Create New User
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        User newUser = userService.createUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Get User by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    // Update User
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);  // Ensure ID is set
        User updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete User
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Search Users
    @GetMapping("/search/results")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {
        List<User> users = userService.searchUsers(username, email);
        return ResponseEntity.ok(users);
    }

    // User File Upload
    @PostMapping("/upload/users")
    public ResponseEntity<String> uploadUsers(@RequestParam("file") MultipartFile file) {
        try {
            userService.processUserUpload(file);
            return ResponseEntity.ok("Users uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }

    // User Download (CSV and PDF)
    @GetMapping("/download/users")
    public ResponseEntity<?> downloadUsers(
            @RequestParam String format,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String dateRange) {

        List<User> users = userService.getUsersByFilterAndDate(filter, dateRange);

        try {
            if ("csv".equalsIgnoreCase(format)) {
                return generateCsvResponse(users);
            } else if ("pdf".equalsIgnoreCase(format)) {
                return generatePdfResponse(users);
            }
            return ResponseEntity.badRequest().body("Unsupported format");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to generate file: " + e.getMessage());
        }
    }

    // CSV Generation Method
    private ResponseEntity<ByteArrayResource> generateCsvResponse(List<User> users) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream),
                     CSVFormat.DEFAULT.withHeader("ID", "Username", "Email", "Role", "Status"))) {

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
        }
    }

    // PDF Generation Method
    private ResponseEntity<ByteArrayResource> generatePdfResponse(List<User> users) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
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
        }
    }
}