package com.antoine.assignment.haud.repositories;

import com.antoine.assignment.haud.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findByUsername(String username);

    @Query(value = "SELECT * FROM customers WHERE MONTH(birthdate) = MONTH(NOW()) AND DAY(birthdate) = DAY(NOW())", nativeQuery = true)
    List<Customer> findcustomerHavingBirthday();

    @Query(value = "SELECT address FROM customers WHERE DATEDIFF(dd, NOW(), birthdate) = 7", nativeQuery = true)
    List<String> findCustomersEmailHavingBirthdayNextWeek();
}
