package com.test.videorental.repository;

import com.test.videorental.entity.Movie;
import com.test.videorental.enums.VideoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie findByNameAndId(String name, Long id);

    @Query("SELECT m.videoType FROM Movie m where m.id = :movieId")
    VideoType findVideoTypeById(@Param("movieId") Long movieId);

}
