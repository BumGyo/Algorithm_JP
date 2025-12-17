package p206;

import java.util.ArrayList;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

有向グラフのサイクル検出を DFS と再帰スタック検査で実装し、返り辺の検出を onstack 配列で定式化した。  
入力検証は最小限に留め、未訪問成分ごとに探索を開始することで全域を網羅する構成とした。  
visited は到達済みの排他、onstack は現在探索中の経路管理に用い、両者の併用で誤検出を防止する。  

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます



public class CycleDetection{
	
	public static void main(String[] args){
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
	}
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます

	detect は隣接リスト list_adjlist を受け取り、有向グラフにサイクルが存在するかを真偽で返す。  
    各連結成分の未訪問頂点から深さ優先探索を開始し、再帰中に onstack 上の頂点へ戻る返り辺が見つかれば真とする。  
    到達管理は visited、探索中経路は onstack で表現する。いずれの成分でも検出に至らなければ偽を返す。  
    計算量は O(V+E)となる。  	
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static boolean detect(ArrayList<ArrayList<Integer>> list_adjlist){
		
		//* 頂点数 n の取得と到達管理配列の用意
		int n = list_adjlist.size();
	    boolean[] visited = new boolean[n];
	    boolean[] onstack = new boolean[n];
	    
	    //* 各連結成分の未訪問頂点から DFS を開始
	    for (int i = 0; i < n; i++) {
	        if (!visited[i]) {
	        	//* 返り辺（onstack）を検出したら即時に真を返す
	            if (search(list_adjlist, i, visited, onstack)) {
	                return true;
	            }
	        }
	    }
	    
	    //* 全探索で検出されなかったため偽
	    return false;
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	search は現在頂点 u を訪問し、未訪問隣接へ再帰下降する補助手続である。  
    入栈 onstack[u]=true の状態で探索し、未訪問 v は再帰、既訪問で onstack[v]=true なら返り辺として真を返す。  
    隣接の走査完了後に onstack[u]=false として復帰し、検出がなければ偽を返す。  
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static boolean search(ArrayList<ArrayList<Integer>> list_adjlist, int u, boolean[] visited, boolean[] onstack) {
		
		//* 訪問印と入栈処理（現在の経路に u を登録）
		visited[u] = true;
		onstack[u] = true;

		//* 隣接頂点 v を順に調べる
		for (int v : list_adjlist.get(u)) {
			//* 未訪問なら再帰下降し、検出結果を伝搬
			if (!visited[v]) {
				if (search(list_adjlist, v, visited, onstack)) {
					return true;
				}
			//* onstack[v] が真なら返り辺（サイクル）を検出
			} else if (onstack[v]) {
				return true;
			}
		}
		//* 探索完了に伴い u をスタックから外す
		onstack[u] = false;
		
		//* 本経路ではサイクル未検出
		return false;
	}

	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test() {
		//test1
		int nodenumtest1 = 5;
		int[][] ar_edgetest1 = {{0, 1}, {1, 2}, {2, 3}, {2, 4}, {4, 0}};
		ArrayList<ArrayList<Integer>> list_adjlisttest1 = getAdjListFromEdges(ar_edgetest1, nodenumtest1, true);
		//System.out.println(list_adjlisttest1); 
		//test1-1
		boolean test1ans1 = true;
		boolean test1res1 = false;
		test1res1 = (detect(list_adjlisttest1) == test1ans1);
		System.out.println(test1res1);
		
		//test2
		int nodenumtest2 = 5;
		int[][] ar_edgetest2 = {{0, 1}, {0, 2}, {2, 3}, {2, 4}};
		ArrayList<ArrayList<Integer>> list_adjlisttest2 = getAdjListFromEdges(ar_edgetest2, nodenumtest2, true);
		//System.out.println(list_adjlisttest2); 
		//test2-1
		boolean test2ans1 = false;
		boolean test2res1 = false;
		test2res1 = (detect(list_adjlisttest2) == test2ans1);
		System.out.println(test2res1);
		
		//test3
		int nodenumtest3 = 6;
		int[][] ar_edgetest3 = {{1, 2}, {2, 3}, {3, 1}, {1, 0}};
		ArrayList<ArrayList<Integer>> list_adjlisttest3 = getAdjListFromEdges(ar_edgetest3, nodenumtest3, true);
		//System.out.println(list_adjlisttest3); 
		//test3-1
		boolean test3ans1 = true;
		boolean test3res1 = false;
		test3res1 = (detect(list_adjlisttest3) == test3ans1);
		System.out.println(test3res1);
		
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
