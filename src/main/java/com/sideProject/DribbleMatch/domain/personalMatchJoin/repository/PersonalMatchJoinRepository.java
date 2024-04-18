package com.sideProject.DribbleMatch.domain.personalMatchJoin.repository;

import com.sideProject.DribbleMatch.domain.personalMatchJoin.entity.PersonalMatchJoin;
import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalMatchJoinRepository extends JpaRepository<PersonalMatchJoin, Long> {
}
