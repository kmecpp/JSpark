package com.kmecpp.jspark;

import java.util.Collection;
import java.util.HashMap;

import com.kmecpp.jspark.util.StringUtil;

public class Profiler extends Thread {

	private Thread mainThread;

	//	private HashMap<String, Integer> callCount = new HashMap<>();
	private TreeNode data = new TreeNode(null, "Root");
	private int samples;

	public Profiler() {
		setName("Profiler Thread");
		setDaemon(true);

		mainThread = Thread.currentThread();
	}

	@Override
	public void run() {
		while (true) {
			StackTraceElement[] stack = mainThread.getStackTrace();

			TreeNode parent = data;
			for (int i = stack.length - 1; i >= 0; i--) {
				String name = stack[i].toString();

				TreeNode child = parent.getChild(name);
				child.addCount();
				parent = child;
			}
			//			String call = stack[stack.length - 3].toString();
			//			callCount.put(call, callCount.getOrDefault(call, 0) + 1);
			samples++;
		}
		//			Collections.sort

		//			for (Entry<String, Integer> entry : ) {
		//				System.out.println(entry.getKey() + ": " + entry.getValue());
		//			}
	}

	public void displayResults() {
		System.out.println();
		System.out.println("Profiler Results:");
		System.out.println("Samples: " + samples);

		data.display(0);
		System.out.println(data.getSamples());
		//		callCount.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder())).forEach((entry) -> {
		//			System.out.println(entry.getValue() + ") " + entry.getKey());
		//		});
	}

	public static class TreeNode {

		private final TreeNode parent;
		private final HashMap<String, TreeNode> children = new HashMap<>();
		private final String name;
		private int samples;

		public TreeNode(TreeNode parent, String name) {
			this.name = name;
			this.parent = parent;

			if (parent != null) {
				parent.children.put(name, this);
			}
		}

		public TreeNode getParent() {
			return parent;
		}

		public Collection<TreeNode> getChildren() {
			return children.values();
		}

		public String getName() {
			return name;
		}

		public int getSamples() {
			return samples;
		}

		public void addCount() {
			if (parent != null) {
				parent.addCount();
			}
			samples++;
		}

		public TreeNode getChild(String name) {
			return children.getOrDefault(name, new TreeNode(this, name));
		}

		public void display() {
			display(0);
		}

		private void display(int level) {
			System.out.println(StringUtil.whitespace(level * 2) + samples + " " + name);
			for (TreeNode node : getChildren()) {
				node.display(level + 1);
			}
		}

		//		public TreeNode<T> addChild(T child) {
		//			TreeNode<T> childNode = new TreeNode<T>(child);
		//			childNode.parent = this;
		//			this.children.add(childNode);
		//			return childNode;
		//		}

	}

}
