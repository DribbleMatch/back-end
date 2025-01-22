package com.sideProject.DribbleMatch.repository.user;

import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.util.List;

public interface UserCustomRepository {

    public List<Tuple> findAllEmailByUserInfo(String name, String phone);
}
