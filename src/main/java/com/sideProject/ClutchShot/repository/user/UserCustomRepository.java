package com.sideProject.ClutchShot.repository.user;

import com.querydsl.core.Tuple;

import java.util.List;

public interface UserCustomRepository {

    public List<Tuple> findAllEmailByUserInfo(String name, String phone);
}
