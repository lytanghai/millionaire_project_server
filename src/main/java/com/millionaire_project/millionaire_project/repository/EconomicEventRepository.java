package com.millionaire_project.millionaire_project.repository;

import com.millionaire_project.millionaire_project.entity.EconomicEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EconomicEventRepository extends JpaRepository<EconomicEventEntity, Integer> {

    @Query(value = "SELECT * FROM event_economic_calendar " +
            "WHERE EXTRACT(MONTH FROM schedule) = :month " +
            "AND EXTRACT(YEAR FROM schedule) = :year", nativeQuery = true)
    List<EconomicEventEntity> findAllByMonthAndYearNative(@Param("month") int month,
                                                          @Param("year") int year);

}
