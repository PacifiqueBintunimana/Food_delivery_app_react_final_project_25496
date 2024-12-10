package com.paccy.controller;

import com.paccy.model.User;
import com.paccy.service.NotificationService;
import com.paccy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @GetMapping("/profile")
    public ResponseEntity<User> userByToken(@RequestHeader("Authorization") String token) throws Exception {

        User user = userService.userByJwtToken(token);


        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @GetMapping("/notifications/user/unread")
    public ResponseEntity<?> getUnreadNotifications(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        return ResponseEntity.ok(notificationService.getUnreadNotifications(user.getId()));
    }

    @PutMapping("/notifications/user/mark-all-as-read")
    public ResponseEntity<?> markNotificationsAsRead(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok("All notifications marked as read");
    }
}
