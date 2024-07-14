package com.example.shetuanlianmeng.service;

import com.example.shetuanlianmeng.entity.Club;
import com.example.shetuanlianmeng.entity.AdminClub;
import com.example.shetuanlianmeng.entity.ClubApplication;
import com.example.shetuanlianmeng.repository.ClubApplicationRepository;
import com.example.shetuanlianmeng.repository.ClubRepository;
import com.example.shetuanlianmeng.repository.AdminClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ClubApplicationService {

    @Autowired
    private ClubApplicationRepository clubApplicationRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private AdminClubRepository adminClubRepository;
    @Autowired
    private UserService userService;

    private final Path root = Paths.get("uploads");

    public List<ClubApplication> getAllClubApplications() {
        return clubApplicationRepository.findAll();
    }

    public List<ClubApplication> findByUserId(String userId) {
        return clubApplicationRepository.findByUserId(userId);
    }

    public ClubApplication createClubApplication(ClubApplication clubApplication) {
        List<ClubApplication> existingApplications = findByUserId(clubApplication.getUserId());
        for (ClubApplication existingApplication : existingApplications) {
            if ("pending".equals(existingApplication.getStatus())) {
                throw new IllegalStateException("You already have a pending application.");
            }
        }
        return clubApplicationRepository.save(clubApplication);
    }

    public String uploadImage(MultipartFile file) throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        Path path = root.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), path);
        return path.toString();
    }

    public void approveClubApplication(Long id) {
        ClubApplication application = clubApplicationRepository.findById(id).orElseThrow(() -> new IllegalStateException("Application not found"));
        application.setStatus("approved");
        clubApplicationRepository.save(application);

        Club club = new Club(application.getClubName(), application.getApplyTime(), application.getPublisher(), application.getCategory());
        clubRepository.save(club);

        AdminClub adminClub = new AdminClub();
        adminClub.setAdminId(Long.valueOf(application.getUserId()));
        adminClub.setClubId(club.getId());
        adminClubRepository.save(adminClub);

        userService.updateUserRole(Long.valueOf(application.getUserId()), "clubleader");
    }

    public void rejectClubApplication(Long id) {
        ClubApplication application = clubApplicationRepository.findById(id).orElseThrow(() -> new IllegalStateException("Application not found"));
        application.setStatus("rejected");
        clubApplicationRepository.save(application);
    }
}
