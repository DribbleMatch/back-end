package com.sideProject.DribbleMatch.service.user;

import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.Admin;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.user.AdminRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Region initRegion(String dong) {
        return Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .latitude(37.5347)
                .longitude(126.9065)
                .build();
    }

    private User initUser(String email, String name, Region region) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode("test1234!A"))
                .nickName(name)
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
                .region(region)
                .build();
    }

    private Admin initAdmin(String email, String nickName) {
        return Admin.builder()
                .email(email)
                .password("test1234!A")
                .nickName(nickName)
                .build();
    }

    @Nested
    @DisplayName("changeToAdminTest")
    public class changeToAdminTest {

        @DisplayName("회원을 관리자로 변경한다")
        @Test
        public void changeToAdmin() {

            // given
            Region region = initRegion("당산동");

            Long fakeUserId = 1L;
            User user = initUser("test@test.com", "test", region);
            ReflectionTestUtils.setField(user, "id", fakeUserId);

            Long fakeAdminId = 1L;
            Admin admin = initAdmin("test@test.com", "test");
            ReflectionTestUtils.setField(admin, "id", fakeAdminId);

            // mocking
            when(userRepository.findById(fakeUserId)).thenReturn(Optional.ofNullable(user));
            doNothing().when(userRepository).delete(user);
            when(adminRepository.save(any(Admin.class))).thenReturn(admin);

            // when
            Long adminId = adminService.changeToAdmin(fakeUserId);

            // then
            assertThat(adminId).isEqualTo(fakeAdminId);
        }
    }
}
