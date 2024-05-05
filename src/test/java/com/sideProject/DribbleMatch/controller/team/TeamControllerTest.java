package com.sideProject.DribbleMatch.controller.team;

import com.sideProject.DribbleMatch.common.security.JwtAuthenticationEntryPoint;
import com.sideProject.DribbleMatch.common.util.JwtUtil;
import com.sideProject.DribbleMatch.config.SecurityConfig;
import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.service.team.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = {TeamController.class, SecurityConfig.class})
@ActiveProfiles("test")
public class TeamControllerTest {

    @SpyBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private TeamService teamService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    private final Long leaderId = 1L;

    private Page<TeamResponseDto> convertToPage(List<TeamResponseDto> teams, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), teams.size());
        return new PageImpl<>(teams.subList(start, end), pageable, teams.size());
    }

    private TeamResponseDto initTeamResponseDto(String name) {
        return TeamResponseDto.builder()
                .name(name)
                .regionString("서울시 영등포구")
                .winning(10)
                .leaderNickName("test")
                .build();
    }

    @Nested
    @DisplayName("CreateTeamTest")
    public class CreateTeamTest{

        @DisplayName("팀 생성 API를 호출한다")
        @Test
        @WithMockUser(username = "1")
        public void createTeam() throws Exception {

            // given
            Long teamId = 1L;
            String request = "{\"name\": \"testTeam\", " +
                    "\"regionString\": \"서울시 영등포구 당산동\"}";

            // mocking
            when(teamService.createTeam(any(Long.TYPE), any(TeamCreateRequestDto.class))).thenReturn(teamId);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/team")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(1L));
        }

        @DisplayName("요청 데이터의 NotNull인 컬럼이 null이면 에러가 발생한다")
        @Test
        @WithMockUser(username = "1")
        public void createTeam2() throws Exception {

            // given
            Long teamId = 1L;
            String request = "{\"name\": \"testTeam\"}";
//                    "\"regionString\": \"서울시 영등포구\"}";

            // mocking
            when(teamService.createTeam(any(Long.TYPE), any(TeamCreateRequestDto.class))).thenReturn(teamId);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/team")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[지역이 입력되지 않았습니다.]"));
        }
    }

    @Nested
    @DisplayName("UpdateTeamTest")
    public class UpdateTeamTest {

        @DisplayName("팀 정보를 수정한다")
        @Test
        @WithMockUser(username = "1")
        public void updateTeam() throws Exception {

            // given
            Long teamId = 1L;
            String request = "{\"name\": \"testTeam\", " +
                    "\"regionString\": \"서울시 영등포구\", " +
                    "\"leaderId\": 2}";

            // mocking
            when(teamService.updateTeam(any(Long.TYPE), any(TeamUpdateRequestDto.class))).thenReturn(teamId);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.put("/api/team/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(1L));
        }

        @DisplayName("요청 데이터의 NotNull인 컬럼이 null이면 에러가 발생한다")
        @Test
        @WithMockUser(username = "1")
        public void updateTeam2() throws Exception {

            // given
            Long teamId = 1L;
            String request = "{\"name\": \"testTeam\", " +
                    "\"regionString\": \"서울시 영등포구\"}";
//                    "\"leaderId\": 2}";

            // mocking
            when(teamService.updateTeam(any(Long.TYPE), any(TeamUpdateRequestDto.class))).thenReturn(teamId);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.put("/api/team/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[팀장 아이디가 입력되지 않았습니다.]"));
        }
    }

    @Nested
    @DisplayName("DeleteTeamTest")
    public class DeleteTeamTest {

        @DisplayName("팀을 삭제한다")
        @Test
        @WithMockUser(username = "1")
        public void deleteTeam() throws Exception {

            // given
            Long deleteTeamId = 1L;

            // mocking
            when(teamService.deleteTeam(deleteTeamId)).thenReturn("팀이 삭제되었습니다.");

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/team/{teamId}", deleteTeamId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("팀이 삭제되었습니다."));
        }
    }

    @Nested
    @DisplayName("findAllTeamsTest")
    public class findAllTeamsTest {

        @DisplayName("모든 팀을 조회한다")
        @Test
        @WithMockUser(username = "1")
        public void findAllTeams() throws Exception {

            // given
            TeamResponseDto teamResponseDto1 = initTeamResponseDto("testTeam1");
            TeamResponseDto teamResponseDto2 = initTeamResponseDto("testTeam2");
            TeamResponseDto teamResponseDto3 = initTeamResponseDto("testTeam3");
            TeamResponseDto teamResponseDto4 = initTeamResponseDto("testTeam4");
            List<TeamResponseDto> teams = List.of(teamResponseDto1, teamResponseDto2, teamResponseDto3, teamResponseDto4);

            Pageable pageable = PageRequest.of(1, 2);
            Page<TeamResponseDto> teamsPage = convertToPage(teams, pageable);

            // mocking
            when(teamService.findAllTeams(pageable, "ALL")).thenReturn(teamsPage);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/team/teams?regionString=ALL&page={pageNum}&size={pageSize}", pageable.getPageNumber(), pageable.getPageSize()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].name").value("testTeam3"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[1].name").value("testTeam4"));

        }

        @DisplayName("지역 별로 팀을 조회한다")
        @Test
        @WithMockUser(username = "1")
        public void findAllTeams2() throws Exception {
            // given
            TeamResponseDto teamResponseDto1 = initTeamResponseDto("testTeam1");
            TeamResponseDto teamResponseDto2 = initTeamResponseDto("testTeam2");
            TeamResponseDto teamResponseDto3 = initTeamResponseDto("testTeam3");
            TeamResponseDto teamResponseDto4 = initTeamResponseDto("testTeam4");
            List<TeamResponseDto> teams = List.of(teamResponseDto1, teamResponseDto2, teamResponseDto3, teamResponseDto4);

            Pageable pageable = PageRequest.of(1, 2);
            Page<TeamResponseDto> teamsPage = convertToPage(teams, pageable);

            String regionString = "서울특별시 영등포구";

            // mocking
            when(teamService.findAllTeams(pageable, "서울특별시 영등포구")).thenReturn(teamsPage);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/team/teams?regionString={regionString}&page={pageNum}&size={pageSize}", regionString, pageable.getPageNumber(), pageable.getPageSize()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].name").value("testTeam3"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[1].name").value("testTeam4"));
        }
    }

    @Nested
    @DisplayName("findTeamTest")
    public class findTeamTest {

        @DisplayName("팀을 상세조회한다")
        @Test
        @WithMockUser(username = "1")
        public void findTeam() throws Exception {
            // given
            Long teamId = 1L;
            TeamResponseDto reponse = TeamResponseDto.builder()
                    .name("testTeam")
                    .regionString("서울시 영등포구")
                    .winning(10)
                    .leaderNickName("testLeader")
                    .build();

            // mocking
            when(teamService.findTeam(teamId)).thenReturn(reponse);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders.get("/api/team/{teamId}", teamId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("testTeam"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.regionString").value("서울시 영등포구"));
        }
    }
}
