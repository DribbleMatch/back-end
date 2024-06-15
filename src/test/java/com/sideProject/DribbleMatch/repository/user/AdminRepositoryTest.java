package com.sideProject.DribbleMatch.repository.user;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.config.QuerydslConfig;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.Admin;
import com.sideProject.DribbleMatch.entity.user.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
public class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    private Admin initAdmin(String email, String nickName) {
        return Admin.builder()
                .email(email)
                .password("test1234!A")
                .nickName(nickName)
                .build();
    }

    @Nested
    @DisplayName("CreateAdminTest")
    @Transactional
    public class CreateAdminTest{

        @DisplayName("Admin를 저장한다")
        @Test
        public void createAdmin() {

            // given
            Admin admin = initAdmin("test@test.com", "test");

            // when
            Admin savedAdmin = adminRepository.save(admin);

            // then
            assertThat(savedAdmin).isNotNull();
            assertThat(savedAdmin.getEmail()).isEqualTo("test@test.com");
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createAdmin2() {

            // given
            Admin admin = Admin.builder()
//                    .email("test@test.com")
//                    .password("test1234!A")
                    .nickName("test")
                    .build();

            // when, then
            assertThatThrownBy(() -> adminRepository.save(admin))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @DisplayName("unique = true 인 컬럼이 중복일 경우 에러가 발생한다")
        @Test
        public void createAdmin3() {

            // given
            Admin admin1 = initAdmin("test@test.com", "test");
            Admin admin2 = initAdmin("test@test.com", "test");

            // when
            adminRepository.save(admin1);

            // then
            assertThatThrownBy(() -> adminRepository.save(admin2))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectAdminTest")
    public class SelectAdminTest {

        @DisplayName("Admin를 조회한다")
        @Test
        public void selectAdmin() {

            // given
            Admin savedAdmin = adminRepository.save(initAdmin("test@test.com", "test"));

            // when
            Admin selectedAdmin = adminRepository.findById(savedAdmin.getId()).get();

            // then
            assertThat(selectedAdmin).isNotNull();
            assertThat(selectedAdmin).isEqualTo(savedAdmin);
        }

        @DisplayName("모든 Admin를 조회한다")
        @Test
        public void selectAdmin2() {

            // given
            Admin savedAdmin1 = adminRepository.save(initAdmin("test1@test.com", "test1"));
            Admin savedAdmin2 = adminRepository.save(initAdmin("test2@test.com", "test2"));

            // when
            List<Admin> admins = adminRepository.findAll();

            assertThat(admins.size()).isEqualTo(2);
            assertThat(admins.contains(savedAdmin1)).isTrue();
            assertThat(admins.contains(savedAdmin2)).isTrue();
        }

        @DisplayName("없는 Admin이면 에러가 발생한다")
        @Test
        public void selectAdmin3() {

            // given
            Admin savedAdmin = adminRepository.save(initAdmin("test@test.com", "test"));

            // when, then
            assertThatThrownBy(() -> adminRepository.findById(savedAdmin.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_ADMIN_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 관리자가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("DeleteAdminTest")
    public class DeleteAdminTest {

        @DisplayName("Admin를 삭제한다")
        @Test
        public void  deleteAdmin() {

            // given
            Admin savedAdmin = adminRepository.save(initAdmin("test@test.com", "test"));

            // when
            adminRepository.deleteById(savedAdmin.getId());

            // then
            assertThat(adminRepository.findById(savedAdmin.getId()).isPresent()).isFalse();
        }
    }

    @Nested
    @DisplayName("FindByEmailTest")
    public class FindByEmailTest {

        @DisplayName("Email로 Admin를 조회한다")
        @Test
        public void findByEmail() {

            // given
            Admin savedAdmin = adminRepository.save(initAdmin("test@test.com", "test"));

            // when
            Admin selectedAdmin = adminRepository.findByEmail(savedAdmin.getEmail()).get();

            // then
            assertThat(selectedAdmin).isNotNull();
            assertThat(selectedAdmin).isEqualTo(savedAdmin);
        }

        @DisplayName("없는 Email이면 에러가 발생한다")
        @Test
        public void findByEmail2() {

            // given
            Admin savedAdmin = adminRepository.save(initAdmin("test@test.com", "test"));

            // when, then
            assertThatThrownBy(() -> adminRepository.findByEmail("no_exist_email.com").orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_EMAIL)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 이메일이 존재하지 않습니다.");
        }
    }
}
