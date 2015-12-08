package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

public class CaseInsensitiveTrie {

	/**
	字典树的Java实现。实现了插入、查询以及深度优先遍历。 
    Trie tree's java implementation.(Insert,Search,DFS)
    
	Problem Description
	Ignatius最近遇到一个难题,老师交给他很多单词(只有小写字母组成,不会有重复的单词出现),现在老师要他统计出以某个字符串为前缀的单词数量(单词本身也是自己的前缀).

	Input
	输入数据的第一部分是一张单词表,每行一个单词,单词的长度不超过10,它们代表的是老师交给Ignatius统计的单词,一个空行代表单词表的结束.第二部分是一连串的提问,每行一个提问,每个提问都是一个字符串.
	注意:本题只有一组测试数据,处理到文件结束.

	Output
	对于每个提问,给出以该字符串为前缀的单词的数量.

	Sample Input
	banana
	band
	bee
	absolute
	acm

	ba
	b
	band
	abc
	 
	Sample Output
	2
	3
	1
	0
	*/
	private int SIZE = 10;// 26 letters.CaseInsensitive.
	private TrieNode root;

	CaseInsensitiveTrie() {
		root = new TrieNode();
	}

	private class TrieNode {
		private int num;// how many words go through this node.
		private TrieNode[] son;// TrieNode[26] in this case
		private boolean isEnd;// end of a string.
		private char val;// No this field mostly.
		// But I think it's easy to traverse when having a specific value in
		// each node.
		private boolean visited;// add this field for DFS

		TrieNode() {
			num = 1;//num=0 is wrong
			son = new TrieNode[SIZE];
			isEnd = false;
			visited = false;
		}
	}

	public void insert(String str) {
		if (str == null || str.length() == 0) {
			return;
		}
		TrieNode node = root;
		char[] letters=str.toCharArray();
		for (int i = 0, len = str.length(); i < len; i++) {
			int pos = letters[i] - '0';
			if (node.son[pos] == null) {
				node.son[pos] = new TrieNode();
				node.son[pos].val = letters[i];
			} else {
				node.son[pos].num++;//for 'countPrefix(String prefix)'
			}
			node = node.son[pos];
		}
		node.isEnd = true;
	}

	/**
	 * count how many words start with the specific prefix.
	 * since we count it in insertion,what we need to do is to return the 'num' of the last letter of prefix.
	 */
	public int countPrefix(String prefix){
		if(prefix==null||prefix.length()==0){
			return -1;
		}
		TrieNode node=root;
		char[] letters=prefix.toCharArray();
		for(int i=0,len=prefix.length();i<len;i++){
			int pos=letters[i]-'0';
			if(node.son[pos]==null){
				return 0;
			}else{
				node=node.son[pos];
			}
		}
		return node.num;
	}
	
	// search a word in the tree.Complete matching.
	public boolean has(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		TrieNode node = root;
		char[] letters=str.toCharArray();
		for (int i = 0, len = str.length(); i < len; i++) {
			int pos = letters[i] - '0';
			if (node.son[pos] != null) {
				node = node.son[pos];
			} else {
				return false;
			}
		}
		return node.isEnd;
	}

	// DFS.Use stack.Like a 26-nary tree.
	public void printAllWords() {
		TrieNode rootNode = root;
		if (root == null){
			return;
		}
		LinkedList<TrieNode> list = new LinkedList<TrieNode>();
		for (int i = 0; i < SIZE; i++) {
			TrieNode node = rootNode.son[i];
			if (node != null) {
				list.addLast(node);
				while (!list.isEmpty()) {
					TrieNode curNode = list.getLast();
					TrieNode firstChild = firstChildOf(curNode);
					while (firstChild != null) {
						list.addLast(firstChild);
						firstChild = firstChildOf(firstChild);// DFS.
					}
					TrieNode strEnd = list.getLast();
					if (strEnd.isEnd) {
						printLinkedList(list);
					}
					list.removeLast();
				}
			}
			list.clear();
		}
	}

	//print each node in preOrder.
	public void preTraverse(TrieNode node){
		if(node!=null){
			System.out.print(node.val+"-");
			for(TrieNode child:node.son){
				preTraverse(child);
			}
		}
		
	}
	// get the first unvisited child of a node.
	public TrieNode firstChildOf(TrieNode node) {
		if (node == null)
			return null;
		for (int i = 0; i < SIZE; i++) {
			TrieNode tmp = node.son[i];
			if (tmp != null && !tmp.visited) {
				tmp.visited = true;
				return tmp;
			}
		}
		return null;
	}

	public static void printLinkedList(LinkedList<TrieNode> list) {
		if (list == null || list.size() == 0){
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0, len = list.size(); i < len; i++) {
			sb.append(list.get(i).val);
		}
		System.out.println(sb.toString());
	}

	public TrieNode getRoot(){
		return this.root;
	}
	
	public static void main(String[] args) throws Exception{
        CaseInsensitiveTrie tree = new CaseInsensitiveTrie();
		
		File file = new File("/gd_black_mobile.txt");
		 
		int count=0;
		BufferedReader bw = new BufferedReader(new FileReader(file));
		String line = null;
		long start = System.currentTimeMillis();
		//因为不知道有几行数据，所以先存入list集合中
		while((line = bw.readLine()) != null){
			try {
				count++;
				//tree.insert(line);
			}catch(Exception e) {
				System.out.println("error:"+count);
			}
	    }
		bw.close();
		System.out.println("finished:"+(System.currentTimeMillis()-start));
		
		
   }	
}

