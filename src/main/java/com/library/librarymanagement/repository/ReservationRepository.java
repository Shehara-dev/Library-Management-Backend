package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.Reservation;
import com.library.librarymanagement.entity.Reservation.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUserId(Integer userId);
    List<Reservation> findByBookId(Integer bookId);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByUserIdAndStatus(Integer userId, ReservationStatus status);

    boolean existsByBookIdAndStatus(Integer bookId, ReservationStatus status);
    @Modifying
    @Transactional
    void deleteByBookId(Integer bookId);
}