package m205;

//ファイル操作用
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

無向グラフに対する最短距離の算出を幅優先探索で実装し、距離未確定を -1 で初期化する設計とした。  
始点からの層状展開により、初回到達時の距離が最短であることを利用した。  
空リストや範囲外始点を早期に排除し、不正入力での副作用を防止した。  
計算量は O(V+E)、追加空間は dist と待ち行列に比例して O(V) と評価される。  
データ読込は行単位の分解と番兵 -1 による終端判定で隣接リストを構築した。  

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます




public class ShortestDistanceInUnweightedGraphs{
	private static final String str_datafile = "src/m205/ex205_data_dblpgraph_adjlist.csv";
	private static final String str_datafile2 = "src/m205/ex205_data_dblpgraph_name.csv";
	
	public static void main(String[] args){
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
		
	}
	
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	search は隣接リスト list_adjlist と始点 initnode を受け取り、各頂点への最短距離配列 dist を返す。
	未到達は -1 とし、始点は 0 に設定する。
	待ち行列を用いて幅優先に層を展開し、隣接頂点の距離を 1 加算で更新する。
	各頂点は初回到達時に最短距離が確定するため、dist[u] == -1 のときのみ更新・キュー投入を行う。
	入力が null または空、始点が範囲外の場合は空配列や未到達配列を返す方針とする。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static int[] search(ArrayList<ArrayList<Integer>> list_adjlist, int initnode) {
		
		//* 入力検証
        if (list_adjlist == null || list_adjlist.isEmpty()) return new int[0];
		int n = list_adjlist.size();
		
		//* 距離配列を -1 で初期化し、待ち行列を準備
		int[] dist = new int[n];
		java.util.Arrays.fill(dist, -1);
		Queue<Integer> q = new LinkedList<>();
		
		//* 始点の距離を 0 に設定し探索を開始
		dist[initnode] = 0;
		q.add(initnode);

		//* 幅優先の主ループ（キューが空になるまで）
		while (!q.isEmpty()) {
			//* 先頭頂点 v を取り出し隣接を走査
			int v = q.poll();
			for (int u : list_adjlist.get(v)) {
				//* 未到達なら最短距離を確定しキューへ追加
				if (dist[u] == -1) {
					dist[u] = dist[v] + 1;
					q.add(u);
				}
			}
		}
		
		//* 各頂点への最短距離配列を返却
		return dist;
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
		int[] ar_test1ans1 = {0, 1, 1, 2, 1, 3};
		boolean test1res1 = false;
		int[] ar_test1res1 = search(list_adjlisttest1, 0);
		//System.out.println(Arrays.toString(ar_test1res1) + " " + Arrays.toString(ar_test1ans1));
		test1res1 = Arrays.equals(ar_test1res1, ar_test1ans1);
		//test1-2
		int[] ar_test1ans2 = {2, 1, 1, 0, 3, 1};
		boolean test1res2 = false;
		int[] ar_test1res2 = search(list_adjlisttest1, 3);
		//System.out.println(Arrays.toString(ar_test1res2) + " " + Arrays.toString(ar_test1ans2));
		test1res2 = Arrays.equals(ar_test1res2, ar_test1ans2);
		System.out.println(test1res1 + " " + test1res2);
		
		//test2
		int nodenumtest2 = 9;
		int[][] ar_edgetest2 = {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 2}, {1, 6}, {2, 5}, {3, 5}, {4, 6}, {4, 7}, {4, 8}, {6, 7}};
		ArrayList<ArrayList<Integer>> list_adjlisttest2 = getAdjListFromEdges(ar_edgetest2, nodenumtest2, false);
		//System.out.println(list_adjlisttest2);//[[1, 2, 3, 4], [0, 2, 6], [0, 1, 5], [0, 5], [0, 6, 7, 8], [2, 3], [1, 4, 7], [4, 6], [4]]
		
		//test2-1
		int[] ar_test2ans1 = {0, 1, 1, 1, 1, 2, 2, 2, 2};
		boolean test2res1 = false;
		int[] ar_test2res1 = search(list_adjlisttest2, 0);
		//System.out.println(Arrays.toString(ar_test2res1) + " " + Arrays.toString(ar_test2ans1));
		test2res1 = Arrays.equals(ar_test2res1, ar_test2ans1);
		//test2-2
		int[] ar_test2ans2 = {2, 3, 3, 3, 1, 4, 2, 2, 0};
		boolean test2res2 = false;
		int[] ar_test2res2 = search(list_adjlisttest2, 8);
		//System.out.println(Arrays.toString(ar_test2res2) + " " + Arrays.toString(ar_test2ans2));
		test2res2 = Arrays.equals(ar_test2res2, ar_test2ans2);
		System.out.println(test2res1 + " " + test2res2);
		
		
		//実データを用いたテスト
		test2();
		
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
	
	
	
	//実データを用いたテスト
	private static void test2(){
		System.out.println("DBLPから作成した無向グラフの実データを用いたテスト：");
		System.out.println("Shapleyからの最短距離を求める");
		//データ呼び出し
		ArrayList<ArrayList<Integer>> list_adjlist = getData();
		ArrayList<String> list_name = getData2();
		//
		int[] ar_res1 = search(list_adjlist, 291399);
		int[] ar_bignameid = {
			291399,
			275409,
			13067,
			102714,
			234636,
			60161,
			75861
		};
		int[] ar_ans1 = {
			0,
			4,
			4,
			4,
			6,
			5,
			5
		};
		String[] ar_memo = {
			"2012年のノーベル経済学賞の受賞者",
			"2012年のノーベル経済学賞の受賞者",
			"マイクロソフトの創業者。沈没した軍艦を引き上げたりしている人の方",
			"マイクロソフトの創業者",
			"ノイマン型計算機の考案者",
			"色々なところに顔を出す言語学者",
			"計算機科学者。ダイクストラ。オランダ人"
		};
		for(int a1 = 0; a1 < ar_bignameid.length; a1++){
			System.out.println(list_name.get(ar_bignameid[a1]) + " （" + ar_memo[a1] + "） " + ar_res1[ar_bignameid[a1]] + " " +(ar_res1[ar_bignameid[a1]]==ar_ans1[a1]));
		}
		
		
	}
	
	
	
	//csvファイルの実データ呼び出し
	private static ArrayList<ArrayList<Integer>> getData(){
		//
		ArrayList<ArrayList<Integer>> list_data = new ArrayList<ArrayList<Integer>>();
		//
		BufferedReader br = null;
		try{
			File file = new File(str_datafile);
			br = new BufferedReader(new FileReader(file));
			String line;
			String[] ar1;
			ArrayList<Integer> list1;
			while((line = br.readLine()) != null){
				ar1 = line.split(",");
				//
				list1 = new ArrayList<Integer>();
				for(int a1 = 0; a1 < ar1.length; a1++){
					int a2 = Integer.parseInt(ar1[a1]);
					if(a2 == -1){
						break;
					}
					list1.add(a2);
				}
				list_data.add(list1);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				br.close();
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		
		return list_data;
	}
	
	private static ArrayList<String> getData2(){
		//
		ArrayList<String> list_data = new ArrayList<String>();
		//
		BufferedReader br = null;
		try{
			File file = new File(str_datafile2);
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null){
				list_data.add(line);
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				br.close();
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		return list_data;
	}
}

