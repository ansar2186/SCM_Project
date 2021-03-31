package com.smart.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Contact")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int contectId;
	private String firstName;
	private String lastName;
	private String profession;
	@Column(unique = true)
	private String email;
	private String phone;
	private String image;
	@Column(length = 500)
	private String descripton;
	private Date  contactCreatedDate;
	private Date  contactUpdatedDate;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	
	public int getContectId() {
		return contectId;
	}
	public void setContectId(int contectId) {
		this.contectId = contectId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescripton() {
		return descripton;
	}
	public void setDescripton(String descripton) {
		this.descripton = descripton;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getContactCreatedDate() {
		return contactCreatedDate;
	}
	public void setContactCreatedDate(Date contactCreatedDate) {
		this.contactCreatedDate = contactCreatedDate;
	}
	public Date getContactUpdatedDate() {
		return contactUpdatedDate;
	}
	public void setContactUpdatedDate(Date contactUpdatedDate) {
		this.contactUpdatedDate = contactUpdatedDate;
	}
	
	
	/*
	 * @Override public String toString() { return "Contact [contectId=" + contectId
	 * + ", firstName=" + firstName + ", lastName=" + lastName + ", profession=" +
	 * profession + ", email=" + email + ", phone=" + phone + ", profileImage=" +
	 * profileImage + ", descripton=" + descripton + ", user=" + user + "]"; }
	 */
	
	
}
