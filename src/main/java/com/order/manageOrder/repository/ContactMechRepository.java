package com.order.manageOrder.repository;

import com.order.manageOrder.entity.ContactMech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMechRepository extends JpaRepository<ContactMech, Long> {
}
