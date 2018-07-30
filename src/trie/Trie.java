package trie;

import java.util.ArrayList;
public class Trie {
	private Trie() { }
	public static TrieNode buildTrie(String[] allWords) {
	
		TrieNode root = new TrieNode(null, null, null);
		TrieNode ptr = new TrieNode(null, null, null);
		ptr = root;
		
		if (allWords.length == 0) {
			return null;
		} 
		
		if (allWords.length == 1 && root.firstChild == null) {
			TrieNode single = new TrieNode(null, null, null);
			ptr.firstChild = single;
			single.substr = new Indexes(-1, (short) -1, (short) -1);
			single.substr.wordIndex = 0;
			single.substr.startIndex = 0;
			single.substr.endIndex = (short) (allWords[0].length()-1);
			return root;
		} 
		
		
		if (allWords.length > 1) {	
			
			
			TrieNode single = new TrieNode(null, null, null);
			ptr.firstChild = single;
			single.substr = new Indexes(-1, (short) -1, (short) -1);
			single.substr.wordIndex = 0;
			single.substr.startIndex = 0;
			single.substr.endIndex = (short) (allWords[0].length()-1);
			
			for (int i = 1; i < allWords.length; i++) {
			ptr = single;
			
			while (true) {
				
				TrieNode newword = new TrieNode(new Indexes (i, (short) 0, (short) (allWords[i].length()-1)), null, null);
				short gP = getPrefix(allWords, ptr.substr, newword.substr);
				newword.substr.startIndex = (short) (gP+1);
				short ptrsi = ptr.substr.startIndex;
				short ptrei = ptr.substr.endIndex;
				
					if (gP < ptrsi) { //IF THERE IS NO COMMON PREFIX
						
							if (ptr.sibling == null) {
								ptr.sibling = newword;
								break;
							}
							
							if (ptr.sibling != null) {
								ptr = ptr.sibling;
							}
							
							
							
					}
					if (gP >= ptrsi && gP < ptrei) { //IF THERE IS A PARTIAL MATCH/COMMON PREFIX
						ptr.substr.endIndex = gP;
						TrieNode newprefix = new TrieNode (new Indexes(ptr.substr.wordIndex,(short)(ptr.substr.endIndex+1), ptrei),ptr.firstChild,newword);
						ptr.firstChild = newprefix;
						newword.substr.startIndex = (short) (gP+1);
						break;
					}
					
					
					
					if (gP >= ptrei) { //IF THERE IS A COMPLETE MATCH/THE WORD FULLY CONTAINS PREFIX
						
						if (ptr.firstChild != null){
							ptr = ptr.firstChild;
						
						} else if (ptr.firstChild == null) {
							
							ptr.substr.endIndex = getPrefix(allWords, ptr.substr, newword.substr);
							
							TrieNode firstword = new TrieNode(new Indexes (ptr.substr.wordIndex, (short)(ptr.substr.endIndex+1),ptrei), null, null);
							
							ptr.firstChild = firstword;
							firstword.sibling = newword;
							newword.substr.startIndex = (short)(ptr.substr.endIndex+1);
							break;
						}
						
					}
				
			}       //END FOR LOOP
			
		}
			
		}
		return root;
			
	}
	
	private static short getPrefix(String[] wordlist, Indexes word1, Indexes word2) {
		int count = -1;
		
		String first = wordlist[word1.wordIndex];
		String second = wordlist[word2.wordIndex];	
		
		for (int j = 0; j < Math.min(first.length(), second.length()); j++) {
			
			if (first.charAt(j) == second.charAt(j)) {
				
				count++;
				
			} else {
				break;
			}
			
		}
		return (short) count;
	}
	
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	
	
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		
		ArrayList<TrieNode> list = new ArrayList<TrieNode>();
		
		if (allWords.length == 0) {   //if there are no words in the list 
			return null;
		}
		
		if (allWords.length == 1){  //if there is one word in the list, return that one word
			if (allWords[0].substring(0,prefix.length()).equals(prefix)){
				list.add(root.firstChild);
				return list;
			} else {
				return null;
			}
		}
		
		
		
		
		//everything above here is good

		
		if (allWords.length > 1) {
			
			TrieNode newroot = new TrieNode (null,null,null);
			
			TrieNode ptr = root.firstChild;
			
			
			while (true) {
				
				String compare = allWords[ptr.substr.wordIndex].substring(0, ptr.substr.endIndex+1); //contains everything in the node up until the endindex+1, not just startindex to endindex
				short nGP = newGetPrefix(prefix,compare); //returns highest common index between prefix and previous string
				
				
				if (nGP == prefix.length()-1) { //if the prefix is fully within the node
					newroot=ptr;
					break;
				}
				
				if (nGP >= 0 && nGP < prefix.length()-1) {
					
					
					if (nGP == compare.length()-1) {
						ptr = ptr.firstChild;
					} else {
						ptr = ptr.sibling;
					}
					
				}
				
				
				
				
				
				
								if (nGP < 0) { //NO MATCH
									
									if(ptr.sibling == null) {
										return null;
									}
									
									if(ptr.sibling!=null) {
										ptr=ptr.sibling;
									}
								}
								
								
								
					
				
			}

			getList(newroot, list, allWords, true);
			return list;
		}
		
		return null;
	}
	
	
	
	//INDICATE WHETHER OR NOT YOU'RE AT THE TARGET NODE FOR THAT CONTAINS THE PREFIX
	private static short newGetPrefix(String word1, String word2) {
		short count = -1;
		for (int j = 0; j < Math.min(word1.length(),word2.length()); j++) {
			if (word1.charAt(j) == word2.charAt(j)) {
				count++;
			}else {
				break;
			}
		}
		return count;
	}
	
	private static void getList(TrieNode newroot, ArrayList<TrieNode> list, String[] allWords, Boolean b){
		
		
		TrieNode ptr = newroot;
		
		if (b == true) {
			if (ptr.firstChild == null) {
				list.add(ptr);
			}
			if (ptr.firstChild != null) {
				getList(ptr.firstChild, list, allWords, false);
			}
		}
		
		if (b==false) {
				//BASE CASE
				if (ptr.firstChild == null) {
					list.add(ptr);
				}
				//END BASE CASE
				
				if (ptr.firstChild == null && ptr.sibling != null) {
					getList (ptr.sibling,list, allWords, false);
				}
				
				if (ptr.sibling == null && ptr.firstChild != null) {
					getList(ptr.firstChild, list, allWords, false);
				}
				if (ptr.sibling != null && ptr.firstChild != null) {
					getList(ptr.firstChild, list, allWords,false);
					getList(ptr.sibling, list, allWords,false);
				}
		}
				
	}
	
	
	
	
	
	
	
	
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
