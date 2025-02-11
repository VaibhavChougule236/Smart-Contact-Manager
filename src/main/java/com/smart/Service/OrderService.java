package com.smart.Service;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smart.entities.*;
import com.smart.Dao.OrderRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	// Value Annotation is used to take value from application.properties

	@Value("razorPay.key.id")
	private String razorpayId;

	@Value("razorPay.key.secret")
	private String razorpaysecret;

	private RazorpayClient razorpayClient;

	@PostConstruct
	public void init() throws RazorpayException {
		this.razorpayClient = new RazorpayClient(razorpayId, razorpaysecret);
	}

	public Orders createOrders(Orders order) throws RazorpayException {
		JSONObject json = new JSONObject();
		json.put("amount", order.getAmount() * 100);
		json.put("currency", "INR");
		json.put("reciept", order.getEmail());

		Order razorpayOrder = this.razorpayClient.orders.create(json);
		if (razorpayOrder != null) {
			order.setRazorpayOrderId(razorpayOrder.get("id"));
			order.setOrderStatus(razorpayOrder.get("status"));
		}
		return orderRepository.save(order);
	}
	
	public Orders updateStatus(Map<String, String> map) {
    	String razorpayId = map.get("razorpay_order_id");
    	Orders order = orderRepository.findByRazorpayOrderId(razorpayId);
    	order.setOrderStatus("PAYMENT DONE");
    	Orders orders = orderRepository.save(order);
    	return orders;
    }
}
