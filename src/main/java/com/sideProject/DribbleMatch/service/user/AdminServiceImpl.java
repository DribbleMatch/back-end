package com.sideProject.DribbleMatch.service.user;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.entity.user.Admin;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.user.AdminRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private UserRepository userRepository;
    private AdminRepository adminRepository;

    @Override
    public Long changeToAdmin(Long userId) {

        User userToChange = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        userRepository.delete(userToChange);

        Admin admin = adminRepository.save(Admin.userToAdmin(userToChange));

        return admin.getId();
    }
}
