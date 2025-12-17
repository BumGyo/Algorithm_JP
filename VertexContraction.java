package p202;

import java.util.ArrayList;
import java.util.HashSet;

//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
import java.util.Arrays;
import java.util.Collections;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

複数頂点の縮約操作を隣接リスト表現に基づいて実装し、縮約後の辺集合を正確に再構成する手順を検討した。
縮約は、集合Sに属する頂点群を代表頂点rに統合し、S内の自己辺や多重辺を排除しつつS外への隣接関係を保存する点に重点が置かれる。
特に、縮約対象頂点からの隣接を一時集合に集約し、重複と自己参照を除外したうえでrの近傍として正規化する工程は、可換ではない更新順序の影響を回避するために重要である。
検証では、等価性比較のための近傍並べ替えを導入し、構築結果の安定性を担保した。
結果として、縮約操作の不変条件（S∩N(r)=∅、r∉N(r)、N(u)からS\\{r}の消去、rへの一本化）を満たす実装原理を整理できた。

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます


public class VertexContraction{
	
	public static void main(String[] args){
		
		//* 縮約対象集合(list_node)の各頂点の隣接要素を合流・重複排除・自己ループ除去する処理は行サイズに比例して走査でき、
		//* HashSet併用で重複排除がO(1)平均になるため、全体でO(nk)に収まりやすい
		//* 逆に隣接行列は行・列全体を都度走査する必要がありO(n^2)になりやすい
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
	}
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	このメソッドは、与えられた無向グラフの隣接リストlist_adjlistと、縮約対象頂点集合list_nodeを受け取り、集合内の頂点を代表頂点rに統合したグラフを返す。
	(1)入力隣接リストを深い複製でresに展開し、
	(2)S=list_nodeを集合化して代表rを決定、
	(3)S外の隣接をr用の一時集合に収集し、
	(4)S内各頂点uの削除処理として、全隣接vからuへの参照を除去しuの近傍を空にする、
	(5)rの近傍を収集済み一時集合で置換、
	(6)各頂点v側からS\\{r}への参照を除去し、rへの単一参照を保証、
	(7)rの自己ループを除去、という順序で更新を施す。出力は縮約後の隣接リストであり、S内の自己辺と多重辺の除去、およびS外の隣接の統合が達成される。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static ArrayList<ArrayList<Integer>> contract(ArrayList<ArrayList<Integer>> list_adjlist, int nodenum, ArrayList<Integer> list_node) {

		//* 入力の隣接リストを破壊しないよう、結果用の深い複製を構築する
		ArrayList<ArrayList<Integer>> res = new ArrayList<>();
		for (int i = 0; i < list_adjlist.size(); i++) {
			res.add(new ArrayList<>(list_adjlist.get(i)));
		}

		//* 縮約対象集合が空の場合はコピーを返して終了する
		if (list_node == null || list_node.isEmpty())
			return res;

		//* 代表頂点rを集合の先頭要素とし、縮約集合Sとr用の新近傍集合を作成する
		int r = list_node.get(0);
		HashSet<Integer> S = new HashSet<>(list_node);
		HashSet<Integer> newNeighborsR = new HashSet<>();

		//* まずr自身が既に持つS外の隣接を収集する
		for (int v : res.get(r)) {
			if (v != r && !S.contains(v))
				newNeighborsR.add(v);
		}

		//* 次にS内の各頂点uから、S外かつr以外の隣接を収集しrの候補近傍に統合する
		for (int u : list_node) {
			if (u == r)
				continue;
			for (int v : res.get(u)) {
				if (v == u)
					continue;
				if (S.contains(v))
					continue;
				if (v == r)
					continue;
				newNeighborsR.add(v);
			}
		}

		//* S内の各頂点uを消去する準備として、全頂点v側の隣接からuを除去し、uの隣接リストを空にする
		for (int u : list_node) {
			if (u == r)
				continue;
			for (int v : new ArrayList<>(res.get(u))) {
				res.get(v).removeIf(x -> x == u);
			}
			res.get(u).clear();
		}

		//* rの近傍を一旦空にし、集約済みの候補集合で置換する
		res.get(r).clear();
		res.get(r).addAll(newNeighborsR);

		//* 各頂点v側からS\\{r}への参照を削除し、rへの参照が存在しない場合のみ追加して多重辺を防止する
		for (int v : newNeighborsR) {
			res.get(v).removeIf(x -> S.contains(x) && x != r);
			boolean hasR = false;
			for (int x : res.get(v)) {
				if (x == r) {
					hasR = true;
					break;
				}
			}
			if (!hasR)
				res.get(v).add(r);
		}

		//* rの自己ループを最終的に除去して縮約不変条件を満たす
		res.get(r).removeIf(x -> x == r);

		//* 縮約済みの隣接リストを返す
		return res;
	}

	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test(){
		//test1
		int nodenumtest1 = 8;
		int[][] ar_edgetest1 = {{0, 4}, {7, 6}, {7, 4}, {5, 3}, {2, 4}, {1, 2}};
		ArrayList<Integer> list_nodetest1 = new ArrayList<Integer>(Arrays.asList(2, 7, 1));
		int[][] ar_edgetest1ans1 = {{0, 4}, {2, 6}, {5, 3}, {2, 4}};
		
		
		// tests for adjacency list
		ArrayList<ArrayList<Integer>> list_adjlisttest1 = getAdjListFromEdges(ar_edgetest1, nodenumtest1, false);
		System.out.println(list_adjlisttest1);
		boolean test1res1 = false;
		ArrayList<ArrayList<Integer>> list_adjlisttest1res1 = contract(list_adjlisttest1, nodenumtest1, list_nodetest1);
		//System.out.println(list_adjlisttest1res1);
		for(int node1 = 0; node1 < list_adjlisttest1res1.size(); node1++){
			Collections.sort(list_adjlisttest1res1.get(node1));
		}
		ArrayList<ArrayList<Integer>> list_adjlisttest1ans1 = getAdjListFromEdges(ar_edgetest1ans1, nodenumtest1, false);
		for(int node1 = 0; node1 < nodenumtest1; node1++){
			Collections.sort(list_adjlisttest1ans1.get(node1));
		}
		//System.out.println(list_adjlisttest1ans1);
		test1res1 = list_adjlisttest1res1.equals(list_adjlisttest1ans1);
		System.out.println(test1res1);
		
		
		/*
		// tests for adjacency matrix
		ArrayList<ArrayList<Integer>> list_adjmatrixtest1 = getAdjMatrixFromEdges(ar_edgetest1, nodenumtest1, false);
		//System.out.println(list_adjmatrixtest1);
		boolean test1res1 = false;
		ArrayList<ArrayList<Integer>> list_adjmatrixtest1res1 = contract(list_adjmatrixtest1, nodenumtest1, list_nodetest1);
		//System.out.println(list_adjmatrixtest1res1);
		ArrayList<ArrayList<Integer>> list_adjmatrixtest1ans1 = getAdjMatrixFromEdges(ar_edgetest1ans1, nodenumtest1, false);
		//System.out.println(list_adjmatrixtest1ans1);
		test1res1 = list_adjmatrixtest1res1.equals(list_adjmatrixtest1ans1);
		System.out.println(test1res1);
		*/
		
	}
	
	
	
