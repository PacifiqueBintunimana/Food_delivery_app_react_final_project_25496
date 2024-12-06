package com.paccy.service.serviceimplimentation;

import com.paccy.config.JwtProvider;
import com.paccy.dto.UserDTO;
import com.paccy.model.ResetToken;
import com.paccy.model.USER_ROLE;
import com.paccy.model.User;
import com.paccy.repository.ResetTokenRepository;
import com.paccy.repository.UserRepository;
import com.paccy.service.UserService;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImplimentation implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;


    @Autowired
    private JwtProvider jwtProvider;
    @Override
    public User userByJwtToken(String jwt) throws Exception {
       String email= jwtProvider.getEmailFromJwtToken(jwt);
       User user = finduserByEmail(email);
       return user;
    }

    @Override
    public User finduserByEmail(String email) throws Exception {
       User user =userRepository.findByEmail(email);
       if(user==null){
           throw new Exception("user not found");
       }
        return user;
    }


    @Transactional
    private void saveResetTokenForUser(User user, String token) {
        ResetToken resetToken = new ResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        resetTokenRepository.save(resetToken);
    }
    @Transactional
    public boolean sendPasswordResetEmail(String email) {
        // Retrieve user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            System.err.println("User not found with email: " + email);
            return false;
        }

        // Delete existing reset tokens for the email
        deleteExistingResetTokenByEmail(email);

        // Generate a new reset token and save it
        String token = UUID.randomUUID().toString();
        saveResetTokenForUser(user, token);

        // Construct the password reset link
        String resetLink = String.format("http://localhost:3000/resetPassword/%s", token);

        // Prepare the email content
        String emailContent = String.format("""
        <html>
            <body style="font-family: Arial, sans-serif; padding: 20px;">
                <h2 style="color: #2575fc;">Password Reset Request</h2>
                <p>Hello %s,</p>
                <p>We received a request to reset your password. Click the button below to proceed:</p>
                <a href="%s" style="background-color: #2575fc; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; margin: 20px 0;">Reset Password</a>
                <p>This link will expire in 15 minutes.</p>
                <p>If you didn't request this, please ignore this email.</p>
                <p>Best regards,<br>Your Application Team</p>
            </body>
        </html>
        """, user.getUsername(), resetLink);

        // Attempt to send the email
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            helper.setText(emailContent, true);
            helper.setReplyTo("no-reply@yourdomain.com");

            mailSender.send(message);

            System.out.printf("Password reset email sent successfully to: %s%n", email);
            return true;
        } catch (Exception e) {
            System.err.printf("Failed to send password reset email to %s: %s%n", email, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    @Transactional
    public void deleteExistingResetTokenByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            resetTokenRepository.findByUser(user).ifPresent(resetToken -> {
                resetTokenRepository.delete(resetToken);
            });
        }
    }

    public boolean validatePasswordResetToken(String token) {
        Optional<ResetToken> resetTokenOptional = resetTokenRepository.findByToken(token);
        return resetTokenOptional.map(resetToken -> resetToken.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    @Transactional
    public boolean resetUserPassword(String token, String newPassword) {
        if (!validatePasswordResetToken(token)) {
            return false;
        }

        Optional<User> userOptional = resetTokenRepository.findByToken(token).map(ResetToken::getUser);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            resetTokenRepository.deleteByToken(token);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User registerUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));  // Encode password
        user.setEmail(userDTO.getEmail());


        user.setRole(USER_ROLE.valueOf(userDTO.getRole()));
        user.setStatus("ACTIVE");
        return userRepository.save(user);
    }



    public void processUserUpload(MultipartFile file) throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                User user = new User();
                user.setUsername(data[4]);
                user.setFullName(data[0]);
                user.setEmail(data[1]);
                // Encode uploaded passwords
                user.setPassword(passwordEncoder.encode(data[2]));
                user.setRole(USER_ROLE.valueOf(data[3]));
                user.setStatus("ACTIVE");
                users.add(user);
            }
        }
        userRepository.saveAll(users);
    }
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboardData = new HashMap<>();

        // Example statistics - customize based on your requirements
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus("ACTIVE");
        long inactiveUsers = userRepository.countByStatus("INACTIVE");

        dashboardData.put("totalUsers", totalUsers);
        dashboardData.put("activeUsers", activeUsers);
        dashboardData.put("inactiveUsers", inactiveUsers);

        return dashboardData;
    }

    // Get User by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Update User
    @Transactional
    public User updateUser(UserDTO userDTO) {
        // Find existing user
        User existingUser = getUserById(userDTO.getId());

        // Update fields
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());

        // Only update password if a new password is provided
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // Update role if provided
        if (userDTO.getRole() != null) {
            existingUser.setRole(USER_ROLE.valueOf(userDTO.getRole()));
        }

        return userRepository.save(existingUser);
    }

    // Delete User
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    // Search Users
    public List<User> searchUsers(String username, String email) {
        // If both username and email are null, return all users
        if (username == null && email == null) {
            return userRepository.findAll();
        }

        // If only username is provided
        if (email == null) {
            return userRepository.findByUsernameContainingIgnoreCase(username);
        }

        // If only email is provided
        if (username == null) {
            return userRepository.findByEmailContainingIgnoreCase(email);
        }

        // If both username and email are provided
        return userRepository.findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(username, email);
    }

    // User download with filtering
    public List<User> getUsersByFilterAndDate(String filter, String dateRange) {
        // Implement filtering logic based on your requirements
        // This is a placeholder implementation
        List<User> allUsers = userRepository.findAll();

        if (filter != null) {
            switch (filter.toLowerCase()) {
                case "active":
                    return allUsers.stream()
                            .filter(user -> "ACTIVE".equals(user.getStatus()))
                            .collect(Collectors.toList());
                case "inactive":
                    return allUsers.stream()
                            .filter(user -> "INACTIVE".equals(user.getStatus()))
                            .collect(Collectors.toList());
                default:
                    return allUsers;
            }
        }

        return allUsers;
    }
    @Override
    public boolean doesEmailExist(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

}

