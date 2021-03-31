package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.helper.Message;
import com.smart.model.Contact;
import com.smart.model.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String UserName = principal.getName();
		logger.debug("User Name------------" + UserName);
		logger.info("User Name------------" + UserName);
		logger.info("UserName------------" + UserName);
		System.out.println("User Name-----------" + UserName);
		User userByUserName = userRepo.getUserByUserName(UserName);
		logger.debug("User Object Found--------------" + userByUserName);
		System.out.println("User Object-------------" + userByUserName);

		Date date = new Date();

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		logger.info("-------------Login DateAndTime------------ " +formatter.format(date));

		model.addAttribute("dateTime", formatter.format(date));

		model.addAttribute("user", userByUserName);
	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		/*
		 * String UserName = principal.getName(); logger.debug("User Name------------"
		 * +UserName); logger.info("User Name------------" +UserName);
		 * logger.info("UserName------------" +UserName);
		 * System.out.println("User Name-----------" +UserName); User userByUserName =
		 * userRepo.getUserByUserName(UserName);
		 * logger.debug("User Object Found--------------" +userByUserName);
		 * System.out.println("User Object-------------"+userByUserName);
		 * 
		 * model.addAttribute("user", userByUserName);
		 */

		model.addAttribute("title", "Home-Page");
		System.out.println("---Inside Dashboard menthod---");

		return "user/user-dashboard";
	}

	@GetMapping("/addContact")
	public String viewAddContactPage(Model model) {

		model.addAttribute("title", "Add-Conatct-Page");
		model.addAttribute("contact", new Contact());
		System.out.println("-----------Inside Add COntact Method-------------");

		return "user/add-contact";

	}

	@PostMapping("/saveContact")
	public String saveContact(@ModelAttribute Contact userContact, Model model, Principal principal,
			HttpSession session, @RequestParam("profileImage") MultipartFile file) {

		System.out.println("------------Inside saveContact method-----------" + userContact.getLastName());

		try {

			String name = principal.getName();
			User user = userRepo.getUserByUserName(name);
			System.out.println("-----------User ---------" + user);
			userContact.setUser(user);
			userContact.setContactCreatedDate(new Date());
			boolean add = user.getList().add(userContact);

			String realPath = session.getServletContext().getRealPath("static/image");

			System.out.println("------------Contextpath--------------" + realPath);
			if (!file.isEmpty()) {
				userContact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/image").getFile();
				System.out.println("----------Save file Path------------------" + saveFile);
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				System.out.println("--------path with image name--------------------" + path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} else {
				userContact.setImage("contact.png");
			}
			if (add) {

				User save = userRepo.save(user);
				session.setAttribute("message",
						new Message("User Contact hase been successfully Saved!", "alert-success"));
			} else {
				session.setAttribute("message", new Message("User Contact not saved!", "alert-danger"));
				model.addAttribute("contact", userContact);
			}

			// user.getsession.setAttribute("message", new Message("User Contact hase been
			// successfully!", "alert-success"));
			model.addAttribute("title", "Add-Conatct-Page");
			model.addAttribute("contact", new Contact());
			System.out.println("-----------Inside Add saveContact-------------" + userContact);

		} catch (Exception e) {

			logger.error("------------exception is cauth while saveing the contact--" + e.getMessage());

		}

		return "user/add-contact";

	}

	@GetMapping("/viewContact/{page}")
	public String viewContactPage(@PathVariable("page") Integer page, Model model, Principal principal) {

		System.out.println("-----------Inside view Contact Method-------------");
		logger.info("--------------Inside view Contact Method----------");

		String userName = principal.getName();

		User userByUserName = userRepo.getUserByUserName(userName);

		System.out.println("User-------------" + userByUserName);
		logger.info("--------------User INfo----------" + userByUserName);

		Pageable pageable = PageRequest.of(page, 7);
		Page<Contact> contactList = this.contactRepository.findAllContactByUserName(userByUserName.getId(), pageable);

		if (contactList.getSize() > 0) {
			logger.info("--------------Contact List Size----------" + contactList.getSize());
			System.out.println("Contact List Size----------" + contactList.getSize());

			model.addAttribute("totalPage", contactList.getTotalPages());
			model.addAttribute("currentPage", page);
			model.addAttribute("title", "view-Conatct-Page");
			model.addAttribute("contact", contactList);

		} else {

		}

		return "user/view-contact";

	}

	@GetMapping("/viewProfile")
	public String viewProfilePage(Model model) {

		model.addAttribute("title", "view-profile-Page");
		logger.info(" In-Side the profile method----------");
		//System.out.println("-----------Inside view Profile Method-------------");

		return "user/view-Profile";

	}

	@GetMapping("/userSetting")
	public String userSetting(Model model) {

		model.addAttribute("title", "setting-Page");
		model.addAttribute("contact", new Contact());
		System.out.println("-----------Inside User Setting Method-------------");

		return "user/user-setting-page";

	}

	@GetMapping("/view-single-contact/{contectId}")
	public String viewSingleUserContact(@PathVariable("contectId") Integer contectId, Model model,
			Principal principal) {

		System.out.println("Contact Id-------------" + contectId);

		Contact contact = contactRepository.getContactByContactID(contectId);

		String name = principal.getName();
		User user = userRepo.getUserByUserName(name);

		if (user.getId() == contact.getUser().getId()) {

			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getFirstName());
		}

		/*
		 * if(findById.isPresent()) {
		 * 
		 * model.addAttribute("contact", findById); model.addAttribute("title",
		 * "View-singleContac"); System.out.println("Contact---------"); }
		 */
		return "user/View-singleContact";
	}

	@GetMapping("/delete-contact/{contectId}")
	public String deleteContact(Model model, @PathVariable("contectId") Integer contectId, Principal principal,
			HttpSession session) {

		System.out.println("Delete Id--------------" + contectId);

		String name = principal.getName();
		User user = this.userRepo.getUserByUserName(name);

		Optional<Contact> findById = this.contactRepository.findById(contectId);
		Contact contact = findById.get();

		if (user.getId() == contact.getUser().getId()) {
			contact.setUser(null);
			this.contactRepository.delete(contact);
			session.setAttribute("message", new Message("Contact Successfully Deleted", "success"));

		}

		return "redirect:/user/viewContact/0";
	}

	// Open Update Form Handler
	@PostMapping("/update-contactPage/{contectId}")
	public String OpenUpdateContactPage(@PathVariable("contectId") Integer contectId, Model model) {

		System.out.println("---------OpenUpdateContactPage--------");
		Optional<Contact> contact = this.contactRepository.findById(contectId);
		Contact contact2 = contact.get();
		if (contact2 != null) {
			model.addAttribute("contact", contact2);

		} else {
			model.addAttribute("contact", new Contact());
		}

		return "user/update-contact";
	}

	// Save Update Form handler

	@PostMapping("/update-Contact")
	public String UpdateConatct(Model model, @ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file, HttpSession session,Principal principal) {
		System.out.println("--------------------------Inside UpdateConatct Method--------------");
		logger.info("Inside the UpdateConatct method");
		Contact oldContact = contactRepository.getContactByContactID(contact.getContectId());

		try {


			if(!file.isEmpty()) {

				//old file delete
				File deleteFile = new ClassPathResource("static/image").getFile();

				File file2= new File(deleteFile, oldContact.getImage());
				file2.delete();

				//save Updated file
				String realPath = session.getServletContext().getRealPath("static/image");

				File saveFile = new ClassPathResource("static/image").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());


			}else {

				contact.setImage(oldContact.getImage());

			}

			String name = principal.getName();
			User user = userRepo.getUserByUserName(name);
			contact.setUser(user);
			contact.setContactUpdatedDate(new Date());

			contactRepository.save(contact);

			session.setAttribute("message", new Message("Your Contact Successfully Saved", "alert-success"));



		} catch (Exception e) {

			e.printStackTrace();
		}

		System.out.println("Contact Name-------" +contact.getFirstName());
		System.out.println("Contact ID-------" +contact.getContectId());

		return "redirect:/user/view-single-contact/"+contact.getContectId();
	}

	//Change password handler
	@PostMapping("/change-password")
	public  String changePassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,Model model, HttpSession session,Principal principal) {
		logger.info("---------- Entring In-Side the ChangePassword Method----------");

		//model.addAttribute("message", "Password Successfull Updated !!");
		logger.info("--Getting old Password is----" +oldPassword +" And getting New Password is ---" +newPassword) ;
		try {
			String userName = principal.getName();
			User currentUser = this.userRepo.getUserByUserName(userName);

			if(bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {

				currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));

				this.userRepo.save(currentUser);

				session.setAttribute("message", new Message("Password Successfully Updated", "alert-success"));

			}else {
				session.setAttribute("message", new Message("Please Enter Correct Old Password", "alert-danger"));
			}
		} catch (Exception e) {
			e.printStackTrace();

			session.setAttribute("message", new Message("Password Not Updated Due to server Internal Error", "alert-danger"));
		}
		logger.info("---------- Ending In-Side the ChangePassword Method----------");

		return "redirect:/user/userSetting";

	}


	

}
