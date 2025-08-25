package com.example.user.service;

import com.example.user.entity.*;
import com.example.user.repository.RoleRepository;
import com.example.user.repository.StudentRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; // ✅ Inject


    /**
     * Converts a validated PendingStudent into a permanent User and Student profile.
     * Username = CIN, Email = firstname.lastname@example.com, Password = CIN (encoded)
     */
    public void createStudentFromPending(PendingStudent pendingStudent) {
        // Create and populate User
        User user = new User();

        String cin = pendingStudent.getCin();
        String email = pendingStudent.getFirstName().toLowerCase() + "." +
                pendingStudent.getLastName().toLowerCase() + "@esprit.com";

        user.setUsername(pendingStudent.getRegistrationNumber());
        user.setEmail(email);                        // Email = firstname.lastname@example.com
        user.setPassword(passwordEncoder.encode(cin));  // Password = CIN encoded

        // Basic personal info
        user.setFirstname(pendingStudent.getFirstName());
        user.setLastname(pendingStudent.getLastName());
        user.setBirthdate(pendingStudent.getBirthdate());
        user.setGender(pendingStudent.getGender());
        user.setPhone(pendingStudent.getPhone());
        user.setAddress(pendingStudent.getAddress());
        user.setCin(cin);
        user.setActive(true);

        // Assign Role
        Role studentRole = roleRepository.findByRoleType(RoleType.STUDENT)
                .orElseThrow(() -> new RuntimeException("Role STUDENT not found"));
        user.setRole(studentRole);

        // Create and attach Student profile
        Student student = new Student();
        student.setUser(user);  // one-to-one link
        student.setRegistrationNumber(pendingStudent.getRegistrationNumber());
        student.setEnrollmentDate(pendingStudent.getEnrollmentDate());
        student.setAcademicYear(Integer.parseInt(pendingStudent.getAcademicYear()));
        student.setStatus("active");

        // Link user to student profile
        user.setStudentProfile(student);

        // Persist the User (and student through cascade if enabled, otherwise separately)
        userRepository.save(user);
        System.out.println("✅ New student created: " + email);
// ✅ Send email to the real form-provided email with system credentials
        emailService.sendStudentAccountInfoEmail(
                pendingStudent.getEmail(),       // form-provided email
                email,
                cin,
                pendingStudent.getRegistrationNumber()
        );

        //emailService.sendCredentialsEmail(email, cin, cin);


        System.out.println("✅ New student created:");
        System.out.println("   Username: " + cin);
        System.out.println("   Email: " + email);
        System.out.println("   Password: CIN");
    }
}
