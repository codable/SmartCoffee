package com.sc.bus.service.socket;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sc.bus.service.MemoryService;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

@Service
public class LocationReceiver {

	@Value("${locationReceiver.port}")
	private String port;

    @Autowired
    private MemoryService memoryService;
    
	@PostConstruct
    public void receive() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                return Channels.pipeline(new LocationReceiverHandler(memoryService));
            }
        });

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("receiveBufferSize", 1048576);
        bootstrap.setOption("sendBufferSize", 1048576);


        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(Integer.valueOf(port)));
        //bootstrap.bind(new InetSocketAddress(9222));
    }
	
	public static void main(String[] as) throws Exception {
		LocationReceiver s= new LocationReceiver();
		s.receive();
		
	}
    
    
}