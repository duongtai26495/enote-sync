package com.kai.mynote.repository;

import com.kai.mynote.entities.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {


    @Query(value = "SELECT m FROM Media m WHERE m.author.username =:username ORDER BY created_at DESC")
    Page<Media> getMediaByUsernameDESC(String username, Pageable pageable);

    @Query(value = "SELECT m FROM Media m WHERE m.name =:name")
    Media getMediaByName(String name);


}
