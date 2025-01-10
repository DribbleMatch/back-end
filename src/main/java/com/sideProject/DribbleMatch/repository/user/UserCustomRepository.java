package com.sideProject.DribbleMatch.repository.user;

import java.time.LocalDate;
import java.util.List;

public interface UserCustomRepository {

    public List<String> findAllEmailByUserInfo(LocalDate birth, String phone);
}
