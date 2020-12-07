package com.mobile.application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.mobile.application.model.Orders;
import com.mobile.application.model.Payment;
import com.mobile.application.model.User;
import com.mobile.application.repository.OrdersRepository;
import com.mobile.application.repository.PaymentRepository;

@Controller
@RequestMapping("/User")
public class PaymentController {

	@Autowired
	private OrdersRepository orderRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@PostMapping(value = "/pay")
	public ModelAndView getDatas(@SessionAttribute("User") User users, Model models) {
		ModelAndView modelObject = new ModelAndView("payinfo");
		try {
			List<Orders> orderValue = (List<Orders>) orderRepository.findAll();
			List<Orders> order = new ArrayList<Orders>();
			String email = users.getEmail();
			for (var iterate : orderValue) {
				if (iterate.getEmail().equals(email))
					order.add(iterate);
			}
			System.out.println(order);
			modelObject.addObject("multiple", order);
		} catch (Exception exception) {
			System.out.println("Exception in Multiple Orders" + exception);
		}
		return modelObject;
	}

	@PostMapping(path = "/saveTopay")
	public ModelAndView savePay1(Payment pay, @RequestParam("fullName") String fullName,
			@RequestParam("address") String address, @RequestParam("city") String city,
			@RequestParam("modeOfPayment") String modeOfPayment, @SessionAttribute("User") User users) {

		ModelAndView modelObject = new ModelAndView("successpage");
		try {
			List<Orders> orderValue = (List<Orders>) orderRepository.findAll();
			List<Orders> order = new ArrayList<Orders>();
			String email = users.getEmail();
			for (var iterate : orderValue) {
				if (iterate.getEmail().equals(email))
					order.add(iterate);
			}
			System.out.print(order);
			for (var iterate : order) {
				System.out.println("hiii");
				Payment payment = new Payment();
				payment.setAddress(address);
				payment.setCity(city);
				payment.setFullname(fullName);
				payment.setModeofpayment(modeOfPayment);
				payment.setEmail(email);
				payment.setItemname(iterate.getItemname());
				payment.setTotal(iterate.getTotal());
				// p.setModeofpayment("cash");
				System.out.println(payment);
				paymentRepository.save(payment);
				System.out.println("Done");

			}
			List<Payment> paid = (List<Payment>) paymentRepository.findAll();
			List<Payment> pays = new ArrayList<Payment>();

			for (var iterate : paid) {
				if (iterate.getEmail().equals(email))
					pays.add(iterate);
			}
			System.out.println(pays);
			System.out.println("******************************");
			List<Payment> paymentList = new ArrayList<Payment>();
			int size = order.size();// 2
			int n = pays.size();// 4
			int i = n - 1;
			while (size != 0) {

				paymentList.add(pays.get(i));
				size--;
				i--;

			}

			List<Orders> orderlist = new ArrayList<Orders>();

			for (var iterate : order) {
				Orders value = orderRepository.getOne(Integer.valueOf(iterate.getOrderid()));
				orderlist.add(value);
				value.setEmail("ORDERS");
				orderRepository.save(value);
			}

			modelObject.addObject("multiple", paymentList);
		} catch (Exception exception) {
			System.out.println("Exception in Saving Payment details" + exception);
		}
		return modelObject;
	}

}
