package com.demo.springmvc.common;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperOperator extends AbstractZooKeeper {
	public void create(String path, byte[] data) throws KeeperException,
			InterruptedException {
		/**
		 * 此处采用的是CreateMode是PERSISTENT 表示The znode will not be automatically
		 * deleted upon client's disconnect. EPHEMERAL 表示The znode will be
		 * deleted upon the client's disconnect.
		 */
		this.zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
	}

	public void getChild(String path) throws KeeperException,
			InterruptedException {
		try {
			List<String> list = this.zooKeeper.getChildren(path, false);
			if (list.isEmpty()) {
				System.out.println(path + "中没有节点");
			} else {
				System.out.println(path + "中存在节点");
				for (String child : list) {
					System.out.println("节点为：" + child);
				}
			}
		} catch (KeeperException.NoNodeException e) {
			throw e;
		}
	}

	public byte[] getData(String path) throws KeeperException,
			InterruptedException {
		return this.zooKeeper.getData(path, false, null);
	}

	public static void main(String[] args) {
		try {
			ZooKeeperOperator zkoperator = new ZooKeeperOperator();
			zkoperator.connect("172.17.0.119");

			String zktest = "abc123";
			Stat stat = zkoperator.zooKeeper.exists("/hrc/queuelist", false);
			zkoperator.zooKeeper.setData("/hrc/queuelist", zktest.getBytes(),
					stat.getVersion());
			System.out.println("获取设置的信息："
					+ new String(zkoperator.getData("/hrc/queuelist")));

			System.out.println("节点孩子信息:");
			zkoperator.getChild("/hrc");

			zkoperator.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
