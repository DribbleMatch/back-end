package com.sideProject.DribbleMatch.repository.user;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.user.ENUM.Gender;
import com.sideProject.DribbleMatch.entity.user.ENUM.Position;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;

    private Region initRegion(String dong) {
        return regionRepository.save(Region.builder()
                .siDo("서울특별시")
                .siGunGu("영등포구")
                .eupMyeonDongGu(dong)
                .latitude(37.5347)
                .longitude(126.9065)
                .build());
    }

    private User initUser(String email, String name, Region region) {
        return User.builder()
                .email(email)
                .password("test1234!A")
                .nickName(name)
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
                .region(region)
                .build();
    }

    @Nested
    @DisplayName("CreateUserTest")
    @Transactional
    public class CreateUserTest{

        @DisplayName("멤버를 저장한다")
        @Test
        public void createUser() {

            // given
            Region region = initRegion("당산동");

            User user = initUser("test@test.com", "test", region);

            // when
            User savedUser = userRepository.save(user);

            // then
            assertThat(savedUser).isNotNull();
            assertThat(savedUser.getEmail()).isEqualTo("test@test.com");
        }

        @DisplayName("@NotNull로 지정된 컬럼에 데이터가 null이면 에러가 발생한다")
        @Test
        public void createUser2() {

            // given
            Region region = initRegion("당산동");
            User user = User.builder()
//                    .email("test@test.com")
//                    .password("test1234!A")
                    .nickName("test")
                    .gender(Gender.MALE)
                    .birth(LocalDate.of(2001, 1, 1))
                    .position(Position.CENTER)
                    .winning(10)
                    .region(region)
                    .build();

            // when, then
            assertThatThrownBy(() -> userRepository.save(user))
                    .isInstanceOf(ConstraintViolationException.class);
        }

        @DisplayName("unique = true 인 컬럼이 중복일 경우 에러가 발생한다")
        @Test
        public void createUser3() {

            // given
            Region region = initRegion("당산동");
            User user1 = initUser("test@test.com", "test", region);
            User user2 = initUser("test@test.com", "test", region);

            // when
            userRepository.save(user1);

            // then
            assertThatThrownBy(() -> userRepository.save(user2))
                    .isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @Nested
    @DisplayName("SelectUserTest")
    public class SelectUserTest {

        @DisplayName("User를 조회한다")
        @Test
        public void selectUser() {

            // given
            Region region = initRegion("당산동");
            User savedUser = userRepository.save(initUser("test@test.com", "test", region));

            // when
            User selectedUser = userRepository.findById(savedUser.getId()).get();

            // then
            assertThat(selectedUser).isNotNull();
            assertThat(savedUser).isEqualTo(selectedUser);
        }

        @DisplayName("모든 User를 조회한다")
        @Test
        public void selectUser2() {

            // given
            Region region = initRegion("당산동");
            User savedUser1 = userRepository.save(initUser("test1@test.com", "test1", region));
            User savedUser2 = userRepository.save(initUser("test2@test.com", "test2", region));

            // when
            List<User> users = userRepository.findAll();

            assertThat(users.size()).isEqualTo(2);
            assertThat(users.contains(savedUser1)).isTrue();
            assertThat(users.contains(savedUser2)).isTrue();
        }

        @DisplayName("없는 User이면 에러가 발생한다")
        @Test
        public void selectUser3() {

            // given
            Region region = initRegion("당산동");
            User savedUser = userRepository.save(initUser("test@test.com", "test", region));

            // when, then
            assertThatThrownBy(() -> userRepository.findById(savedUser.getId() + 1).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_USER_ID)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 사용자가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("DeleteUserTest")
    public class DeleteUserTest {

        @DisplayName("User를 삭제한다")
        @Test
        public void  deleteUser() {

            // given
            Region region = initRegion("당산동");
            User savedUser = userRepository.save(initUser("test@test.com", "test", region));

            // when
            userRepository.deleteById(savedUser.getId());

            // then
            assertThat(userRepository.findById(savedUser.getId()).isPresent()).isFalse();
        }
    }

    @Nested
    @DisplayName("FindByEmailTest")
    public class FindByEmailTest {

        @DisplayName("Email로 User를 조회한다")
        @Test
        public void findByEmail() {

            // given
            Region region = initRegion("당산동");
            User savedUser = userRepository.save(initUser("test@test.com", "test", region));

            // when
            User selectedUser = userRepository.findByEmail(savedUser.getEmail()).get();

            // then
            assertThat(selectedUser).isNotNull();
            assertThat(savedUser).isEqualTo(selectedUser);
        }

        @DisplayName("없는 Email이면 에러가 발생한다")
        @Test
        public void findByEmail2() {

            // given
            Region region = initRegion("당산동");
            User savedUser = userRepository.save(initUser("test@test.com", "test", region));

            // when, then
            assertThatThrownBy(() -> userRepository.findByEmail("no_exist_email.com").orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_EMAIL)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 이메일이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("FindByNickNameTest")
    public class FindByNickNameTest {

        @DisplayName("NickName으로 User를 조회한다")
        @Test
        public void findByNickName() {

            // given
            Region region = initRegion("당산동");
            User savedUser = userRepository.save(initUser("test@test.com", "test", region));

            // when
            User selectedUser = userRepository.findByNickName(savedUser.getNickName()).get();

            // then
            assertThat(selectedUser).isNotNull();
            assertThat(savedUser).isEqualTo(selectedUser);
        }

        @DisplayName("없는 NickName이면 에러가 발생한다")
        @Test
        public void findByNickNAme2() {

            // given
            Region region = initRegion("당산동");
            User savedUser = userRepository.save(initUser("test@test.com", "test", region));

            // when, then
            assertThatThrownBy(() -> userRepository.findByNickName("no_exist_nickName").orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_NICKNAME)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 닉네임이 존재하지 않습니다.");
        }
    }
}
