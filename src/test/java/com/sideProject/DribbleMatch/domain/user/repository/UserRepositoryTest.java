package com.sideProject.DribbleMatch.domain.user.repository;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Gender;
import com.sideProject.DribbleMatch.domain.user.entity.ENUM.Position;
import com.sideProject.DribbleMatch.domain.user.entity.User;
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
    UserRepository userRepository;

    private User initUser(String email, String nickName) {
        return User.builder()
                .email(email)
                .password("test1234")
                .nickName(nickName)
                .gender(Gender.MALE)
                .birth(LocalDate.of(2001, 1, 1))
                .position(Position.CENTER)
                .winning(10)
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
            User user = initUser("test@test.com", "test");

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
            User user = User.builder()
//                    .email("test@test.com")
//                    .password("test1234")
                    .nickName("test")
                    .gender(Gender.MALE)
                    .birth(LocalDate.of(2001, 1, 1))
                    .position(Position.CENTER)
                    .winning(10)
                    .build();

            // when, then
            assertThatThrownBy(() -> userRepository.save(user))
                    .isInstanceOf(ConstraintViolationException.class)
                    .extracting("message")
                    .asString()
                    .contains("이메일이 입력되지 않았습니다.")
                    .contains("비밀번호가 입력되지 않았습니다.");
        }

        @DisplayName("unique = true 인 컬럼이 중복일 경우 에러가 발생한다")
        @Test
        public void createUser3() {

            // given
            User user1 = User.builder()
                    .email("test@test.com")
                    .password("test1234")
                    .nickName("test")
                    .gender(Gender.MALE)
                    .birth(LocalDate.of(2001, 1, 1))
                    .position(Position.CENTER)
                    .winning(10)
                    .build();

            User user2 = User.builder()
                    .email("test@test.com")
                    .password("test1234")
                    .nickName("test")
                    .gender(Gender.MALE)
                    .birth(LocalDate.of(2001, 1, 1))
                    .position(Position.CENTER)
                    .winning(10)
                    .build();

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
            User savedUser = userRepository.save(initUser("test@test.com", "test"));

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
            User savedUser1 = userRepository.save(initUser("test1@test.com", "test1"));
            User savedUser2 = userRepository.save(initUser("test2@test.com", "test2"));

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
            User savedUser = userRepository.save(initUser("test@test.com", "test"));

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
            User savedUser = userRepository.save(initUser("test@test.com", "test"));

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
            User savedUser = userRepository.save(initUser("test@test.com", "test"));

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
            User savedUser = userRepository.save(initUser("test@test.com", "test"));

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
            User savedUser = userRepository.save(initUser("test@test.com", "test"));

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
            User savedUser = userRepository.save(initUser("test@test.com", "test"));

            // when, then
            assertThatThrownBy(() -> userRepository.findByNickName("no_exist_nickName").orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_NICKNAME)))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("해당 닉네임이 존재하지 않습니다.");
        }
    }
}
