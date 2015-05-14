package com.demo.springmvc.controller;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.springmvc.repository.UserRepository;
import com.zhaopin.rabbitMQ.producer.ProducerMq;

@Controller
public class HelloWorldController {
	@Autowired
	private UserRepository userRepository;

	@Value("#{config['jdbc.driverClass']}")
	private String driverClass;

	@Autowired
	private ProducerMq producer;

	private ZooKeeper zooKeeper;
	private CountDownLatch connectedSignal = new CountDownLatch(1);

	@RequestMapping("/hello")
	public String hello(
			@RequestParam(value = "corpId", required = false, defaultValue = "79941768") String corpId,
			Model model) throws IOException, KeeperException,
			InterruptedException {
		String corpName = userRepository.getUserStatusByCorpId(corpId);
		zooKeeper = new ZooKeeper("172.17.0.119:2181", 5000, new ConnWatcher());
		System.out.println(zooKeeper.exists("/hrc/queuelist", true));
		System.out.println(zooKeeper
				.getData("/hrc/queuelist", true, new Stat()));
		System.out.println(zooKeeper.setData("hrc", "555".getBytes(), 2));
		model.addAttribute("name", driverClass + corpName);
		return "helloworld";
	}

	public ZooKeeper getZooKeeper() {
		return zooKeeper;
	}

	public void setZooKeeper(ZooKeeper zooKeeper) {
		this.zooKeeper = zooKeeper;
	}

	public class ConnWatcher implements Watcher {
		@Override
		public void process(WatchedEvent event) {
			// 连接建立, 回调process接口时, 其event.getState()为KeeperState.SyncConnected
			if (event.getState() == KeeperState.SyncConnected) {
				// 放开闸门, wait在connect方法上的线程将被唤醒
				connectedSignal.countDown();
			}
		}

	}
}