	// 枝の集合から隣接行列を取得するメソッド
	private static ArrayList<ArrayList<Integer>> getAdjMatrixFromEdges(int[][] ar_edge, int nodenum, boolean directed){
		//隣接行列を初期化（全てを0にする）
		ArrayList<ArrayList<Integer>> list_adjmatrix1 = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		for(int a1 = 0; a1 < nodenum; a1++){
			list1 = new ArrayList<Integer>();
			for(int a2 = 0; a2 < nodenum; a2++)
				list1.add(0);
			list_adjmatrix1.add(list1);
		}
		//枝を追加（枝の値のみ1にする）
		for(int a1 = 0; a1 < ar_edge.length; a1++){
			//枝の両端点
			int node1 = ar_edge[a1][0]; int node2 = ar_edge[a1][1];
			list_adjmatrix1.get(node1).set(node2, 1);
			// 無向グラフの場合、逆向きの枝を加える
			if(directed == false){
				list_adjmatrix1.get(node2).set(node1, 1);
			}
		}
		//隣接行列を返す
		return list_adjmatrix1;
	}
	
	// 枝の集合から隣接リストを取得するメソッド
	private static ArrayList<ArrayList<Integer>> getAdjListFromEdges(int[][] ar_edge, int nodenum, boolean directed){
		//隣接リストを初期化（全てを空のリストにする）
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
