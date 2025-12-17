package p208;

import java.util.ArrayList;
//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
import java.util.Arrays;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

経路の重み計算を重み付き隣接リスト上で実装し、与えられた頂点列に沿って枝重みを逐次合算する構成とした。  
経路長に比例した計算量で評価できるため、単一経路の評価用途では全体グラフを走査するアルゴリズムに比べて計算負荷が小さい。  
隣接リスト表現はダイクストラ法の実装と共通化でき、疎グラフに対してメモリ使用量と初期化コストが抑えられる。  

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます



public class WeightedPaths{
	
	public static void main(String[] args){
		
		//* 経路の頂点列 list_node の長さに比例した計算量で重みを求めることができ，
		//* またダイクストラ法のスライドと同じ3次元隣接リスト実装を流用できるため，
		//* 隣接行列よりも実装が簡潔でメモリ使用量も少ない隣接リストを用いた。
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	本メソッド weigh は、重み付き隣接リスト list_adjlist と頂点列 list_node を受け取り、列が表す経路の枝重み総和を整数で返す。  
    経路を構成する連続する頂点対 (u, v) ごとに、始点 u の隣接リストを走査して行き先 v に対応する枝を見つけ、その重みを累積和 sum に加算する。  
    list_node の長さが 0 または 1 の場合は枝を含まないため、総和 0 をそのまま返却する。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static int weigh(ArrayList<ArrayList<ArrayList<Integer>>> list_adjlist, ArrayList<Integer> list_node){
		
		//* 結果用変数の初期化・短経路に対する早期終了
		int sum = 0;
		if (list_node.size() <= 1) {
			return sum;
		}

		//* 連続する頂点対 (u, v) ごとに対応する枝重みを探索
		for (int i = 0; i < list_node.size() - 1; i++) {
			int u = list_node.get(i);
			int v = list_node.get(i + 1);

			//* 始点 u の重み付き隣接リストを取得
			ArrayList<ArrayList<Integer>> adj = list_adjlist.get(u);

			//* 各枝 (to, w) を走査し、行き先 v に一致する枝の重みを加算
			for (int j = 0; j < adj.size(); j++) {
				ArrayList<Integer> edge = adj.get(j);
				int to = edge.get(0);
				int w = edge.get(1);

				if (to == v) {
					sum += w;
					break;
				}
			}
		}
		
		//* 経路上の全ての枝重みの総和を返却
		return sum;
	}

	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	/*
	private static int weigh(ArrayList<ArrayList<Integer>> list_adjmatrix, ArrayList<Integer> list_node){
		
	}
	*/
	
	
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test() {
		//test1
		int nodenumtest1 = 5;
		int[][] ar_edgetest1 = {{0, 1, 5}, {0, 2, 1}, {0, 3, 2}, {3, 0, 9}, {1, 4, 8}, {2, 3, 7}, {4, 3, 3}};
		
		// tests for adjacent list
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlisttest1 = getAdjListFromWeightedEdges(ar_edgetest1, nodenumtest1, true);
		//System.out.println(list_adjlisttest1);
		//test1-1 
		ArrayList<Integer> list_node1test1 = new ArrayList<Integer>(Arrays.asList(0, 1, 4, 3));
		int test1ans1 = 16;
		boolean test1res1 = false;
		int weighttest1res1 = weigh(list_adjlisttest1, list_node1test1);
		//System.out.println(weighttest1res1);
		test1res1 = (weighttest1res1 == test1ans1);
		//test1-2
		ArrayList<Integer> list_node2test1 = new ArrayList<Integer>(Arrays.asList(0, 2, 3));
		int test1ans2 = 8;
		boolean test1res2 = false;
		int weighttest1res2 = weigh(list_adjlisttest1, list_node2test1);
		//System.out.println(weighttest1res2);
		test1res2 = (weighttest1res2 == test1ans2);
		System.out.println(test1res1 + " " + test1res2);
		
		
		/*
		// tests for adjacent matrix
		ArrayList<ArrayList<Integer>> list_adjmatrixtest1 = getAdjMatrixFromEdges(ar_edgetest1, nodenumtest1, true);
		System.out.println(list_adjmatrixtest1);
		//test1-1 
		ArrayList<Integer> list_node1test1 = new ArrayList<Integer>(Arrays.asList(0, 1, 4, 3));
		int test1ans1 = 16;
		boolean test1res1 = false;
		int weighttest1res1 = weigh(list_adjmatrixtest1, list_node1test1);
		//System.out.println(weighttest1res1);
		test1res1 = (weighttest1res1 == test1ans1);
		//test1-2
		ArrayList<Integer> list_node2test1 = new ArrayList<Integer>(Arrays.asList(0, 2, 3));
		int test1ans2 = 8;
		boolean test1res2 = false;
		int weighttest1res2 = weigh(list_adjmatrixtest1, list_node2test1);
		//System.out.println(weighttest1res2);
		test1res2 = (weighttest1res2 == test1ans2);
		System.out.println(test1res1 + " " + test1res2);
		*/
		
	}
	
	
	// 枝の集合から隣接行列を取得するメソッド
	private static ArrayList<ArrayList<Integer>> getAdjMatrixFromEdges(int[][] ar_edge, int nodenum, boolean directed){
		// 隣接行列を初期化（全てを0にする）
		ArrayList<ArrayList<Integer>> list_adjmatrix1 = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		for(int a1 = 0; a1 < nodenum; a1++){
			list1 = new ArrayList<Integer>();
			for(int a2 = 0; a2 < nodenum; a2++)
				list1.add(0);
			list_adjmatrix1.add(list1);
		}
		// 枝を追加（枝の値のみ1にする）
		for(int a1 = 0; a1 < ar_edge.length; a1++){
			// 枝の両端点
			int node1 = ar_edge[a1][0]; int node2 = ar_edge[a1][1];
			// 重み
			int weight1 = ar_edge[a1][2];
			// node1行node2列に重みを設定る
			list_adjmatrix1.get(node1).set(node2, weight1);
			// 無向グラフの場合、逆向きの枝を加える
			if(directed == false){
				list_adjmatrix1.get(node2).set(node1, weight1);
			}
		}
		//隣接行列を返す
		return list_adjmatrix1;
	}
	
	// 枝の集合から重み付き隣接リストを取得するメソッド
	private static ArrayList<ArrayList<ArrayList<Integer>>> getAdjListFromWeightedEdges(int[][] ar_edge, int nodenum, boolean directed){
		// 隣接リストを初期化（全てを空のリストにする）
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlist1 = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<ArrayList<Integer>> list_adj1;
		ArrayList<Integer> list1;
		for(int a1 = 0; a1 < nodenum; a1++){
			list_adj1 = new ArrayList<ArrayList<Integer>>();
			list_adjlist1.add(list_adj1);
		}
		// 枝を追加
		ArrayList<Integer> list_edge1;//重み付き枝
		for(int a1 = 0; a1 < ar_edge.length; a1++){
			// 枝の両端点
			int node1 = ar_edge[a1][0]; int node2 = ar_edge[a1][1];
			// 枝の重み
			int weight1 = ar_edge[a1][2];
			// 枝作成して隣接リストに加える
			list_edge1 = new ArrayList<Integer>(Arrays.asList(node2, weight1));
			list_adjlist1.get(node1).add(list_edge1);
			// 無向グラフの場合、逆向きの枝を加える
			if(directed == false){
				list_edge1 = new ArrayList<Integer>(Arrays.asList(node1, weight1));
				list_adjlist1.get(node2).add(list_edge1);
			}
		}
		// 隣接リストを返す
		return list_adjlist1;
	}
}

