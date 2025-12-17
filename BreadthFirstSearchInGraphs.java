package m204;

import java.util.ArrayDeque;
import java.util.ArrayList;
//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
import java.util.Arrays;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

幅優先探索の基本手続を確認しつつ、入力検証と探索打切り条件を明示化した。  
キューによる層状走査が期待通りの順序を生成することをテストで再確認した。  
隣接リストの取り扱いでは、無向条件と範囲外ノードの扱いに留意した。  
探索個数の上限を nodenum とし、到達済み管理を visited で一意に保つ設計とした。    
計算量は辺走査 O(E)、頂点処理 O(V) で総計 O(V+E) と評価される。  

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます



public class BreadthFirstSearchInGraphs{
	
	public static void main(String[] args){
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	メソッド search は、隣接リスト list_adjlist、開始頂点 initnode、
	出力上限 nodenum を入力とし、幅優先探索の訪問順序を ArrayList<Integer> として返す。
	まず入力の妥当性を検査し、空・範囲外・上限非正などの場合は空リストを返す。
	探索は boolean 配列 visited で到達管理を行い、ArrayDeque<Integer> を待ち行列として用いる。
	初期頂点を訪問済みにし、キューへ入れてからループを開始する。
	ループでは先頭を取り出して順序に記録し、所定個数 desired に達したら早期に打ち切る。
	取り出した頂点の隣接頂点列を取得し、未訪問かつ範囲内の頂点に到達印を付して順次キューへ追加する。
	これにより、開始頂点からの距離が小さい順に頂点が展開され、幅優先の層構造に従う順序が得られる。
	nodenum が n を上回る場合は n に丸め、order.size() が desired に達した時点で処理を終了する。

	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static ArrayList<Integer> search(ArrayList<ArrayList<Integer>> list_adjlist, int initnode, int nodenum) {
		
		//* 出力用リストの初期化と入力の空検査をまとめて行う
		ArrayList<Integer> order = new ArrayList<>();
		if (list_adjlist == null)	return order;
		
		
		//* 頂点数 n を取得し、空グラフの場合は即時終了とする
		int n = list_adjlist.size();
		if (n == 0)	return order;
		
		//* 初期頂点の範囲検査
		if (initnode < 0 || initnode >= n)	return order;

		//* 取得上限 desired を決定し、非正なら処理不要と判断する
		int desired = (nodenum <= n ? nodenum : n);
		if (desired <= 0)	return order;

		//* 到達管理と待ち行列の準備
		boolean[] visited = new boolean[n];
		ArrayDeque<Integer> queue = new ArrayDeque<>();

		//* 初期頂点を訪問済みに設定し、探索の起点としてキュー投入
		visited[initnode] = true;
		queue.add(initnode);

		//* 幅優先の主ループ
		while (!queue.isEmpty() && order.size() < desired) {
			
			//* 先頭頂点 u を取り出して訪問順に記録（取り出しは FIFO）
			int u = queue.poll();
			order.add(u);
			
			//* 要求個数に達した時点で早期終了し過剰走査を避ける
			if (order.size() == desired)	break;

			//* 現在頂点 u の隣接頂点列を取得
			ArrayList<Integer> neighbors = list_adjlist.get(u);
			
			//* 隣接頂点を走査し、未訪問かつ範囲内のみを次に展開する
			for (int v : neighbors) {
				//* 範囲検査と未訪問判定を併用し重複展開を防止
				if (v >= 0 && v < n && !visited[v]) {
					//* v を訪問済みにして待ち行列へ追加
					visited[v] = true;
					queue.add(v);
				}
			}
		}
		//* 幅優先で得た訪問順の結果を返す
		return order;
	}
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test(){
		//test1
		int nodenumtest1 = 6;
		int[][] ar_edgetest1 = {{0, 1}, {0, 2}, {0, 4}, {1, 3}, {2, 3}, {3, 5}};
		ArrayList<ArrayList<Integer>> list_adjlisttest1 = getAdjListFromEdges(ar_edgetest1, nodenumtest1, false);
		//System.out.println(list_adjlisttest1); 
		//test1-1
		ArrayList<Integer> list_test1ans1 = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 4, 3, 5));
		boolean test1res1 = false;
		ArrayList<Integer> list_test1res1 = search(list_adjlisttest1, 0, list_adjlisttest1.size());
		//System.out.println(list_test1res1 + " " + list_test1ans1);
		test1res1 = list_test1res1.equals(list_test1ans1);
		//test1-2
		ArrayList<Integer> list_test1ans2 = new ArrayList<Integer>(Arrays.asList(5, 3, 1, 2, 0, 4));
		boolean test1res2 = false;
		ArrayList<Integer> list_test1res2 = search(list_adjlisttest1, 5, list_adjlisttest1.size());
		//System.out.println(list_test1res2 + " " + list_test1ans2);
		test1res2 = list_test1res2.equals(list_test1ans2);
		System.out.println(test1res1 + " " + test1res2);
		
		//test2
		int nodenumtest2 = 9;
		int[][] ar_edgetest2 = {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 2}, {1, 6}, {2, 5}, {3, 5}, {4, 6}, {4, 7}, {4, 8}, {6, 7}};
		ArrayList<ArrayList<Integer>> list_adjlisttest2 = getAdjListFromEdges(ar_edgetest2, nodenumtest2, false);
		//System.out.println(list_adjlisttest2); //
		//test2-1
		ArrayList<Integer> list_test2ans1 = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 6, 5, 7, 8));
		boolean test2res1 = false;
		ArrayList<Integer> list_test2res1 = search(list_adjlisttest2, 0, list_adjlisttest2.size());
		//System.out.println(list_test2res1 + " " + list_test2ans1);
		test2res1 = list_test2res1.equals(list_test2ans1);
		//test2-2
		ArrayList<Integer> list_test2ans2 = new ArrayList<Integer>(Arrays.asList(8, 4, 0, 6, 7, 1, 2, 3, 5));
		boolean test2res2 = false;
		ArrayList<Integer> list_test2res2 = search(list_adjlisttest2, 8, list_adjlisttest2.size());
		//System.out.println(list_test2res2 + " " + list_test2ans2);
		test2res2 = list_test2res2.equals(list_test2ans2);
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

