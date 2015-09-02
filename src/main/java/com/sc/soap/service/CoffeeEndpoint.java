package com.sc.soap.service;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.tempurl.Postesalescreate;
import org.tempurl.PostesalescreateResponse;
import org.tempurl.PostesalescreateResult;
import org.tempurl.ResponseHeader;

@Endpoint
public class CoffeeEndpoint {
	private static final String NAMESPACE_URI = "http://tempurl.org";
 
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "postesalescreate")
	@ResponsePayload
	public PostesalescreateResponse getPostSales(@RequestPayload Postesalescreate request) {
		System.out.println(request.getAstrRequest().getHeader().getMessagetype());
		PostesalescreateResponse response = new PostesalescreateResponse();
		
		ResponseHeader header = new ResponseHeader();
		header.setMessageid("xxxxx");
		PostesalescreateResult post = new PostesalescreateResult();
		post.setHeader(header);
		response.setPostesalescreateResult(post);

		return response;
	}
}
