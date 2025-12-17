package p205;

//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
//import java.util.Arrays;
//ファイル操作用
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

無向グラフの直径を二段階 BFS により測定し、最遠層の候補から再探索して上限を精密化する方針を採った。  
入力検証と空グラフや非連結に対しては、未到達距離 -1 の扱いを維持しつつ最大値の比較対象から除外する設計とした。  
距離配列は初回到達時に最短が確定するため、訪問フラグを別途持たず dist の -1 判定のみで十分である。  
計算量は measure が U 候補数に依存して O(|U|·(V+E))。

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます




public class GraphDiameter{
	private static final String str_datafile = "src/p205/ex205_data_dblpgraph_adjlist.csv";
	private static final String str_datafile2 = "src/p205/ex205_data_dblpgraph_name.csv";
	
	public static void main(String[] args){
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
		
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	まず任意の頂点 v から BFS を行い最遠距離 maxDistFromV とその到達頂点集合 U を得る。  
	次に各 u∈U を始点として BFS を行い、得られた距離配列の最大値を局所上限 localMax とし、その最大を直径とする。  
	非連結の場合、到達不能頂点は距離 -1 のままであるため最大値算出から除外し、測定対象成分内での最大値を用いる。 	
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static int measure(ArrayList<ArrayList<Integer>> list_adjlist){
		//* 入力検証
        if (list_adjlist == null || list_adjlist.isEmpty()) return 0;

        //* 頂点数 n を取得し、任意の始点 v=0 から BFS
        int n = list_adjlist.size();
        int v = 0;
        int[] distFromV = search(list_adjlist, v);

        //* v からの最遠距離 maxDistFromV を抽出（未到達 -1 は無視）
        int maxDistFromV = -1;
        for (int d : distFromV) {
            if (d >= 0 && d > maxDistFromV) maxDistFromV = d;
        }

        //* 最遠層 U を収集（dist==maxDistFromV の頂点群）
        ArrayList<Integer> U = new ArrayList<>();
        if (maxDistFromV >= 0) {
            for (int i = 0; i < n; i++) {
                if (distFromV[i] == maxDistFromV) U.add(i);
            }
        }

        //* 各 u∈U を始点に BFS し、局所最大距離 localMax を求めて直径を更新
        int diameter = 0;
        for (int u : U) {
            int[] distFromU = search(list_adjlist, u);
            int localMax = 0;
            for (int d : distFromU) {
                if (d > localMax) localMax = d;
            }
            if (localMax > diameter) diameter = localMax;
        }

        //* 測定した直径を返却
        return diameter;
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	search は無向グラフ上での幅優先探索により、始点 initnode から各頂点への最短距離配列 dist を返す。  
    未到達は -1、始点は 0 とし、キューにより層状に展開する。隣接頂点 u が未到達（dist[u]==-1）であるときのみ dist[u]=dist[v]+1 として更新し、キューに追加する。  
    dist の -1/非 -1 判定により訪問管理を兼ねるため、visited 配列は不要である。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static int[] search(ArrayList<ArrayList<Integer>> list_adjlist, int initnode){
		//* 頂点数 n を取得し距離配列 dist を -1 で初期化
        int n = list_adjlist.size();
        int[] dist = new int[n];
        java.util.Arrays.fill(dist, -1);

        //* 幅優先探索のための待ち行列を用意し、始点を初期化
        Queue<Integer> q = new LinkedList<>();
        dist[initnode] = 0;
        q.add(initnode);

        //* BFS ループ（キューが尽きるまで層状展開）
        while (!q.isEmpty()) {
            //* 先頭 v を取り出して隣接を走査
            int v = q.poll();
            for (int u : list_adjlist.get(v)) {
                //* 未到達のみ距離を確定してキュー投入（最短性の保持）
                if (dist[u] == -1) {
                    dist[u] = dist[v] + 1;
                    q.add(u);
                }
            }
        }

        //* 各頂点への最短距離配列 dist を返却
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
		/*
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
		*/
		//test1-3
		int diametertest1ans3 = 4;
		boolean test1res3 = false;
		int diametertest1res3 = measure(list_adjlisttest1);
		//System.out.println(diametertest1ans3 + " " + diametertest1res3);
		test1res3 = (diametertest1ans3 == diametertest1res3);
		System.out.println(test1res3);
		
		
		//test2
		int nodenumtest2 = 9;
		int[][] ar_edgetest2 = {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 2}, {1, 6}, {2, 5}, {3, 5}, {4, 6}, {4, 7}, {4, 8}, {6, 7}};
		ArrayList<ArrayList<Integer>> list_adjlisttest2 = getAdjListFromEdges(ar_edgetest2, nodenumtest2, false);
		//System.out.println(list_adjlisttest2);//[[1, 2, 3, 4], [0, 2, 6], [0, 1, 5], [0, 5], [0, 6, 7, 8], [2, 3], [1, 4, 7], [4, 6], [4]]
		/*
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
		*/
		//test2-3
		int diametertest2ans3 = 4;
		boolean test2res3 = false;
		int diametertest2res3 = measure(list_adjlisttest2);
		//System.out.println(diametertest2ans3 + " " + diametertest2res3);
		test2res3 = (diametertest2ans3 == diametertest2res3);
		System.out.println(test2res3);
		
		
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
		//データ呼び出し
		ArrayList<ArrayList<Integer>> list_adjlisttest0 = getData();
		ArrayList<String> list_name = getData2();
		System.out.println("直径:" +  measure(list_adjlisttest0) + " " + (measure(list_adjlisttest0) == 24));
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
			while ((line = br.readLine()) != null){
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

