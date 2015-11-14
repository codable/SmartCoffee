package com.sc.bus.service.socket;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sc.bus.service.MemoryService;
import com.sc.model.Location;

public class LocationReceiverHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = LoggerFactory.getLogger(LocationReceiver.class);
	
	private MemoryService memoryService;

	public LocationReceiverHandler(MemoryService memoryService) {
		this.memoryService = memoryService;
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		//todo, first line count, second line 20,20,20
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
		
		List<Location> locationList = parseMessage2(sb.toString());
		for(Location location: locationList) {
			this.memoryService.receiveLocaltion(location);
		}
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
	
	private List<Location> parseMessage2(String message) {
		logger.info(message);
		int count = message.length() / 20;
		List<Location> list = new ArrayList<Location>();
		for(int i = 0; i < count; i++) {
			int cardBegin = 12 * ( i+ 1);
			int cardEnd = 16 * ( i+ 1);
			int tableBegin = 16 * ( i+ 1);
			int tableEnd = 20 * ( i+ 1);
			String cardId = message.substring(cardBegin, cardEnd);
			cardId = Integer.valueOf(cardId).toString();
			String tableId = message.substring(tableBegin, tableEnd);
			Location location = new Location(tableId, cardId);
			list.add(location);
		}
		return list;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// Close the connection when an exception is raised.
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}