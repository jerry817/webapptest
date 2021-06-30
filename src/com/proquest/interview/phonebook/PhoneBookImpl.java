package com.proquest.interview.phonebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.proquest.interview.util.DatabaseUtil;

public class PhoneBookImpl implements PhoneBook {
	private List<Person> people = new ArrayList<>();

	@Override
	public void addPerson(Person newPerson) {
		//TODO: write this method
		if(newPerson != null){
			people.add(newPerson);
		}
	}

	@Override
	public Person findPerson(String firstName, String lastName) {
		//TODO: write this method
		String name = null;
		if(firstName != null && !firstName.isEmpty()){
			name = firstName;
		}
		if(lastName != null && !lastName.isEmpty()){
			name = (name == null) ? lastName : name + " " + lastName;
		}
		for(Person p : people){
			if(p.getName().equals(name)){
				return p;
			}
		}
		return null;
	}

	public List<Person> getPeople() {
		return people;
	}

	public static void main(String[] args) {
		DatabaseUtil.initDB();  //You should not remove this line, it creates the in-memory database

		/* TODO: create person objects and put them in the PhoneBook and database
		 * John Smith, (248) 123-4567, 1234 Sand Hill Dr, Royal Oak, MI
		 * Cynthia Smith, (824) 128-8758, 875 Main St, Ann Arbor, MI
		*/
		Person john = new Person("John Smith", "(248) 123-4567", "Royal Oak, MI");
		Person cynthia = new Person("Cynthia Smith", "(824) 128-8758", "875 Main St, Ann Arbor, MI");
		PhoneBookImpl phoneBook = new PhoneBookImpl();
		phoneBook.addPerson(john);
		phoneBook.addPerson(cynthia);

		// TODO: print the phone book out to System.out
		for(Person p : phoneBook.getPeople()){
			System.out.println(p);
		}

		// TODO: find Cynthia Smith and print out just her entry
		System.out.println(phoneBook.findPerson("Cynthia", "Smith"));

		// TODO: insert the new person objects into the database
		try {
			Connection cn = DatabaseUtil.getConnection();
			for (Person person : phoneBook.getPeople()) {
				PreparedStatement stmt = cn.prepareStatement(
						"INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) values (?,?,?)");
				stmt.setString(1, person.getName());
				stmt.setString(2, person.getPhoneNumber());
				stmt.setString(3, person.getAddress());
				stmt.execute();
			}
			cn.commit();
			cn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
