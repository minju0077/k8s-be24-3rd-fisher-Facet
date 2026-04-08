package com.facet.api.ask;


import com.facet.api.ask.model.Ask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AskRepository extends JpaRepository<Ask,Long> {

    List<Ask> findByEmail(String userEmail);
}