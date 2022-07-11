package com.raftProject.dao;

import java.io.IOException;
import java.util.List;


public interface StudentDAO {

	public List<String> getStudents(String path);

	public void saveCourse(String theCustomer, String path) throws IOException;

}
