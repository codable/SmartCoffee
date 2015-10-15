package com.sc.bus.service.socket;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sc.bus.controller.LocationController;
import com.sc.bus.service.LocationService;
import com.sc.bus.service.MemoryService;
import com.sc.bus.service.OrderService;
import com.sc.model.Location;
import com.sc.model.Order;
import com.sc.util.Constants;
import com.sc.util.Constants.OrderUpdateStatus;

public class LocationReceiverHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
	
	private LocationService locationService;
	private OrderService orderService;
	private MemoryService memoryService;

	public LocationReceiverHandler(LocationService locationService,
			OrderService orderService,
			MemoryService memoryService) {
		this.locationService = locationService;
		this.orderService = orderService;
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
		receiveLocaltion(location);
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

	public void receiveLocaltion(Location location) {
		logger.info(location.toString());
		String locationId = location.getLocationId();
		String cardId = location.getCardId();
		List<Location> existLocations = locationService.findByCardId(cardId);

		int size = existLocations.size();
		if (size <= 0) {
			if (!locationId.equals(Constants.LocationDeleteFLag)) { // add
				locationService.add(location);
				memoryService.updateLocation(location, OrderUpdateStatus.UPDATE);
			}
		} else if (size == 1) {
			Location existLocation = existLocations.get(0);
			if (locationId.equals(Constants.LocationDeleteFLag)) { // delete
				List<Order> orders = orderService.findByCardId(cardId);
				// If location deleted, could be think as order has delivered.
				if (orders.size() > 0) {
					for (Order order : orders) {
						order.setFinish(true);
						orderService.update(order);
					}
				}

				locationService.delete(existLocation);
				memoryService.updateLocation(existLocation, OrderUpdateStatus.DELETE);
			} else { // update
				// Change seat or in the middle of the take away
				locationService.update(existLocation);
				memoryService.updateLocation(existLocation, OrderUpdateStatus.UPDATE);
			}
		} else {
			logger.warn("Abnormal status, One Card Id mapping to multi locations!");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// Close the connection when an exception is raised.
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}