package com.raftProject.service;

import java.io.IOException;
import java.util.List;


public interface StudentService {

	public List<String> getStudents(String path);

	public void saveCourse(String course, String path) throws IOException;

}
