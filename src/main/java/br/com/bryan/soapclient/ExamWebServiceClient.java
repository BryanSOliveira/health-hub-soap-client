package br.com.bryan.soapclient;

import java.util.ArrayList;
import java.util.List;

import br.com.bryan.soapclient.handlers.ClientAuthenticationHandler;
import br.com.bryan.webservice.EntityNotFoundException_Exception;
import br.com.bryan.webservice.Exam;
import br.com.bryan.webservice.ExamWebService;
import br.com.bryan.webservice.ValidationException_Exception;
import br.com.bryan.webservice.impl.Soap;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.Handler;

public class ExamWebServiceClient {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		Soap service = new Soap();
        ExamWebService port = service.getExamWebServiceImplPort();

        BindingProvider bindingProvider = (BindingProvider) port;
        List<Handler> handlerChain = new ArrayList<>();
        handlerChain.add(new ClientAuthenticationHandler("user", "password"));
        bindingProvider.getBinding().setHandlerChain(handlerChain);
        
        // Create
        Exam newExam = new Exam();
        newExam.setName("New Exam");
        newExam.setActive(true);
        
        try {
			newExam = port.createExam(newExam);
			System.out.println("ID: " + newExam.getId() + "; Name: " + newExam.getName() + "; Active: " + newExam.isActive() +
								"; Detail1: " + newExam.getDetail1() + "; Detail2: " + newExam.getDetail2());
		} catch (ValidationException_Exception e) {
			System.out.println("Invalid create exam: " + e.getMessage());
		}

        // Read
        Exam exam = null;
		try {
			exam = port.getExam(newExam.getId());
		} catch (EntityNotFoundException_Exception e) {
			System.out.println(e.getMessage());
		}
        
		// Update
        if(exam != null) {
            System.out.println(exam.getName());
	        try {
	        	exam.setName("Other Exam");
				port.updateExam(exam);
				System.out.println("Updated successfully!");
			} catch (ValidationException_Exception | EntityNotFoundException_Exception e) {
				System.out.println("Invalid update exam: " + e.getMessage());
			}
        }
        
        // Delete
        if(exam != null) {
	        try {
				port.deleteExam(exam.getId());
				System.out.println("Deleted successfully!");
			} catch (ValidationException_Exception | EntityNotFoundException_Exception e) {
				System.out.println("Invalid delete exam: " + e.getMessage());
			}
        }
	}
}
