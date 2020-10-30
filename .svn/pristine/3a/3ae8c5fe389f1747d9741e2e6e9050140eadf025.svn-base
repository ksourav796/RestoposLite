package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Customer findAllByCustomerNameAndCustomerIdNotIn(String name,Long id);
    List<Customer> findAllByCustomerNameContainingAndStatus(String name,String status);
    List<Customer> findAllByCustomerNameContainingAndStatus(String name, String status, Pageable pageable);
    Customer findFirstByCustomerNameContainingAndStatus(String name, String status, Sort sort);
   List<Customer> findAllByCustomerName(String name);
   Customer findByCustomerName(String name);
  List<Customer>  findAllByStatus(String status);
    Customer findAllByCustomerId(Long id);
   Customer findFirstByStatus(String status,Sort sort);
  List< Customer> findAllByStatus(String status,Pageable pageable);
}
