package com.auth.domain.dao;

import com.auth.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamDao extends JpaRepository<Team, String> {
}
