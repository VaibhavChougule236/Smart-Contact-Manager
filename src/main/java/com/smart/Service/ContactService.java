package com.smart.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.smart.Dao.contactRepository;
import com.smart.entities.Contact;

@Service
public class ContactService {
	@Autowired
	private contactRepository contactRepo;

	public Page<Contact> getContactsByUser(int id,Pageable pageable) {
		return contactRepo.findContactsByUserId(id,pageable);
	}

	public void deleteById(@PathVariable(value="id") Integer id) {
		this.contactRepo.deleteById(id);
		
	}

//	public Page<Contact> findPaginated(int pageNo, int pageSize, String sortField, String sortDir) {
//		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
//				: Sort.by(sortField).descending();
//
//		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
//		return this.contactRepo.findAll(pageable);
//
//	}
}
