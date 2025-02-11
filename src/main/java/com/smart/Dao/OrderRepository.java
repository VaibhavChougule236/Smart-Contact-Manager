package com.smart.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entities.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer>{
	Orders findByRazorpayOrderId(String razorpayId);
}
