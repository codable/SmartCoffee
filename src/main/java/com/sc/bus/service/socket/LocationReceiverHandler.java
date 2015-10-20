package com.sc.bus.service.socket;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sc.bus.controller.LocationController;
import com.sc.bus.service.MemoryService;
import com.sc.model.Location;

public class LocationReceiverHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
	
	private MemoryService memoryService;

	public LocationReceiverHandler(MemoryService memoryService) {
		this.memoryService = memoryService;
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		int size = buffer.readableBytes();
		/*if (size != 20) {
			e.getChannel().close();
			return;
		}*/
		
		StringBuffer sb = new StringBuffer();
		while (buffer.readable()) {
			byte data = buffer.readByte();

			Charset cs = Charset.forName("UTF-8");
			ByteBuffer bb = ByteBuffer.allocate(1);
			bb.put(data);
			bb.flip();
			CharBuffer cb = cs.decode(bb);
			sb.append(cb.toString());
		}
		
		e.getChannel().close();
		
		Location location = parseMessage(sb.toString());
		this.memoryService.receiveLocaltion(location);
	}
	
	private Location parseMessage(String message) {
		//int macBegin = 0;
		//int macEnd = 11;
		logger.info(message);
		int cardBegin = 12;
		int cardEnd = 16;
		int tableBegin = 16;
		int tableEnd = 20;
		String cardId = message.substring(cardBegin, cardEnd);
		cardId = Integer.valueOf(cardId).toString();
		String tableId = message.substring(tableBegin, tableEnd);
		return new Location(tableId, cardId);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// Close the connection when an exception is raised.
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}