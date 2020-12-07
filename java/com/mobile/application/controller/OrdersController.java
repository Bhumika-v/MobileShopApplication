package com.mobile.application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import com.mobile.application.model.Cart;
import com.mobile.application.model.Item;
import com.mobile.application.model.Orders;
import com.mobile.application.model.Payment;
import com.mobile.application.model.User;
import com.mobile.application.repository.CartRepository;
import com.mobile.application.repository.ItemRepository;
import com.mobile.application.repository.OrdersRepository;
import com.mobile.application.repository.PaymentRepository;

@Controller
@RequestMapping("/User")

public class OrdersController {

	@Autowired
	private OrdersRepository orderRepository;
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@PostMapping(value = "/save-orders")
	public ModelAndView getDatas(Model models, @SessionAttribute("User") User users) {
		ModelAndView modelObject = new ModelAndView("orders2");
		try {
			List<Cart> cartValue = (List<Cart>) cartRepository.findAll();
			List<Cart> cart = new ArrayList<Cart>();
			String email = users.getEmail();
			for (var iterate : cartValue) {
				if (email != null)
					cart.add(iterate);
			}
			int sum = 0;
			System.out.println(cart);
			for (var iterate : cart) {
				Orders order = new Orders();
				order.setAddress("IN");
				order.setEmail(email);
				order.setItemname(iterate.getItemname());
				order.setModel(iterate.getModel());
				order.setQuantity(iterate.getQuantity());
				order.setTotal(iterate.getTotal());
				int total = iterate.getTotal();
				sum = sum + total;
				orderRepository.save(order);
				cartRepository.deleteById(iterate.getCartid());
				Item item = itemRepository.findById(iterate.getModel()).get();
				item.setQuantity_available(item.getQuantity_available() - iterate.getQuantity());
				itemRepository.save(item);
			}
			System.out.println("total " + sum);
			List<Orders> allorder = orderRepository.findAll();
			List<Orders> order = new ArrayList<Orders>();

			for (var iterate : allorder) {
				if (iterate.getEmail().equals(email))
					order.add(iterate);
			}
			modelObject.addObject("multiple", order);
			modelObject.addObject("total", sum);
		} catch (Exception exception) {
			System.out.println("Exception in Save Orders" + exception);
		}
		return modelObject;
	}

	@PostMapping("/removeO/{orderid}")
	public ModelAndView remove(@PathVariable("orderid") int orderid, @SessionAttribute("User") User user) {

		ModelAndView model = new ModelAndView("orders2");

		try {
			Orders value = orderRepository.getOne(Integer.valueOf(orderid));
			System.out.println("start remove");
			int id = value.getOrderid();
			// cartService.removefromcart(cartid);
			orderRepository.deleteById(id);
			System.out.println("Deleted");
			System.out.println(user.getEmail());

			System.out.println("The Email of session :" + user.getEmail());
			List<Orders> cart = orderRepository.findAll();
			System.out.println("fine  getcart");
			List<Orders> newCart = new ArrayList<>();
			for (var iterate : cart) {
				if ((iterate.getEmail().equals(user.getEmail())))
					newCart.add(iterate);
			}
			System.out.println(newCart);
			model.addObject("cart", newCart);
		} catch (Exception exception) {
			System.out.println("Exception in Remove Orders" + exception);
		}
		return model;
	}

	@RequestMapping("/allorder")
	public String home(Model model, @SessionAttribute("User") User users) {
		try {
			System.out.println(this.getClass().getSimpleName() + ":=======>Showing Orders list.");
			List<Orders> allorder = (List<Orders>) orderRepository.findAll();
			model.addAttribute("list", allorder);
		} catch (Exception exception) {
			System.out.println("Exception in All Orders" + exception);
		}
		return "allorder";
	}

	@PostMapping(value = "/getorder")
	public String getOrderList(Model model, @SessionAttribute("User") User users) {

		try {
			List<Payment> cart = (List<Payment>) paymentRepository.findAll();
			System.out.println("fine  Orders");
			String email = users.getEmail();
			List<Payment> newOrders = new ArrayList<>();
			for (var iterate : cart) {
				if ((iterate.getEmail().equals(email)))
					newOrders.add(iterate);
			}
			System.out.println(newOrders);
			model.addAttribute("list", newOrders);
		} catch (Exception exception) {
			System.out.println("Exception in Successfull orders" + exception);
		}
		return "allorder";
	}
}
