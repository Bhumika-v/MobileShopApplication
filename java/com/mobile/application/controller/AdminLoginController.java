package com.mobile.application.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.mobile.application.model.User;
import com.mobile.application.repository.UserRepository;

@SessionAttributes("Admin")
@Controller
public class AdminLoginController {

	@ModelAttribute("Admin")
	public User setUp() {
		return new User();
	}

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("register1")
	public String register1() {
		return "register1";
	}

	@PostMapping(path = "/save-user1")
	public ModelAndView registerAdmin(User user) {
		User u = userRepository.save(user);
		ModelAndView m = new ModelAndView("adminlogin");
		m.addObject("l", u);
		return m;
	}

	@PostMapping(path = "/validateadmin")
	public ModelAndView adminLogin(@ModelAttribute("Admin") User user, BindingResult bindingResult,
			HttpServletRequest request) {
		List<User> l = (List<User>) userRepository.findAll();
		String ret = "admin";
		for (var l1 : l) {
			if (l1.getEmail().equals(user.getEmail())) {
				System.out.println(l1);

				if ((l1.getPassword().equals(user.getPassword()) && l1.getRolename().equals("Admin"))) {
					ret = "adminlogin";
				} else
					ret = "error1";
			}
		}
		ModelAndView mode = new ModelAndView(ret);
		mode.addObject("l", user);
		mode.addObject("message", "Enter Valid Credentials");
		mode.addObject("msg", "Admin");
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(900);
		return mode;
	}

}
