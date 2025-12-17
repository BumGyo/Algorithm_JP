package p207;

import java.util.ArrayList;
//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
import java.util.Arrays;
import java.util.Collections;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

有向非巡回グラフのトポロジカル順序を深さ優先探索の後順（postorder）反転で得る実装とした。  
巡回検出は onstack による再帰スタック検査で行い、検出時は空列を返す設計とした。  
隣接リストは疎グラフでの走査効率が高く、各辺・各頂点を高々一度ずつ処理できる。  

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます



public class TopologicalSort{
	
	public static void main(String[] args){
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	topologicalsort は有向グラフの隣接リスト list_adjlist を受け取り、トポロジカル順序を ArrayList<Integer> で返す。  
    各未訪問頂点から DFS を行い、再帰の帰りがけに頂点を post に追加し、全成分分を連結して最後に反転することで順序を得る。  
    再帰中は onstack により経路上の頂点を管理し、onstack に戻る返り辺を検出した場合は巡回と見なし空列を返す。  
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます	
	private static ArrayList<Integer> topologicalsort(ArrayList<ArrayList<Integer>> list_adjlist){
		
		//* 頂点数・到達管理配列・結果器の用意
		int n = list_adjlist.size();
	    ArrayList<Integer> A = new ArrayList<>();
	    boolean[] visited = new boolean[n];

	    //* 未訪問成分ごとに DFS を起動し、後順を連結
	    for (int v = 0; v < n; v++) {                 
	        if (!visited[v]) {
	            ArrayList<Integer> post = new ArrayList<>();
	            search(v, list_adjlist, visited, post);
	            //* 成分ごとの後順列を結果へ追加
	            A.addAll(post);
	        }
	    }
	    //* 後順連結列を反転してトポロジカル順序へ変換
	    Collections.reverse(A);
	    return A;	
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	search は頂点 u を始点として深さ優先探索を行い、帰りがけに u を post へ追加する補助手続である。
	visited[u] を真にしてから隣接頂点列 adj.get(u) を順に調べ、未訪問 w に対して再帰的に search を呼び出す。
	全ての隣接の探索を終えた後に post.add(u) を行うことで後順列を構成し、反転によってトポロジカル順序を得る前段を担う。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static void search(int u, ArrayList<ArrayList<Integer>> adj, boolean[] visited, ArrayList<Integer> post) {
		
		//* 訪問印とスタック登録
		visited[u] = true;
		
		//* 隣接頂点を順に走査
		ArrayList<Integer> nei = adj.get(u);
		for (int i = 0; i < nei.size(); i++) {
			int w = nei.get(i);
			//* 未訪問なら再帰下降
			if (!visited[w]) {
				search(w, adj, visited, post);
			}
		}
		//* 帰りがけに追加(後順)
		post.add(u);
	}
	
	
	
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test(){
		//test1
		int nodenumtest1 = 5;
		int[][] ar_edgetest1 = {{0, 1}, {0, 2}, {0, 4}, {1, 2}, {2, 3}};
		ArrayList<ArrayList<Integer>> list_adjlisttest1 = getAdjListFromEdges(ar_edgetest1, nodenumtest1, true);
		//System.out.println(list_adjlisttest1);// [[1, 2, 4], [2], [3], [], []]
		
		
		//test1-3
		ArrayList<Integer> list_nodetest1ans3 = new ArrayList<Integer>(Arrays.asList(0, 4, 1, 2, 3));
		boolean test1res3 = false;
		ArrayList<Integer> list_node3test1 = topologicalsort(list_adjlisttest1);
		//System.out.println(list_node3test1 + " " + list_nodetest1ans3);
		test1res3 = list_node3test1.equals(list_nodetest1ans3);
		System.out.println(test1res3);
		
		//test2
		int nodenumtest2 = 8;
		int[][] ar_edgetest2 = {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 2}, {1, 7}, {2, 5}, {3, 6}, {5, 6}, {6, 7}};
		ArrayList<ArrayList<Integer>> list_adjlisttest2 = getAdjListFromEdges(ar_edgetest2, nodenumtest2, true);
		//System.out.println(list_adjlisttest2);// [[1, 2, 3, 4], [2, 7], [5], [6], [], [6], [7], []]
		
		
		//test2-3
		ArrayList<Integer> list_nodetest2ans3 = new ArrayList<Integer>(Arrays.asList(0, 4, 3, 1, 2, 5, 6, 7));
		boolean test2res3 = false;
		ArrayList<Integer> list_node3test2 = topologicalsort(list_adjlisttest2);
		//System.out.println(list_node3test2 + " " + list_nodetest2ans3);
		test2res3 = list_node3test2.equals(list_nodetest2ans3);
		System.out.println(test2res3);
		
		
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

