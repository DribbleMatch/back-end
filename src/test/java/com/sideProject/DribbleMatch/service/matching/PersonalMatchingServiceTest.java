package com.sideProject.DribbleMatch.service.matching;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PersonalMatchingServiceTest {

    @InjectMocks
    private PersonalMatchingServiceImpl personalMatchingService;

    @Nested
    @DisplayName("CreatePersonalMatchingTest")
    public class CreatePersonalMatchingTest {

        @DisplayName("개인 매칭을 생성한다")
        @Test
        public void createPersonalMatching() {

        }

        @DisplayName("개인 매칭 중 현재 종료되지 않은 매칭 중 중복되는 이름이 존재하면 에러가 발생한다")
        @Test
        public void createPersonalMatching2() {

        }
    }

    @Nested
    @DisplayName("UpdatePersonalMatching2")
    public class UpdatePersonalMatching2 {

        @DisplayName("개인 매칭 정보를 수정한다")
        @Test
        public void updatePersonalMatching() {

        }

        @DisplayName("개인 매칭 중 현재 종료되지 않은 매칭 중 중복되는 이름이 존재하면 에러가 발생한다")
        @Test
        public void updatePersonalMatching2() {

        }
    }

    @Nested
    @DisplayName("DeletePersonalMatchingTest")
    public class DeletePersonalMatchingTest {

        @DisplayName("개인 매칭을 삭제한다")
        @Test
        public void deletePersonalMatching() {

        }
    }

    @Nested
    @DisplayName("EnterPersonalMatchingStadiumInfoTest")
    public class EnterPersonalMatchingStadiumInfoTest {

        //todo: 경기장 Entity와의 관계 생각하면서 구현
        @DisplayName("개인 매칭의 경기장이 정해지면 경기장 정보를 입력한다")
        @Test
        public void enterPersonalMatchingStadiumInfoTest() {

        }
    }

    @Nested
    @DisplayName("findPersonalMatchingTest")
    public class findPersonalMatchingTest {

        @DisplayName("개인 매칭을 상세 조회한다")
        @Test
        public void findPersonalMatching() {

        }

        @DisplayName("존재하지 않는 개인 매칭이면 에러가 발생한다")
        @Test
        public void findPersonalMatching2() {

        }
    }

    @Nested
    @DisplayName("findAllPersonalMatchingsTest")
    public class findAllPersonalMatchingsTest {

        @DisplayName("개인 매칭을 전체 조회한다")
        @Test
        public void findAllPersonalMatchings() {

        }

        @DisplayName("개인 매칭을 지역별로 조회한다")
        @Test
        public void findAllPersonalMatchings2() {

        }
    }
}
