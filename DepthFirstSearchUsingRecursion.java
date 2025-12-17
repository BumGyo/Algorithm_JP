package m206;

import java.util.ArrayList;
//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
import java.util.Arrays;
import java.util.HashSet;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

再帰による深さ優先探索を先行順（preorder）で実装し、訪問管理を HashSet で行って無限再帰を防止した。  
隣接リストは疎グラフに適し、反復訪問の抑止と併せて O(V+E) の計算量を実現した。  
再帰の終端条件は「訪問済みなら直帰」とし、到達記録 list_node に先行順で頂点を格納する設計とした。 
最初に再帰を用いて実装することを思いつくのが難しかった。

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます



public class DepthFirstSearchUsingRecursion{
	
	public static void main(String[] args)	{
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	本関数 preordersearch は、隣接リスト list_adjlist、始点 initnode、訪問順の出力 list_node、訪問済み集合 hset_searchednode を受け取る。  
    まず initnode が訪問済みなら即時に復帰し、未訪問なら訪問済みに登録して出力へ追加する。  
    次に隣接頂点を順に巡り、未訪問のみについて再帰的に preordersearch を呼び出して探索を深掘りする。  
    これにより先行順（親を先に、子を後に）の訪問列が得られる。計算量は頂点と枝の和に対して線形の O(V+E)となる。 
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static void preordersearch(ArrayList<ArrayList<Integer>> list_adjlist, int initnode, ArrayList<Integer> list_node, HashSet<Integer> hset_searchednode){
		
		//* 終端条件（訪問済みなら直帰）
	    if (hset_searchednode.contains(initnode)) return;

        //* 現在頂点を訪問済みに登録し、先行順として出力へ追加
	    hset_searchednode.add(initnode);
	    list_node.add(initnode);

	    //* 隣接頂点を順に辿り、未訪問のみ再帰で深掘り
	    for (Integer next : list_adjlist.get(initnode)) {
	    	//* 未訪問判定による枝の展開抑止
	        if (!hset_searchednode.contains(next)) {
	        	//* 再帰呼び出し（先行順での深さ優先）
	            preordersearch(list_adjlist, next, list_node, hset_searchednode);
	        }
	    }
	}
	
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test() {
		//test1
		int nodenumtest1 = 5;
		int[][] ar_edgetest1 = {{0, 1}, {0, 2}, {0, 4}, {1, 2}, {2, 3}};
		ArrayList<ArrayList<Integer>> list_adjlisttest1 = getAdjListFromEdges(ar_edgetest1, nodenumtest1, false);
		//System.out.println(list_adjlisttest1);// [[1, 2, 4], [0, 2], [0, 1, 3], [2], [0]]
		//test1-1
		ArrayList<Integer> list_nodetest1ans1 = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4));
		boolean test1res1 = false;
		ArrayList<Integer> list_node1test1 = new ArrayList<Integer>();
		HashSet<Integer> hset_searchednode1test1 = new HashSet<Integer>();
		preordersearch(list_adjlisttest1, 0, list_node1test1, hset_searchednode1test1);
		//System.out.println(list_node1test1 + " " + list_nodetest1ans1);
		test1res1 = list_node1test1.equals(list_nodetest1ans1);
		//test1-2
		ArrayList<Integer> list_nodetest1ans2 = new ArrayList<Integer>(Arrays.asList(3, 2, 0, 1, 4));
		boolean test1res2 = false;
		ArrayList<Integer> list_node2test1 = new ArrayList<Integer>();
		HashSet<Integer> hset_searchednode2test1 = new HashSet<Integer>();
		preordersearch(list_adjlisttest1, 3, list_node2test1, hset_searchednode2test1);
		//System.out.println(list_node2test1 + " " + list_nodetest1ans2);
		test1res2 = list_node2test1.equals(list_nodetest1ans2);
		System.out.println(test1res1 + " " + test1res2);
		
		//test2
		int nodenumtest2 = 9;
		int[][] ar_edgetest2 = {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 2}, {1, 6}, {2, 5}, {3, 5}, {4, 6}, {4, 7}, {4, 8}, {6, 7}};
		ArrayList<ArrayList<Integer>> list_adjlisttest2 = getAdjListFromEdges(ar_edgetest2, nodenumtest2, false);
		//System.out.println(list_adjlisttest2);//[[1, 2, 3, 4], [0, 2, 6], [0, 1, 5], [0, 5], [0, 6, 7, 8], [2, 3], [1, 4, 7], [4, 6], [4]]
		//test2-1
		ArrayList<Integer> list_nodetest2ans1 = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 5, 3, 6, 4, 7, 8));
		boolean test2res1 = false;
		ArrayList<Integer> list_node1test2 = new ArrayList<Integer>();
		HashSet<Integer> hset_searchednode1test2 = new HashSet<Integer>();
		preordersearch(list_adjlisttest2, 0, list_node1test2, hset_searchednode1test2);
		//System.out.println(list_node1test2 + " " + list_nodetest2ans1);
		test2res1 = list_node1test2.equals(list_nodetest2ans1);
		//test2-2
		ArrayList<Integer> list_nodetest2ans2 = new ArrayList<Integer>(Arrays.asList(8, 4, 0, 1, 2, 5, 3, 6, 7));
		boolean test2res2 = false;
		ArrayList<Integer> list_node2test2 = new ArrayList<Integer>();
		HashSet<Integer> hset_searchednode2test2 = new HashSet<Integer>();
		preordersearch(list_adjlisttest2, 8, list_node2test2, hset_searchednode2test2);
		//System.out.println(list_node2test2 + " " + list_nodetest2ans2);
		test2res2 = list_node2test2.equals(list_nodetest2ans2);
		System.out.println(test2res1 + " " + test2res2);
		
	}
	
	
	// 枝の集合から隣接リストを取得するメソッド
	private static ArrayList<ArrayList<Integer>> getAdjListFromEdges(int[][] ar_edge, int nodenum, boolean directed){
		//隣接リストを初期化（全てを0にする）
		ArrayList<ArrayList<Integer>> list_adjlist1 = new ArrayList<ArrayList<Integer>>();
		for(int a1 = 0; a1 < nodenum; a1++){
			list_adjlist1.add(new ArrayList<Integer>());
		}
		//枝を追加
		for(int a1 = 0; a1 < ar_edge.length; a1++){
			//枝の両端点
			int node1 = ar_edge[a1][0]; int node2 = ar_edge[a1][1];
			list_adjlist1.get(node1).add(node2);
			// 無向グラフの場合、逆向きの枝を加える
			if(directed == false){
				list_adjlist1.get(node2).add(node1);
			}
		}
		//隣接リストを返す
		return list_adjlist1;
	}
	
	
}

